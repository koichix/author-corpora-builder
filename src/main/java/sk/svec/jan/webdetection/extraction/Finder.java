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
package sk.svec.jan.webdetection.extraction;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import org.apache.commons.codec.binary.Hex;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Node;

/**
 *
 * @author Ján Švec
 */
public class Finder {

    private boolean foundDate;
    private String date;
    private int dateScore;

    private boolean foundAuthor;
    private String author;
    private int authorScore;

    private boolean foundTitle;
    private String title;
    private int titleScore;

    private String today;
    private String yesterday;

    private Document doc;
    private Node node;

    private List<Node> nodesToRemove;
    private List<String> linkAndPath;

    private int nullAuthor;

    public Document getDoc() {
        return doc;
    }

    public Node getNode() {
        return node;
    }

    public String getDate() {
        return date;
    }

    public int getDateScore() {
        return dateScore;
    }

    public String getAuthor() {
        return author;
    }

    public int getAuthorScore() {
        return authorScore;
    }

    public String getTitle() {
        return title;
    }

    public int getTitleScore() {
        return titleScore;
    }

    public List<Node> getNodesToRemove() {
        return nodesToRemove;
    }

    public List<String> getLinkAndPath() {
        return linkAndPath;
    }

    public int getNullAuthor() {
        return nullAuthor;
    }

    public Finder(String path) throws Exception {
        linkAndPath = new ArrayList<String>();

        File folder = new File(path + "extracted/");
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {

            if (listOfFile.isFile()) {
                //vynuluje data, lebo ideme hladat druhy subor
                nullData();

                findData(path + "extracted/" + listOfFile.getName());
                String xmlPath = path + "results/" + listOfFile.getName().substring(0, listOfFile.getName().indexOf(".")) + ".xml";

                if (author == null) {
                    author = "null";
                    nullAuthor++;
                }
                if (date == null) {
                    date = "null";
                }
                if (title == null) {
                    title = "null";
                }

                //nastavime si nazov suboru, podla mena autora
                String linkPath = path + "links/" + listOfFile.getName().substring(0, listOfFile.getName().indexOf(".")) + ".link";

                //jednoriadkove nacitanie obsahu suboru
                String link = new Scanner(new File(linkPath)).useDelimiter("\\A").next();
//                linkAndPath.add(link + " - " + xmlPath);
                linkAndPath.add("<a href=\"" + link + "\">" + link + "</a> - <a href=\"/WebStructureDetection-web/getfile?name=" + xmlPath + "\"> " + xmlPath + "</a>");

                markBadText(getNode());
                //remove author,date,title from tree
                for (Node nod : getNodesToRemove()) {
                    removeNodes(getNode(), nod);
                }

                String text;
                text = getDoc().text();
                if (getDoc().text().compareTo("") == 0) {
                    text = "null";
                }

                //remove html tags
                title = html2text(title);
                author = html2text(author);
                date = html2text(date);

                //odstrani autor: xxx, datum: xxx atd
                if (author.indexOf(":") != -1) {
                    author = author.substring(author.indexOf(":") + 1);
                }

                //ocisti datum
                date = findDateRegex(date);

                //ocisti nadpis (napr idnes.cz)
                URL url = new URL(link);
                String host = url.getHost();
                if (host.startsWith("www.")) {
                    host = host.substring(4);
                }

                title = title.replaceAll("(?i)" + host, "");

                System.out.println("path: " + path + "extracted/" + listOfFile.getName());
                System.out.println("author: " + author.trim() + " " + getAuthorScore());
                System.out.println("date: " + date.trim() + " " + getDateScore());
                System.out.println("title: " + title.trim() + " " + getTitleScore());
                System.out.println("text: " + text.trim());

                WriteXMLFile wxmlf = new WriteXMLFile();
                wxmlf.createXmlFile(author.trim(), link.trim(), title.trim(), date.trim(), text.trim(), xmlPath);

//                //ukladanie autora, ak  nenaslo, ulozi do specialneho suboru
                String xmlFileName;
                if (author.compareTo("null") == 0 || text.compareTo("null") == 0) {
                    xmlFileName = "deletedLinksLog.xml";
                    author = "";
                    date = "";
                    text = "";
                    title = xmlPath;
                } else {
                    xmlFileName = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(author.getBytes())) + ".xml";
                }
//                String xmlFileName = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(author.getBytes())) + ".xml";

                StringTokenizer st = new StringTokenizer(path, "/");
                //cesta k suboru output/sk/cas/ napriklad
                String outputPath = "";
                for (int j = 0; j < 3; j++) {
                    outputPath += st.nextToken() + "/";
//                    System.out.println(st.nextToken());
                }
                String xmlAuthorPath = outputPath + "author/" + xmlFileName;
                new File(outputPath + "author/").mkdirs();
                //ukladanie pod menom autora, ak existuje subor s autorom, tak ho vezme a ulozi priamo tam
                File f = new File(xmlAuthorPath);
                if (f.isFile()) {
                    wxmlf.addToXmlFile(link.trim(), title.trim(), date.trim(), text.trim(), xmlAuthorPath);
                } else {
                    wxmlf.createXmlFile(author.trim(), link.trim(), title.trim(), date.trim(), text.trim(), xmlAuthorPath);
                }
            }
        }
    }

    private void nullData() {
        foundDate = false;
        foundAuthor = false;
        foundTitle = false;
        author = null;
        date = null;
        title = null;
        authorScore = 0;
        dateScore = 0;
        titleScore = 0;
    }

    private String findDateRegex(String date) {
        //dni cislom, oddelovac: medzera,bodka,pomlcka a mozno medzera, mesiace cislom a slovom, roky 2 a 4 miestne       
        String daysNum = "\\d{1,2}";
        String monthsEN = "(jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sep(?:t|tember)?|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?";
        String monthsSK = "|jan(?:uár|uára)?|feb(?:ruár|ruára)?|mar(?:ec|ca)?|apr(?:íl|íla)?|máj(?:a)?|jún(?:a)?|júl(?:a)?|aug(?:usta)?|sep(?:tember|tembra)?|okt(?:óber|óbra)?|nov(?:ember|embra)?|dec(?:ember|embra)?";
        String monthsCZ = "|led(?:en|na)?|únor(?:a)?|břez(?:en|na)?|dub(?:en|na)?|květ(?:en|na)?|červ(?:en|na)?|červen(?:ce|ec)?|srp(?:en|na)?|zář(?:í)?|říj(?:en|na)?|list(?:opad|opadu)?|pros(?:inec|ince)?";
        String monthsNum = "|\\d{1,2})";
        String separator = "( |\\.|-|,) ?"; // medzera, bodka, pomlcka, nic a potom bud medzera alebo ne
        String years = "(\\d{2,4})?";
        String todayYesterday = "|(dnes|včera|today|yesterday)..?([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]"; //dnes 12:25

        String strPattern = daysNum.concat(separator).concat(monthsEN).concat(monthsCZ).concat(monthsSK).concat(monthsNum).concat(separator).concat(years).concat(todayYesterday);
        Pattern pattern = Pattern.compile(strPattern, CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(date);
        while (matcher.find()) {
            String foundExpression = matcher.group();
//            System.out.println(foundExpression);
//            System.out.println(value); 

            //convert today/yesterday to actual date
            Pattern todayYesterdayPattern = Pattern.compile(todayYesterday.substring(1), CASE_INSENSITIVE);
            Matcher todayYesterdayMatcher = todayYesterdayPattern.matcher(foundExpression);
            while (todayYesterdayMatcher.find()) {
                foundExpression = foundExpression.replaceAll("dnes|today", today);
                foundExpression = foundExpression.replaceAll("včera|yesterday", yesterday);
            }

            return foundExpression;
        }
        return "null";

    }

    private void markBadText(Node root) {
        Node node = root;
        int depth = 0;

        while (node != null) {
            //ak sa jedna o text, ktory ma menej ako 15 znakov
            if (node.nodeName().compareTo("#text") == 0) {
                if (node.toString().trim().length() < 20) {
                    nodesToRemove.add(node);
//                    System.out.println(node);
                }

            }
            if (node.childNodeSize() > 0) {
                node = node.childNode(0);
                depth++;
            } else {
                while (node.nextSibling() == null && depth > 0) {
                    node = node.parentNode();
                    depth--;
                }

                if (node == root) {
                    break;
                }
                node = node.nextSibling();
            }

        }

    }

    public void findData(String path) throws IOException {
        this.nodesToRemove = new ArrayList<Node>();

        File input = new File(path);

        Date todayDate = new Date(input.lastModified());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");
        today = dateFormat.format(todayDate);
        Date yesterdayDate = new Date(todayDate.getTime() - 1 * 24 * 3600 * 1000);
        yesterday = dateFormat.format(yesterdayDate);

        doc = Jsoup.parse(input, "UTF-8");
        node = doc;
        //Using EscapeMode.xhtml will give you output without entities. 
        //správne kódovanie
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        traversePage(node);
        traversePageFindAuthor(node);
    }

    public Node removeNodes(Node root, Node nodeToRemove) {
        Node node = root;
        Node ntr = nodeToRemove;
        int depth = 0;

        while (node != null) {
            if (node.equals(ntr)) {
                node.remove();
                return root;
            }
            if (node.childNodeSize() > 0) {
                node = node.childNode(0);
                depth++;
            } else {
                while (node.nextSibling() == null && depth > 0) {
                    node = node.parentNode();
                    depth--;
                }

                if (node == root) {
                    break;
                }
                node = node.nextSibling();
            }

        }
        return root;
    }

    public void traversePage(Node root) {
        Node node = root;
        int depth = 0;

        while (node != null) {
//            System.out.println(depth + " " + node.nodeName() + " " + node.childNodeSize());
//            System.out.println(node.attributes());
            boolean analyze = analyze(node);
            if (analyze) {
                break;
            }
            if (node.childNodeSize() > 0) {
                node = node.childNode(0);
                depth++;
            } else {
                while (node.nextSibling() == null && depth > 0) {
                    node = node.parentNode();
                    depth--;
                }

                if (node == root) {
                    break;
                }
                node = node.nextSibling();
            }

        }
    }

    public void traversePageFindAuthor(Node root) {
        Node node = root;
        int depth = 0;

        while (node != null) {
//            System.out.println(depth + " " + node.nodeName() + " " + node.childNodeSize());
//            System.out.println(node.attributes());
            for (Attribute attribute : node.attributes().asList()) {

                String value = attribute.getValue();
                if (!foundAuthor) {
                    foundAuthor = findAuthorInText(node, value);
                    break;
                }

            }
            if (node.childNodeSize() > 0) {
                node = node.childNode(0);
                depth++;
            } else {
                while (node.nextSibling() == null && depth > 0) {
                    node = node.parentNode();
                    depth--;
                }

                if (node == root) {
                    break;
                }
                node = node.nextSibling();
            }

        }
    }

    private boolean analyze(Node node) {
        // System.out.println(node.nodeName());

        for (Attribute attribute : node.attributes().asList()) {
            String key = attribute.getKey();
            String value = attribute.getValue();
//            System.out.println(" attr:" + key + " value:" + value);
            if (!foundDate) {
                boolean foundDateString = findDate(node, value);
                if (foundDateString) {
                    if (node.childNodeSize() != 0) {
                        String child = node.childNode(0).toString();
                        foundDate = findDateValue(node, child);
                        dateScore = 10;
                    } else {

                    }

                } else {
//                    nodesToRemove.add(node);
                    foundDate = findDateValue(node, value);
                    dateScore = 5;
                }
                if (foundDate) {
                    nodesToRemove.add(node);
                }
            }

            if (!foundAuthor) {
                foundAuthor = findAuthor(node, value);
            }

        }
        if (!foundTitle) {
            foundTitle = findTitle(node, node.nodeName());
        }
        return foundDate && foundAuthor && foundTitle;
    }

    public boolean findDate(Node node, String value) {
        //slovo date v texte nehladame
        if (node.nodeName().compareTo("#text") == 0) {
            return false;
        }
        //hladame text date, datum, dátum
        Pattern datePattern = Pattern.compile("date|datum|dátum", CASE_INSENSITIVE);
        Matcher dateMatcher = datePattern.matcher(value);
        while (dateMatcher.find()) {
//       if (value.contains("date") || value.contains("datum") || value.contains("dátum")) {            
            return true;
        }
        return false;

    }

    public boolean findDateValue(Node node, String value) {
        //dni cislom, oddelovac: medzera,bodka,pomlcka a mozno medzera, mesiace cislom a slovom, roky 2 a 4 miestne       
        String daysNum = "\\d{1,2}";
        String monthsEN = "(jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sep(?:t|tember)?|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?";
        String monthsSK = "|jan(?:uár|uára)?|feb(?:ruár|ruára)?|mar(?:ec|ca)?|apr(?:íl|íla)?|máj(?:a)?|jún(?:a)?|júl(?:a)?|aug(?:usta)?|sep(?:tember|tembra)?|okt(?:óber|óbra)?|nov(?:ember|embra)?|dec(?:ember|embra)?";
        String monthsCZ = "|led(?:en|na)?|únor(?:a)?|břez(?:en|na)?|dub(?:en|na)?|květ(?:en|na)?|červ(?:en|na)?|červen(?:ce|ec)?|srp(?:en|na)?|zář(?:í)?|říj(?:en|na)?|list(?:opad|opadu)?|pros(?:inec|ince)?";
        String monthsNum = "|\\d{1,2})";
        String separator = "( |\\.|-) ?"; // medzera, bodka, pomlcka, nic a potom bud medzera alebo ne
        String years = "(\\d{2,4})?";
        String todayYesterday = "|(dnes|včera|today|yesterday)..?([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]"; //dnes 12:25

        String strPattern = daysNum.concat(separator).concat(monthsEN).concat(monthsCZ).concat(monthsSK).concat(monthsNum).concat(separator).concat(years).concat(todayYesterday);
        Pattern pattern = Pattern.compile(strPattern, CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
//            String foundExpression = matcher.group();
//            System.out.println(foundExpression);
//            System.out.println(value); 

            //convert today/yesterday to actual date
            Pattern todayYesterdayPattern = Pattern.compile(todayYesterday.substring(1), CASE_INSENSITIVE);
            Matcher todayYesterdayMatcher = todayYesterdayPattern.matcher(value);
            while (todayYesterdayMatcher.find()) {
                value = value.replaceAll("dnes|today", today);
                value = value.replaceAll("včera|yesterday", yesterday);
            }

            date = value;
            return true;
        }
        return false;

    }

    public boolean findAuthorInText(Node node, String value) {
        //hladanie slova autor: v texte
        Pattern capital = Pattern.compile("([A-Z]|Á|Č|Ď|É|Í|Ň|Ó|Ř|Š|Ť|Ú|Ý|Ž)");
        Pattern woman = Pattern.compile("ová\\b", CASE_INSENSITIVE);

        Pattern autor = Pattern.compile("Napísal:|Napsal:|Vložil:|Author:|Autor:|Autori:|Autoři:", CASE_INSENSITIVE);
        if (node.nodeName().compareTo("#text") == 0) {
            Matcher autMatcher = autor.matcher(value);
            while (autMatcher.find()) {
                nodesToRemove.add(node);
                author = node.toString();
                authorScore = 5;

                Matcher capitalMatcher = capital.matcher(author);
                while (capitalMatcher.find()) {
                    authorScore++;
                }
                Matcher womanMatcher = woman.matcher(author);
                while (womanMatcher.find()) {
                    authorScore++;
                }
                return true;
            }
        }
        return false;
    }

    public boolean findAuthor(Node node, String value) {
        //slovo autor v texte nehladame
        if (node.nodeName().compareTo("#text") == 0) {
            return false;
        }
        //ignore this patterns
        Pattern photo = Pattern.compile("foto|photo", CASE_INSENSITIVE);

        //add score when this pattern
        Pattern capital = Pattern.compile("([A-Z]|Á|Č|Ď|É|Í|Ň|Ó|Ř|Š|Ť|Ú|Ý|Ž)");
        Pattern woman = Pattern.compile("ová\\b", CASE_INSENSITIVE);

        Pattern authorPattern = Pattern.compile("autor|autori|autoři|author|authors", CASE_INSENSITIVE);
        Matcher authorMatcher = authorPattern.matcher(value);
        while (authorMatcher.find()) {
            nodesToRemove.add(node);
            author = node.toString();
            authorScore = 10;
            Matcher photoMatcher = photo.matcher(author);
            while (photoMatcher.find()) {
                return false;
            }

            Matcher capitalMatcher = capital.matcher(author);
            while (capitalMatcher.find()) {
                authorScore++;
            }
            Matcher womanMatcher = woman.matcher(author);
            while (womanMatcher.find()) {
                authorScore++;
            }

            return true;
        }

        return false;

    }

    public boolean findTitle(Node node, String nodeName) {
        //v texte nehladame
        if (node.nodeName().compareTo("#text") == 0) {
            return false;
        }

        //  <meta property="og:title" content="Exmarkizáčka Mečiarová sa nevie zmieriť s rozvodom: Odkazy plné sklamania a hnevu" />
        if (nodeName.compareTo("meta") == 0) {
            boolean propertyTitle = false;
            for (Attribute attribute : node.attributes().asList()) {
                String key = attribute.getKey();
                String value = attribute.getValue();

                if (key.compareTo("property") == 0 && value.compareTo("og:title") == 0) {
                    propertyTitle = true;
                    break;
                }
            }
            if (propertyTitle) {
                nodesToRemove.add(node);
                title = node.attr("content");
                titleScore = 10;
                return true;
            }
        }

        Pattern titlePattern = Pattern.compile("h1", CASE_INSENSITIVE);
        Pattern titlePattern2 = Pattern.compile("title", CASE_INSENSITIVE);
        Matcher titleMatcher = titlePattern.matcher(nodeName);
        while (titleMatcher.find()) {
            nodesToRemove.add(node);
            title = node.childNode(0).toString();
            titleScore = 10;
            return true;
        }
        Matcher titleMatcher2 = titlePattern2.matcher(nodeName);
        while (titleMatcher2.find()) {
            nodesToRemove.add(node);
            title = node.childNode(0).toString();
            titleScore = 5;
            return true;
        }
        return false;
    }

    /**
     * Remove all html tags from string
     *
     * @param html
     * @return
     */
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}
