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

import java.io.File;
import java.io.FileInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Ján Švec
 */
public class WriteXMLFile {

    public void createXmlFile(String name, String link, String title, String date, String text, String path) throws Exception {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("Author");
        doc.appendChild(rootElement);

        Element nam = doc.createElement("Name");
        nam.appendChild(doc.createTextNode(name));
        rootElement.appendChild(nam);

        Element article = doc.createElement("Article");
        rootElement.appendChild(article);

        Attr attr = doc.createAttribute("id");
        attr.setValue("1");
        article.setAttributeNode(attr);

        Element lin = doc.createElement("Link");
        lin.appendChild(doc.createTextNode(link));
        article.appendChild(lin);

        Element titl = doc.createElement("Title");
        titl.appendChild(doc.createTextNode(title));
        article.appendChild(titl);

        Element dat = doc.createElement("Date");
        dat.appendChild(doc.createTextNode(date));
        article.appendChild(dat);

        Element tex = doc.createElement("Text");
        tex.appendChild(doc.createTextNode(text.replaceAll("\\p{Cntrl}", "")));
        article.appendChild(tex);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(path));

        // Output to console for testing
//		 StreamResult result = new StreamResult(System.out);
        transformer.transform(source, result);

        System.out.println("File saved!\n");

    }

    public void xmlData() {

    }

    public void addToXmlFile(String link, String title, String date, String text, String path) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(new FileInputStream(new File(path)));

        int length = doc.getElementsByTagName("Article").getLength();
        int id = length + 1;

        Element rootElement = doc.getDocumentElement();

        Element article = doc.createElement("Article");
        rootElement.appendChild(article);

        Attr attr = doc.createAttribute("id");
        attr.setValue("" + (id));
        article.setAttributeNode(attr);

        Element lin = doc.createElement("Link");
        lin.appendChild(doc.createTextNode(link));

        Element titl = doc.createElement("Title");
        titl.appendChild(doc.createTextNode(title));

        Element dat = doc.createElement("Date");
        dat.appendChild(doc.createTextNode(date));

        Element tex = doc.createElement("Text");
        tex.appendChild(doc.createTextNode(text.replaceAll("\\p{Cntrl}", "")));
        article.appendChild(lin);
        article.appendChild(titl);
        article.appendChild(dat);
        article.appendChild(tex);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(path));

        // Output to console for testing
//		 StreamResult result = new StreamResult(System.out);
        transformer.transform(source, result);

        System.out.println("File updated!");
    }

   
}
