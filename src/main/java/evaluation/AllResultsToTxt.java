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
package sk.svec.jan.acb.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import sk.svec.jan.acb.utility.TravelDirectory;

/**
 *
 * @author Ján Švec
 */
public class AllResultsToTxt {

    public static void main(String[] args) throws Exception {
        int poc = 1;

        TravelDirectory travelDirectory = new TravelDirectory("output/");
        List<String> directories = travelDirectory.getDirectories();
        for (String p : directories) {
            StringBuilder sb = new StringBuilder();

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

                    String autor = elementsByTagName.item(0).getTextContent();
                    String link = childNodes.item(0).getTextContent();
                    String nadpis = childNodes.item(1).getTextContent();
                    String datum = childNodes.item(2).getTextContent();
                    String text = childNodes.item(3).getTextContent();

                    sb.append(autor + "\t" + link + "\t" + nadpis + "\t" + datum + "\t" + text);
                    sb.append("\n");
                }

                PrintWriter out;
                out = new PrintWriter("output/spolu" + poc + ".txt");
                out.println(sb.toString());
                out.close();
                poc++;
            }
        }
    }
}
