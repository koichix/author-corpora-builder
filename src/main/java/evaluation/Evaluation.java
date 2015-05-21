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
package evaluation;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import sk.svec.jan.acb.extraction.WriteXMLFile;
import sk.svec.jan.acb.utility.FourReturn;
import sk.svec.jan.acb.web.ViewXML;
import us.codecraft.xsoup.Xsoup;

/**
 *
 * @author Ján Švec
 */
public class Evaluation {

    public void extractValues(String path) throws Exception {

        File folder = new File(path + "downloaded/");
        File[] listOfFiles = folder.listFiles();

        //nacitanie Xpath zo suboru (treba ho umiestnit do priecinku a pomenovat annotation.txt
        FourReturn<String, String, String, String> readDeclaration = readDeclaration(path + "anotation.txt");
        String authorXpath = readDeclaration.getFirst();
        String titleXpath = readDeclaration.getSecond();
        String dateXpath = readDeclaration.getThird();
        String textXpath = readDeclaration.getFourth();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {

                File input = new File(path + "downloaded/" + listOfFile.getName());

                Document document = Jsoup.parse(input, "UTF-8");

                //extract author, title, date, text from document
                //jeden autor
                //String author = Xsoup.compile("//div[@class=\"author\"]/span/text()").evaluate(document).get();
                //viac autorov
                List<Element> element0 = Xsoup.compile(authorXpath.trim()).evaluate(document).getElements();
                StringBuilder sb = new StringBuilder();
                for (Element item : element0) {
                    sb.append(item.text());
                    sb.append(" ");
                }
                String author = sb.toString();
                String title = Xsoup.compile(titleXpath.trim()).evaluate(document).get();
                String date = Xsoup.compile(dateXpath.trim()).evaluate(document).get();
                List<Element> element = Xsoup.compile(textXpath.trim()).evaluate(document).getElements();
                StringBuilder sb2 = new StringBuilder();
                for (Element item : element) {
                    sb2.append(item.text());
                    sb2.append(" ");
                }
                String text = sb2.toString();

//                //testovaci vypis       
//                System.out.println("AUTOR:"+author.trim());
//                System.out.println("TITLE:"+title.trim());
//                System.out.println("DATE:"+date.trim());
//                System.out.println("TEXT:"+text.trim());
                if (author == null) {
                    author = "";
                } else if (title == null) {
                    title = "";
                } else if (date == null) {
                    date = "";
                } else if (text == null) {
                    text = "";
                }

                //nastavime si nazov suboru, podla mena autora
                String linkPath = path + "links/" + listOfFile.getName().substring(0, listOfFile.getName().indexOf(".")) + ".link";

                //jednoriadkove nacitanie obsahu suboru
                String link = new Scanner(new File(linkPath)).useDelimiter("\\A").next();

                //vytvorenie priecinku "manual" - manualne anotovane 
                new File(path + "manual/").mkdirs();

                WriteXMLFile manualAnotatedXML = new WriteXMLFile();
                manualAnotatedXML.createXmlFile(author.trim(), link.trim(), title.trim(), date.trim(), text.trim(), path + "manual/" + listOfFile.getName().replace("html", "xml"));

            }
        }

    }

    public void xpathSelectorsOneFile(String path) throws Exception {

        FourReturn<String, String, String, String> readDeclaration = readDeclaration("test/cas/anotation.txt");
        String authorXpath = readDeclaration.getFirst();
        String titleXpath = readDeclaration.getSecond();
        String dateXpath = readDeclaration.getThird();
        String textXpath = readDeclaration.getFourth();

        File input = new File(path);

        Document document = Jsoup.parse(input, "UTF-8");

//        //examples of Xsoup usage
//        //find just one item
//        String result = Xsoup.compile("//a/@href").evaluate(document).get();
//        System.out.println(result);
//
//        //find all items
//        List<String> list = Xsoup.compile("//a/@href").evaluate(document).list();
//        for (String item : list) {
//            System.out.println(item);
//        }
//        
//        //find whole element
//        List<Element> element = Xsoup.compile("//a/@href").evaluate(document).getElements();
//        for (Element item : element) {
//            System.out.println(item);
//        }
//
        String autor = Xsoup.compile(authorXpath).evaluate(document).get();
        System.out.println(autor);

        String nadpis = Xsoup.compile(titleXpath).evaluate(document).get();
        System.out.println(nadpis);

        String datum = Xsoup.compile(dateXpath).evaluate(document).get();
        System.out.println(datum.trim());

//        String text = Xsoup.compile("//div[@class=\"article_wrap\"]").evaluate(document).get();
//        System.out.println(text);
//
        List<Element> element = Xsoup.compile(textXpath).evaluate(document).getElements();
//        for (Element item : element) {
//            System.out.println(item.text());
//        }
        String text = element.get(0).text();
        System.out.println(text);
        WriteXMLFile manualAnotatedXML = new WriteXMLFile();
        manualAnotatedXML.createXmlFile(autor.trim(), "link", nadpis.trim(), datum.trim(), text.trim(), "test/output.xml");
    }

    public static String getTagValue(String xml, String tagName) {
        return xml.split("<" + tagName + ">")[1].split("</" + tagName + ">")[0];
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public FourReturn<String, String, String, String> readDeclaration(String file) throws Exception {

        String input = readFile(file, Charset.defaultCharset());
        String authorXpath = getTagValue(input, "author_value");
        String titleXpath = getTagValue(input, "document_heading");
        String dateXpath = getTagValue(input, "version_time");
        String textXpath = getTagValue(input, "raw_text");
//
//        System.out.println(authorXpath.trim());
//        System.out.println(titleXpath.trim());
//        System.out.println(dateXpath.trim());
//        System.out.println(textXpath.trim());
        FourReturn<String, String, String, String> returnValue = new FourReturn(authorXpath.trim(), titleXpath.trim(), dateXpath.trim(), textXpath.trim());
        return returnValue;
    }

    public void compareResultsForText(String path) throws Exception {

        StringBuilder sb = new StringBuilder();
        File folder = new File(path + "results/");
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                ViewXML xml = new ViewXML();
                String result = xml.getText(path + "results/" + listOfFile.getName());
                String manual = xml.getText(path + "manual/" + listOfFile.getName());

//                StringSimilarity.printSimilarity(result, manual);
//                System.out.println(String.format("File with name: \"%s\" has similarity: %.3f", listOfFile.getName(), StringSimilarity.similarity(result, manual)));
                sb.append(String.format("File with name: \"%s\" has similarity: %.3f\n", listOfFile.getName(), StringSimilarity.similarity(result, manual)));

            }
        }
        System.out.println(sb.toString());
//        ViewXML xml = new ViewXML();
//        String result3 = xml.getText(path + "results/00ef1c115337f9822597da4e1b2d0f6b.xml");
//        String manual3 = xml.getText(path + "manual/00ef1c115337f9822597da4e1b2d0f6b.xml");
//
//        System.out.println(result3);
//        System.out.println();
//        System.out.println(manual3);
//
//        printSimilarity(result3, manual3);
//
//        String string1 = "Prokuratúra po troch týždňoch polícii: Hľadajte svedkov incidentu v električke! Martin (28) z bratislavskej Petržalky je podozrivý, že v pondelok 2. marca v električke číslo 4 na Americkom námestí v mestskej časti Staré Mesto napadol ženu (39). Mal ju biť do tváre. Po vystúpení z električky na zastávke konflikt pokračoval. Martin ženu ťahal za vlasy a strhol jej kabelku. Policajti, ktorí podozrivého zatkli krátko po čine, Martina obvinili z výtržnosti. V prípade dokázania viny na súde mu hrozí trest odňatia slobody až na tri roky - informoval bratislavský krajský policajný hovorca Michal Szeiff.  Polícia sa vo štvrtok 26. marca prostredníctvom médií obrátila na verejnosť so žiadosťou o nájdenie prípadných svedkov incidentu. Pokyn na hľadanie prípadných svedkov po troch týždňoch od incidentu dal dozorujúci prokurátor.  Martin (28) z bratislavskej Petržalky je podozrivý, že v pondelok 2. marca v električke číslo 4 na Americkom námestí v mestskej časti Staré Mesto napadol ženu (39). Mal ju biť do tváre. Po vystúpení z električky na zastávke konflikt pokračoval. Martin ženu ťahal za vlasy a strhol jej kabelku. Policajti, ktorí podozrivého zatkli krátko po čine, Martina obvinili z výtržnosti. V prípade dokázania viny na súde mu hrozí trest odňatia slobody až na tri roky - informoval bratislavský krajský policajný hovorca Michal Szeiff. ";
//        String string2 = "Martin (28) z bratislavskej Petržalky je podozrivý, že v pondelok 2. marca v električke číslo 4 na Americkom námestí v mestskej časti Staré Mesto napadol ženu (39). Mal ju biť do tváre. Po vystúpení z električky na zastávke konflikt pokračoval. Martin ženu ťahal za vlasy a strhol jej kabelku. Policajti, ktorí podozrivého zatkli krátko po čine, Martina obvinili z výtržnosti. V prípade dokázania viny na súde mu hrozí trest odňatia slobody až na tri roky - informoval bratislavský krajský policajný hovorca Michal Szeiff.  Polícia sa vo štvrtok 26. marca prostredníctvom médií obrátila na verejnosť so žiadosťou o nájdenie prípadných svedkov incidentu. Pokyn na hľadanie prípadných svedkov po troch týždňoch od incidentu dal dozorujúci prokurátor. ";
//        printSimilarity(string1, string2);
    }

    public static void main(String[] args) throws Exception {
        Evaluation test = new Evaluation();
        //test.xpathSelectorsOneFile("test/cas/downloaded/00ef1c115337f9822597da4e1b2d0f6b.html");   
        //test.extractValues("test/cas/");
        //test.extractValues("test/nova/tn/");        

        test.compareResultsForText("test/cas/");

    }
}
