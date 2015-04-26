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
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import static sk.svec.jan.webdetection.extraction.Finder.html2text;

/**
 *
 * @author Ján Švec
 */
public class DiscussionFinder {

    private Node authorNode;
    private Node dateNode;
    private Node textNode;

    private List<HashMap<String, Integer>> allLevels;
    private int maxDepth;
    private boolean foundDateStringSwitch;
    private String documentPartNode;

    private boolean foundDate;
    private String date;
    private int dateCount;
    private int dateScore;

    private String today;
    private String yesterday;

    private List<String> linkAndPath;
    private int nullAuthor;

    public List<String> getLinkAndPath() {
        return linkAndPath;
    }

    public Node getAuthorNode() {
        return authorNode;
    }

    public Node getDateNode() {
        return dateNode;
    }

    public Node getTextNode() {
        return textNode;
    }

    public String getDate() {
        return date;
    }

    public int getDateScore() {
        return dateScore;
    }

    public int getDateCount() {
        return dateCount;
    }

    public int getNullAuthor() {
        return nullAuthor;
    }

    public DiscussionFinder(String path) throws Exception {
        linkAndPath = new ArrayList<String>();

        File folder = new File(path + "extracted/");
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                findData(path + "extracted/" + listOfFile.getName());
            }
        }

    }

    public void findData(String path) throws Exception {
        dateCount = 0;
        maxDepth = 0;
        foundDateStringSwitch = false;
        foundDate = false;
        File input = new File(path);

        Date todayDate = new Date(input.lastModified());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");
        today = dateFormat.format(todayDate);
        Date yesterdayDate = new Date(todayDate.getTime() - 1 * 24 * 3600 * 1000);
        yesterday = dateFormat.format(yesterdayDate);

        Document doc = Jsoup.parse(input, "UTF-8");
        Node node = doc;
        //Using EscapeMode.xhtml will give you output without entities. 
        //správne kódovanie
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

        traversePage(node);

        String filePath = path.substring(0, path.lastIndexOf("/") + 1);
        String outputPath = filePath.replace("extracted", "results");

        //create folder for comments
        String fileName = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));

//        String commentFolderPath = outputPath + fileName + "_comments/";
//        new File(commentFolderPath).mkdirs();
        //initialize
        allLevels = new ArrayList<HashMap<String, Integer>>();
        for (int i = 0; i <= maxDepth; i++) {
            allLevels.add(new HashMap<String, Integer>());
        }

        boolean findDocumentParts = findDocumentParts(node);
        if (findDocumentParts) {
//            System.out.println(documentPart);

            Elements documentParts = doc.select(documentPartNode);
            int i = 0;
            for (Element documentPart : documentParts) {
//                System.out.println(documentPart.toString()+"\n");
                DocumentPartFinder dpf = new DocumentPartFinder(documentPart.toString(), today, yesterday);
//                 System.out.println("celly komentar "+dpf.getDoc().text());
                for (Node nod : dpf.getNodesToRemove()) {
//            System.out.println(nod);
                    dpf.removeNodes(dpf.getNode(), nod);
                }
//                System.out.println("XXXXXXXX");
                String text = dpf.getDoc().text();
               
                if (text.trim().length() == 0) {
                    text = "null";
                }
                //ak nenajdeme text alebo autora tak nevypiseme nic
//                if (text.trim().length() != 0 && dpf.getAuthor() != null) {

                String name;
                if (dpf.getAuthor() == null) {
                    name = "null";
                    nullAuthor++;
                } else {
                    name = dpf.getAuthor().trim();
                }
            
                String date;
                if (dpf.getDate() == null) {
                    date = "null";
                } else {
                    date = dpf.getDate().trim();
                }

                String title = "diskusia";

                //remove html tags
                title = html2text(title);
                name = html2text(name);
                date = html2text(date);

                //odstrani autor: xxx, datum: xxx atd
//                if (name.indexOf(":") != -1) {
//                    name = name.substring(name.indexOf(":") + 1);
//                }

                date = findDateRegex(date);

                //nacitanie linku z exkterneho suboru       
                String linkPath = filePath.replace("extracted", "links");
                linkPath = linkPath + fileName + ".link";
                String link = new Scanner(new File(linkPath)).useDelimiter("\\A").next();

                String xmlPath = (outputPath + fileName + "_comment" + i + ".xml");

                linkAndPath.add("<a href=\"" + link + "\">" + link + "</a> - <a href=\"/WebStructureDetection-web/getfile?name=" + xmlPath + "\"> " + xmlPath + "</a>");

                WriteXMLFile wxmlf = new WriteXMLFile();
                wxmlf.createXmlFile(name.trim(), link.trim(), title.trim(), date.trim(), text.trim(), xmlPath);

                //cesty pre autora, ak  nenaslo, ulozi do specialneho suboru
                String xmlFileName;
                if (name.compareTo("null") == 0) {
                    xmlFileName = "deletedLinksLog.xml";
                    name = "";
                    date = "";
                    text = "";
                    title = xmlPath;
                } else {
                    xmlFileName = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(name.getBytes())) + ".xml";
                }
                // String xmlFileName = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(name.getBytes())) + ".xml";
                StringTokenizer st = new StringTokenizer(outputPath, "/");
                //cesta k suboru output/sk/cas/ napriklad
                String outputPath2 = "";
                for (int j = 0; j < 3; j++) {
                    outputPath2 += st.nextToken() + "/";
//                    System.out.println(st.nextToken());
                }
                String xmlAuthorPath = outputPath2 + "author/" + xmlFileName;
                new File(outputPath2 + "author/").mkdirs();

                //ulozenie autora
                if (text.compareTo("null") != 0) {
                    File f = new File(xmlAuthorPath);
                    if (f.isFile()) {
                        wxmlf.addToXmlFile(link.trim(), title.trim(), date.trim(), text.trim(), xmlAuthorPath);
                    } else {
                        wxmlf.createXmlFile(name.trim(), link.trim(), title.trim(), date.trim(), text.trim(), xmlAuthorPath);
                    }

                }
                System.out.println("username: " + name);
                System.out.println("date: " + date);
                System.out.println("text: " + text);

                System.out.println("comment " + i + "extracted succesfully\n");
//                }
                i++;
            }
        }

    }

    private String findDateRegex(String date) {
        //dni cislom, oddelovac: medzera,bodka,pomlcka a mozno medzera, mesiace cislom a slovom, roky 2 a 4 miestne       
        String daysNum = "\\d{1,2}";
        String monthsEN = "(jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sep(?:t|tember)?|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?";
        String monthsSK = "|jan(?:uár|uára)?|feb(?:ruár|ruára)?|mar(?:ec|ca)?|apr(?:íl|íla)?|máj(?:a)?|jún(?:a)?|júl(?:a)?|aug(?:usta)?|sep(?:tember|tembra)?|okt(?:óber|óbra)?|nov(?:ember|embra)?|dec(?:ember|embra)?";
        String monthsCZ = "|led(?:en|na)?|únor(?:a)?|břez(?:en|na)?|dub(?:en|na)?|květ(?:en|na)?|červ(?:en|na)?|červen(?:ce|ec)?|srp(?:en|na)?|zář(?:í)?|říj(?:en|na)?|list(?:opad|opadu)?|pros(?:inec|ince)?";
        String monthsNum = "|\\d{1,2})";
        String separator = "( |\\.|-|,|/) ?"; // medzera, bodka, pomlcka, nic a potom bud medzera alebo ne
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

    private boolean findDocumentParts(Node root) {

        Node node = root;
        int depth = 0;

        while (node != null) {

            if (node.nodeName().compareTo("#text") != 0) {
                HashMap<String, Integer> level = allLevels.get(depth);
//            System.out.println(depth + " " + allLevels.size());
                if (level.containsKey(node.nodeName() + "[class=" + node.attr("class") + "]")) {
                    Integer get = level.get(node.nodeName() + "[class=" + node.attr("class") + "]");

                    level.put(node.nodeName() + "[class=" + node.attr("class") + "]", get + 1);
                } else {
                    level.put(node.nodeName() + "[class=" + node.attr("class") + "]", 1);
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
        //ak je 0 alebo 1 datum, vratime false, kedze sa to neda zistit
        if (dateCount < 2) {
            return false;
        } else {
            return findOnePart(dateCount);
        }

    }

    private boolean findOnePart(int commentCount) {
        int i = 0;
        for (HashMap<String, Integer> level : allLevels) {
            for (String nodeWithClass : level.keySet()) {
                if (level.get(nodeWithClass).compareTo(commentCount) == 0) {
//                    System.out.println(i + " " + nodeWithClass+" ");                   
                    nodeWithClass = nodeWithClass.replace("[class=]", "");
                    documentPartNode = nodeWithClass;
//                    System.out.println("xxxxxxx"+documentPartNode);
                    return true;
                }
            }

            i++;
        }
        return false;
    }

    private void traversePage(Node root) {
        Node node = root;
        int depth = 0;

        while (node != null) {
//            System.out.println(depth + " " + node.nodeName() + " " + node.childNodeSize());
//          if(node.attr("class").compareTo("contribution")==0){
//              System.out.println(depth);
//          }
            if (maxDepth < depth) {
                maxDepth = depth;
            }

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

    private boolean analyze(Node node) {
        // System.out.println(node.nodeName());

        for (Attribute attribute : node.attributes().asList()) {
            String key = attribute.getKey();
            String value = attribute.getValue();
//            System.out.println(" attr:" + key + " value:" + value);
            if (!foundDateStringSwitch) {
                foundDateStringSwitch = findDate(node, value);
            }
            if (foundDateStringSwitch) {
                boolean foundDateString = findDate(node, value);
                if (foundDateString) {
                    String child = node.childNode(0).toString();
                    foundDate = findDateValue(node, child);
                    dateScore = 10;

                }
            } else {
                foundDate = findDateValue(node, value);
                dateScore = 5;
            }

        }

        return false;
//        return foundDate && foundAuthor && foundText;
    }

    public boolean findDate(Node node, String value) {
        //slovo datum v texte nehladame
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
        String todayYesterday = "|(dnes|včera|today|yesterday)..?([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]";

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
            dateCount++;
            dateNode = node;
////            System.out.println("date: " + date.trim());
            return true;
        }

        return false;

    }

}
