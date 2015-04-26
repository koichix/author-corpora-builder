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
package sk.svec.jan.acb.extraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Node;

/**
 *
 * @author Ján Švec
 */
public class DocumentPartFinder {

    private Node authorNode;
    private Node dateNode;
    private Node textNode;
    private Node node;
    private Document doc;

    private List<HashMap<String, Integer>> allLevels;
    private int maxDepth = 0;
    private boolean foundDateStringSwitch = false;
    private String documentPartNode;

    private List<Node> nodesToRemove;

    private boolean foundDateStringSw = false;

    private boolean foundDate = false;
    private String date;
    private int dateCount = 0;
    private int dateScore;

    private boolean foundAuthor = false;
    private String author;
    private int authorCount = 0;
    private int authorScore;

    private boolean foundText = false;
    private String text;
    private int textCount = 0;
    private int textScore;

    private String today;
    private String yesterday;

    public Document getDoc() {
        return doc;
    }

    public Node getNode() {
        return node;
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

    public String getAuthor() {
        return author;
    }

    public int getAuthorScore() {
        return authorScore;
    }

    public int getDateCount() {
        return dateCount;
    }

    public int getAuthorCount() {
        return authorCount;
    }

    public String getText() {
        return text;
    }

    public int getTextCount() {
        return textCount;
    }

    public int getTextScore() {
        return textScore;
    }

    public List<Node> getNodesToRemove() {
        return nodesToRemove;
    }

    public DocumentPartFinder(String part, String today, String yesterday) throws IOException {
        this.nodesToRemove = new ArrayList<Node>();

        this.today = today;
        this.yesterday = yesterday;

        doc = Jsoup.parse(part, "UTF-8");
        node = doc;
        //Using EscapeMode.xhtml will give you output without entities. 
        //správne kódovanie
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

        traverseDocPart(node);
        markBadText(node);

    }

    private void traverseDocPart(Node root) {
        Node node = root;
        int depth = 0;

        while (node != null) {

            boolean analyzeDocPart = analyzeDocPart(node);
            if (analyzeDocPart) {
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

    private boolean analyzeDocPart(Node node) {
        // System.out.println(node.nodeName());

        for (Attribute attribute : node.attributes().asList()) {
            String key = attribute.getKey();
            String value = attribute.getValue();
//            System.out.println(" attr:" + key + " value:" + value);

            if (!foundDateStringSw) {
                foundDateStringSw = findDate(node, value);
            }
            if (foundDateStringSw) {
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
            if (foundDate) {
//                nodesToRemove.add(node);
            }

            if (!foundAuthor) {
                foundAuthor = findAuthor(node, value);
            }

        }
//        if (!foundTitle) {
//            foundTitle = findTitle(node, node.nodeName());
//        }

        return foundDate && foundAuthor; //&& foundTitle;
    }

    public boolean findDate(Node node, String value) {
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
        String separator = "( |\\.|-|,|/) ?"; // medzera, bodka, pomlcka, nic a potom bud medzera alebo ne
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
//            System.out.println("date: " + date.trim());
            return true;
        }

        return false;

    }

    public boolean findAuthor(Node node, String value) {
        if (node.nodeName().compareTo("#text") == 0) {
            return false;
        }
        Pattern badAuthorPattern = Pattern.compile("user_reaction", CASE_INSENSITIVE);
        Pattern komentUsera = Pattern.compile("komentusera", CASE_INSENSITIVE);
        Matcher komentMatcher = komentUsera.matcher(value);
        while (komentMatcher.find()) {
            return false;
        }
        
        Pattern authorPattern = Pattern.compile("username|author|name|user|nick|profil", CASE_INSENSITIVE);
        Matcher authorMatcher = authorPattern.matcher(value);
        while (authorMatcher.find()) {

            nodesToRemove.add(node);
//System.out.println(node+"\n");
            if (node.childNodeSize() == 0) {
                return false;
            }
            if (node.childNode(0).toString().trim().length() == 0) {
                return false;
            }
            Matcher photoMatcher = badAuthorPattern.matcher(node.toString());
            while (photoMatcher.find()) {
                return false;
            }

            author = node.childNode(0).toString();
            authorScore = 10;
            authorCount++;
            authorNode = node;
//            System.out.println("username: " + author);
            return true;

        }
        return false;

    }

    private void markBadText(Node root) {
        Node node = root;
        int depth = 0;

        while (node != null) {
            //ak sa jedna o text, ktory ma menej ako 15 znakov
            if (node.nodeName().compareTo("#text") == 0) {
                if (node.toString().trim().length() < 23) {
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

}
