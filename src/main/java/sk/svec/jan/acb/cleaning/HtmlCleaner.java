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
package sk.svec.jan.acb.cleaning;

import java.io.File;
import java.io.PrintWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

/**
 *
 * @author Ján Švec
 */
public class HtmlCleaner {

    public HtmlCleaner(String path) throws Exception {

        File folder = new File(path + "downloaded/");
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {

                File input = new File(path + "downloaded/" + listOfFile.getName());

                Document doc = Jsoup.parse(input, "UTF-8");

                removeComments(doc);
                removeEmptyText(doc);
                for (Element element : doc.select("script")) {
                    element.remove();
                }
                for (Element element : doc.select("noscript")) {
                    element.remove();
                }
                for (Element element : doc.select("style")) {
                    element.remove();
                }
                for (Element element : doc.select("img")) {
                    element.remove();
                }
                //remove invisible tags to user
                for (Element element : doc.select("[style=display:none]")) {
                    element.remove();
                }
                for (Element element : doc.select("[style=visibility:hidden]")) {
                    element.remove();
                }
                for (Element element : doc.select("[class=hidden]")) {
                    element.remove();
                }
                for (Element element : doc.select("[type=hidden]")) {
                    element.remove();
                }
                //remove empty tags
                for (Element element : doc.select("*")) {
                    // if (!element.hasText() && element.isBlock()) {
                    if (!element.hasText()) {
                        element.remove();
                    }
                }

//                // funkcia unwrap odstrani tag ale zanecha jeho obsah
//                for (Element element : doc.select("span")) {
//                    element.unwrap();
//                }
                //cistenie pomocou whitelistu
                // doc = cleanToWhitelist(doc);
                //use OutputSettings prettyPrint to false, to prevent creating empty text elements
                 /*example:
                 from:
                 <arial>
                 TEXT
                 </arial>
        
                 to:
                 <arial>TEXT</arial>
                 */
                final OutputSettings settings = new OutputSettings();
                settings.prettyPrint(false);
                doc.outputSettings(settings);

                //Using EscapeMode.xhtml will give you output without entities.
                doc.outputSettings().escapeMode(EscapeMode.xhtml);

                PrintWriter out;
                out = new PrintWriter(path + "cleaned/" + listOfFile.getName(), "UTF-8");
                System.out.println("page " + path + "cleaned/" + listOfFile.getName() + " was cleaned");
                out.println(doc.html());
                out.close();
            }
        }

    }

    /**
     * Sanitizes documents using custom whitelist
     *
     * @param doc
     * @return Cleaned document according to custom whitelist.
     */
    //whitelist necha text a odstrani TAG, odstranenie tagu s textom jedine cez remove
    private Document cleanToWhitelist(Document doc) {
        //vyrobit whitelist, ktory obsahuje len pozadovane tagy
        Whitelist myWhitelist = new Whitelist();
        myWhitelist.addTags("div", "p");
        myWhitelist.addAttributes("div", "class");
        myWhitelist.addAttributes("div", "id");
        myWhitelist.addAttributes("p", "class");
        myWhitelist.addAttributes("p", "id");

        Cleaner myCleaner = new Cleaner(myWhitelist);
//        Cleaner myCleaner = new Cleaner(Whitelist.relaxed());
        return myCleaner.clean(doc);
    }

    private void removeComments(Node node) {
        int i = 0;
        while (i < node.childNodes().size()) {
            Node child = node.childNode(i);
            if (child.nodeName().equals("#comment")) {
                child.remove();
            } else {
                removeComments(child);
                i++;
            }
        }
    }

    private void removeEmptyText(Node node) {
        int i = 0;
        while (i < node.childNodes().size()) {
            Node child = node.childNode(i);
            if (child.nodeName().equals("#text") && (child.outerHtml().length() == 1)) {
                child.remove();
            } else {
                removeEmptyText(child);
                i++;
            }
        }
    }
}
