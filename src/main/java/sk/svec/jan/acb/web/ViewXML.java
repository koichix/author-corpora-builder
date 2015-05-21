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
package sk.svec.jan.acb.web;

import java.io.File;
import java.io.FileInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Ján Švec
 */
public class ViewXML {

    public String getText(String path) throws Exception {

        File xmlFile = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(new FileInputStream(xmlFile));

        Element rootElement = doc.getDocumentElement();

        NodeList allArticles = rootElement.getElementsByTagName("Article");
        NodeList childNodes = allArticles.item(0).getChildNodes();
        String text = childNodes.item(3).getTextContent();

        return text;

    }

    public String getXMLcontent(String path) throws Exception {
        StringBuilder xml = new StringBuilder();
        String link = new String();
        String nadpis = new String();
        String datum = new String();
        String text = new String();

        File xmlFile = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(new FileInputStream(xmlFile));

        Element rootElement = doc.getDocumentElement();
        NodeList elementsByTagName = rootElement.getElementsByTagName("Name");

        String autor = elementsByTagName.item(0).getTextContent();
        xml.append("Autor: " + autor);

        NodeList allArticles = rootElement.getElementsByTagName("Article");
        int articlesCount = allArticles.getLength();

        for (int i = 0; i < articlesCount; i++) {
            NodeList childNodes = allArticles.item(i).getChildNodes();
            link = childNodes.item(0).getTextContent();
            nadpis = childNodes.item(1).getTextContent();
            datum = childNodes.item(2).getTextContent();
            text = childNodes.item(3).getTextContent();
            xml.append("<br/><br/>Článok číslo:" + (i + 1) + "<br/>");
            xml.append("Link: <a href=\"" + link + "\" target=\"_blank\">" + link + "</a><br/>"
                    + "Nadpis: " + nadpis + "<br/>"
                    + "Dátum: " + datum + "<br/>"
                    + "Text: " + text);
        }
//        
//        NodeList childNodes = rootElement.getElementsByTagName("Article").item(0).getChildNodes();
//
//        String link = childNodes.item(0).getTextContent();
//        String nadpis = childNodes.item(1).getTextContent();
//        String datum = childNodes.item(2).getTextContent();
//        String text = childNodes.item(3).getTextContent();

        return (xml.toString());

    }

//    public static void main(String[] args) throws Exception {
//        ViewXML test = new ViewXML();
//        System.out.println(test.getXMLcontent("corpus/a.xml"));
//
//    }
}
