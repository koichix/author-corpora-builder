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
package sk.svec.jan.acb.postprocess;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.codec.binary.Hex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import sk.svec.jan.acb.extraction.WriteXMLFile;
import sk.svec.jan.acb.utility.TravelDirectory;

/**
 *
 * @author Koichi
 */
public class ResultsCleaner {

    private String convertMonthsToNumbers(String date) {
        String one = "jan(?:uára|uár)?|led(?:en|na)";
        String two = "feb(?:ruára|ruár)?|únor(?:a)";
        String three = "mar(?:ec|ca)?|břez(?:en|na)";
        String four = "apr(?:íla|íl)?|dub(?:en|na)";
        String five = "máj(?:a)?|květ(?:en|na)";
        String six = "jún(?:a)?|červ(?:en|na)";
        String seven = "júl(?:a)?|červen(?:ce|ec)";
        String eight = "aug(?:usta)?|srp(?:en|na)";
        String nine = "sep(?:tember|tembra)?|zář(?:í)";
        String ten = "okt(?:óber|óbra)?|říj(?:en|na)";
        String eleven = "nov(?:ember|embra)?|list(?:opadu|opad)";
        String twelve = "dec(?:ember|embra)?|pros(?:inec|ince)";

        date = date.replaceAll(one, "01.");
        date = date.replaceAll(two, "02.");
        date = date.replaceAll(three, "03.");
        date = date.replaceAll(four, "04.");
        date = date.replaceAll(five, "05.");
        date = date.replaceAll(seven, "07.");
        date = date.replaceAll(six, "06.");
        date = date.replaceAll(eight, "08.");
        date = date.replaceAll(nine, "09.");
        date = date.replaceAll(ten, "10.");
        date = date.replaceAll(eleven, "11.");
        date = date.replaceAll(twelve, "12.");
        return date;
    }

    private String cleanAuthor(String author) {
     
        List<String> keywords = new LinkedList<>();
        //agencies
        keywords.add("ČTK");
        keywords.add("mf dnes");
        keywords.add("tasr");
        keywords.add("sita");
        keywords.add("mafra");
        //servers
        keywords.add("idnes.cz");
        keywords.add("natoaktual.cz");
        keywords.add("profimedia.cz");
        //junk
        keywords.add("celý článek");

        for (String agency : keywords) {
            // [keyword, ] OR [ ,keyword]
            author = author.replaceAll("(?i)" + agency + "\\, |\\, " + agency, "");
            author = author.replaceAll("(?i)" + agency, "");
        }

        //remove four,three or two small letters followed by one capital (fkaFilip Klicnar -> Filip Klicnar) 
        String strPattern = "\\p{Ll}\\p{Ll}\\p{Ll}\\p{Ll}\\p{Lu}|\\p{Ll}\\p{Ll}\\p{Ll}\\p{Lu}|\\p{Ll}\\p{Ll}\\p{Lu}";
        Pattern pattern = Pattern.compile(strPattern);

        Matcher matcher = pattern.matcher(author);
        while (matcher.find()) {
            String foundExpression = matcher.group();
            author = author.replaceAll(foundExpression.substring(0, foundExpression.length() - 1), "");
        }

        return author;
    }

    public ResultsCleaner() throws Exception {
        TravelDirectory travelDirectory = new TravelDirectory("output/");
        List<String> directories = travelDirectory.getDirectories();
        for (String p : directories) {

            if (new File(p + "results/").exists()) {
                File folder = new File(p + "results/");
                File[] listOfFiles = folder.listFiles();

                for (File listOfFile : listOfFiles) {

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();

                    Document doc = db.parse(new FileInputStream(listOfFile));

                    Element rootElement = doc.getDocumentElement();
                    NodeList elementsByTagName = rootElement.getElementsByTagName("Name");
                    NodeList childNodes = rootElement.getElementsByTagName("Article").item(0).getChildNodes();

                    String author = elementsByTagName.item(0).getTextContent();
                    String link = childNodes.item(0).getTextContent();
                    String title = childNodes.item(1).getTextContent();
                    String date = childNodes.item(2).getTextContent();
                    String text = childNodes.item(3).getTextContent();

                    if (author.compareTo("null") == 0
                            || link.compareTo("null") == 0
                            || title.compareTo("null") == 0
                            || date.compareTo("null") == 0
                            || text.compareTo("null") == 0) {
                        //something is null - don't save
                    } else {
                        System.out.println(author);
                        date = convertMonthsToNumbers(date);
                        author = cleanAuthor(author);
                        System.out.println(author);
                        System.out.println("");

                        //if text lenght is lower than 200 we remove it
                        if (author.trim().compareTo("") == 0 || text.length() < 200) {
                            //author was removed with regex and it is empty
                        } else {

                            //save cleaned data to new directory
                            String cleanPath = p + "results_cleaned/" + listOfFile.getName();
                            new File(p + "results_cleaned/").mkdirs();
                            WriteXMLFile wxmlf = new WriteXMLFile();
                            wxmlf.createXmlFile(author.trim(), link.trim(), title.trim(), date.trim(), text.trim(), cleanPath);

                        }
                    }

                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ResultsCleaner r = new ResultsCleaner();

    }
}
