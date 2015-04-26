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
package sk.svec.jan.acb.crawling;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.List;
import sk.svec.jan.acb.main.WebDetectionMain;

/**
 *
 * @author Ján Švec
 */
public class Crawler {

    private List<String> seeds;
    private String url;

    public Crawler(List<String> seeds, boolean disc) {

        this.seeds = seeds;
        WebURL url = new WebURL();
        url.setURL(this.seeds.get(0));
        this.url = url.getDomain() + "|" + disc;
    }
    CrawlController controller;

    public void startCrawling() throws Exception {
        String crawlStorageFolder = "crawlStorageFolder/";
        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        /*
         * Be polite: Make sure that we don't send more than 1 request per
         * second (1000 milliseconds between requests).
         */
        config.setPolitenessDelay(Integer.parseInt(WebDetectionMain.getResourceBundle().getString("PolitenessDelay")));
        config.setMaxPagesToFetch(-1);

        config.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        for (String seed : seeds) {
            controller.addSeed(seed);
        }
        controller.setCustomData(url);

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(CrawlerConfig.class, numberOfCrawlers);

//        zistili ze sme skoncili
//        controller.isFinished();
//         // Wait for 30 seconds
//    Thread.sleep(30 * 1000);
//
//    // Send the shutdown request and then wait for finishing
        controller.shutdown();

//    controller.waitUntilFinish();
//        System.out.println(controller.isFinished());
    }

}
