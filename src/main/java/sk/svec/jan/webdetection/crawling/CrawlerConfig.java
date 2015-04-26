/*
 * Copyright 2015 Ján Švec
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package sk.svec.jan.webdetection.crawling;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Hex;
import sk.svec.jan.webdetection.main.WebDetectionMain;

/**
 *
 * @author Ján Švec
 */
public class CrawlerConfig extends WebCrawler {

    private static int count;
    private boolean forum;
    private MessageDigest md;
    private int depth = Integer.parseInt(WebDetectionMain.getResourceBundle().getString("MaxDepth"));
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz|rss|asp|xml)(|\\?.*))$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        String href = url.getURL().toLowerCase();

        String customData = this.getMyController().getCustomData().toString();
        forum = Boolean.parseBoolean(customData.substring(customData.indexOf("|") + 1));

        String customDataUrl = customData.substring(0, customData.indexOf("|"));
        return !FILTERS.matcher(href).matches() && href.contains(customDataUrl + "/");

    }

    public static String createDirStructure(URL url, int depth) {

        StringBuilder sb = new StringBuilder();
        String host = url.getHost();
        if (host.startsWith("www.")) {
            host = host.substring(4);
        }
        int count = host.length() - host.replace(".", "").length();
        if (depth > 0 && depth < count) {
            count = depth;
        }
        for (int i = 0; i < count; i++) {
            String level = host.substring(host.lastIndexOf(".") + 1);
            host = host.substring(0, host.lastIndexOf("."));
            //System.out.print(level + "/");
            sb.append(level);
            sb.append(File.separator);
            if ((i + 1) == count) {
//                System.out.print(host + "/");
                if (host.indexOf(".") != -1) {
                    sb.append(host.substring(host.lastIndexOf(".") + 1));

                } else {
                    sb.append(host);
                }

                sb.append(File.separator);
            }
        }
        return sb.toString();
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        System.out.println(count);
        System.out.println(WebDetectionMain.getResourceBundle().getString("MaxPagesToFetch"));
        if (count >= Integer.parseInt(WebDetectionMain.getResourceBundle().getString("MaxPagesToFetch"))) {
            CrawlController myController = getMyController();
            myController.shutdown();
            myController.getPageFetcher().shutDown();
            //Vynulujeme pocitadlo, velmi dolezite
            count = 0;
        }

        try {
            md = MessageDigest.getInstance("MD5");
            try {
                String url = page.getWebURL().getURL();
                System.out.println("URL: " + url);
                URL jUrl = new URL(url);
//                String path = "output/downloaded" + File.separator;
                String path = "output" + File.separator + createDirStructure(jUrl, depth) + "downloaded" + File.separator;
                String path2 = "output" + File.separator + createDirStructure(jUrl, depth) + "links" + File.separator;
                System.out.println(path);
                if (Boolean.parseBoolean(WebDetectionMain.getResourceBundle().getString("CustomUrlSet"))) {
                    if (url.startsWith(WebDetectionMain.getResourceBundle().getString("CustomUrl"))) {
                        new File(path).mkdirs(); //create directory
                        new File(path2).mkdirs(); //create directory
                    }
                } else {
                    new File(path).mkdirs(); //create directory
                    new File(path2).mkdirs(); //create directory
                }

                if (page.getParseData() instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    String html = htmlParseData.getHtml();
                    String title = htmlParseData.getTitle();

                    String text = htmlParseData.getText();
                    Set<WebURL> links = htmlParseData.getOutgoingUrls();

//                    Header[] responseHeaders = page.getFetchResponseHeaders();
//                    if (responseHeaders != null) {
//                        System.out.println("Response headers:");
//                        for (Header header : responseHeaders) {
//                            System.out.println("\t" + header.getName() + ": " + header.getValue());
//                        }
//                    }                   
                    System.out.println("Docid: " + page.getWebURL().getDocid());
                    System.out.println("Text length: " + text.length());
                    System.out.println("Html length: " + html.length());
                    System.out.println("Number of outgoing links: " + links.size());
                    System.out.println("Title: " + title);
                    System.out.println();

                    PrintWriter out;
                    PrintWriter out2;

                    try {
                        try {
                            if (Boolean.parseBoolean(WebDetectionMain.getResourceBundle().getString("CustomUrlSet"))) {

                                if (url.startsWith(WebDetectionMain.getResourceBundle().getString("CustomUrl"))) {

                                    String filename;
                                    if (forum) {
                                        filename = Hex.encodeHexString(md.digest(url.getBytes()));
                                    } else {
                                        filename = Hex.encodeHexString(md.digest(title.getBytes()));
                                    }
                                    if (new File(path + filename + ".html").isFile()) {
                                        count--;
                                    }
                                    out = new PrintWriter(path + filename + ".html", "UTF-8");
                                    out.println(html);
                                    if (new File(path + filename + ".html").isFile()) {
                                        count++;
                                    }
                                    out.close();

                                    String linkPath = path.replace("downloaded", "links");
                                    out2 = new PrintWriter(linkPath + filename + ".link");
                                    out2.println(url);
                                    out2.close();
                                }
                            } else {
                                String filename;
                                if (forum) {
                                    filename = Hex.encodeHexString(md.digest(url.getBytes()));
                                } else {
                                    filename = Hex.encodeHexString(md.digest(title.getBytes()));
                                }
                                if (new File(path + filename + ".html").isFile()) {
                                    count--;
                                }
                                out = new PrintWriter(path + filename + ".html", "UTF-8");
                                out.println(html);
                                if (new File(path + filename + ".html").isFile()) {
                                    count++;
                                }
                                out.close();

                                String linkPath = path.replace("downloaded", "links");
                                out2 = new PrintWriter(linkPath + filename + ".link");
                                out2.println(url);
                                out2.close();

                            }
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(CrawlerConfig.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(CrawlerConfig.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(CrawlerConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CrawlerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
