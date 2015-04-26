/*
 * Copyright 2015 JĂˇn Ĺ vec
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
package sk.svec.jan.webdetection.site_style_tree;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Node;
import sk.svec.jan.webdetection.main.WebDetectionMain;
import sk.svec.jan.webdetection.utility.FourReturn;

/**
 *
 * @author Ján Švec
 */
public class SiteStyleTree {

    private int allPagesCount;
    private Node rootNode;
    private static final double THRESHOLD = Double.parseDouble(WebDetectionMain.getResourceBundle().getString("Threshold")); //prah

    public SiteStyleTree(String path) throws Exception {
        this.allPagesCount = 1;

        StyleNode bssFinal = null;
        File folder = new File(path + "cleaned/");
        File[] listOfFiles = folder.listFiles();
        //vybudujeme sst
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {

                if (listOfFile == listOfFiles[0]) { //prve
                    buildStyleTree(path + "cleaned/" + listOfFile.getName());
                    StyleNode styleNodeA = getRootSn();
                    setStyleNodeX(styleNodeA);

                } else if (listOfFile == listOfFiles[listOfFiles.length - 1]) { //posledne
                    bssFinal = buildSiteStyleTree(path + "cleaned/" + listOfFile.getName());
                } else { //secky ostatne
                    buildSiteStyleTree(path + "cleaned/" + listOfFile.getName());
                }

            }
        }

        //vypocitame premenne
        countNodeImpInStyleTree(bssFinal);
        markNoise(bssFinal);

        //extrahujeme a ulozime
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                extractPage(bssFinal, path + "cleaned/" + listOfFile.getName());
            }
        }
    }

    public int getAllPagesCount() {
        return allPagesCount;
    }

    /**
     * Start a depth-first traverse of the root and all of its descendants.
     *
     * @param root the root node point to traverse.
     */
    public void dfsNode(Node root) {
        Node node = root;
        int depth = 0;

        while (node != null) {
            System.out.println(depth + " " + node.nodeName() + " " + node.childNodeSize());
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

    private int level = 0;

    public void traverseStyleTree(StyleNode root) {
        // System.out.println(level + " " + root.getNumberOfPages() + " " + root.getElementNames());
        for (ElementNode en : root.getElementNodes()) {
            //tu ziskam konkretny Node, ktory mozem porovnavat s dom stromom
            //zislo by sa nejak zapisovat hlbku
//           System.out.println(en.getNode().nodeName() + " " + en.getSiblingIndex());
            //System.out.println(en.getNode().nodeName() + " " + en.getNode());
//            if (en.getNodeImp() != 0.0 && en.getNodeImp() != 1.0) {
//               
//                System.out.println(en.getNodeImp());
//            }

            System.out.println(en.getNode().nodeName());
            if (!en.getStyleNodes().isEmpty()) {
                for (StyleNode sn : en.getStyleNodes()) {
                    level++;
                    traverseStyleTree(sn);
                }
            }
        }
    }

    public void countNodeImpInStyleTree(StyleNode root) {
        for (ElementNode en : root.getElementNodes()) {
            en.countNodeImp();
            if (!en.getStyleNodes().isEmpty()) {
                for (StyleNode sn : en.getStyleNodes()) {
                    level++;
                    countNodeImpInStyleTree(sn);
                }
            }
        }
    }

    public void markNoise(StyleNode root) {
        for (ElementNode en : root.getElementNodes()) {
            if (en.getNodeImp() <= THRESHOLD) {
                en.setNoisy(true);
            }

            if (!en.getStyleNodes().isEmpty()) {
                for (StyleNode sn : en.getStyleNodes()) {
                    level++;
                    markNoise(sn);
                }
            }
        }
    }

    private StyleNode rootSn;
    private StyleNode tempSn;

    public void buildStyleTree(String path) throws Exception {
        File input = new File(path);
//        File input = new File("../1test/output/cleanedPages/" + num + ".html");
        Document doc = Jsoup.parse(input, "UTF-8");

        final Document.OutputSettings settings = new Document.OutputSettings();
        settings.prettyPrint(false);
        doc.outputSettings(settings);

        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        Queue<StyleNode> fifo = new LinkedList<StyleNode>();
        Node node = doc;

        rootSn = new StyleNode(1, null, null);
        ElementNode en = new ElementNode(node, null, rootSn);
        rootSn.addElementNode(en);

        tempSn = new StyleNode(1, null, en);
        en.addStyleNode(tempSn);

        fifo.add(tempSn); //vlozime tempsn do fronty
        while (!fifo.isEmpty()) {
            tempSn = fifo.remove();
            for (Node n : tempSn.getParent().getNode().childNodes()) {
                tempSn.addElementNode(new ElementNode(n, null, tempSn));
            }
            for (ElementNode e : tempSn.getElementNodes()) {
                if (e.getNode().childNodeSize() > 0) {
                    StyleNode newStyleNode = new StyleNode(1, null, e);
                    e.addStyleNode(newStyleNode);
                    fifo.add(newStyleNode);
                }
            }
        }

        rootNode = doc;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public StyleNode getRootSn() {
        return rootSn;
    }

    public FourReturn<ElementNode, ElementNode, StyleNode, StyleNode> travelRightAndUp(ElementNode elementNodeA, ElementNode elementNodeB, StyleNode styleNodeA, StyleNode styleNodeB) {
        while (elementNodeA.styleNodesSize() == 0 && elementNodeB.styleNodesSize() == 0) {
            if (elementNodeA.nextSibling() == null && elementNodeB.nextSibling() == null) {
//                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");
//                    System.out.println(elementNodeA.getNode().nodeName() + " " + elementNodeA.getNode().outerHtml());

                //vystupi o jedno vyssie a vyberie vedlajsie
                elementNodeA = styleNodeA.getParent().nextSibling();
                elementNodeB = styleNodeB.getParent().nextSibling();

                if (elementNodeA == null) {

                    ElementNode fixedA = styleNodeA.getParent();
                    ElementNode fixedB = styleNodeB.getParent();

                    //ak nemal surodencov, tak pomocou cyklu ideme vyssie a doprava, kolko treba
                    while (elementNodeA == null) {
                        ElementNode eNhelpA = fixedA.getStyleNode().getParent();
                        ElementNode eNhelpB = fixedB.getStyleNode().getParent();
                        fixedA = eNhelpA;
                        fixedB = eNhelpB;

                        //dorazili sme na koniec suboru
                        if (eNhelpA == null) {
                            break;
                        }

                        elementNodeA = eNhelpA.nextSibling();
                        elementNodeB = eNhelpB.nextSibling();

//                        if (elementNodeA != null && elementNodeA.getNode().nodeName().compareTo("p") == 0) {
//                            System.out.println(elementNodeA.getNode().nodeName() + " " + elementNodeA.getHashCode());
//                            System.out.println(styleNodeA);
//                        }
                    }

                    if (elementNodeA == null) {
                        break;
                    }
                    styleNodeA = elementNodeA.getStyleNode();
                    styleNodeB = elementNodeB.getStyleNode();

                }
                styleNodeA = elementNodeA.getStyleNode();
                styleNodeB = elementNodeB.getStyleNode();

//                 System.out.println(elementNodeA.getNode().nodeName() + " " + elementNodeA.getNode().outerHtml());
//                  System.out.println(elementNodeB.getNode().nodeName() + " " + elementNodeB.getNode().outerHtml());
            } else {
                elementNodeA = elementNodeA.nextSibling();
                elementNodeB = elementNodeB.nextSibling();

            }

        }
        FourReturn<ElementNode, ElementNode, StyleNode, StyleNode> returnValue = new FourReturn(elementNodeA, elementNodeB, styleNodeA, styleNodeB);
        return returnValue;
    }

    private StyleNode styleNodeX;

    public void setStyleNodeX(StyleNode styleNodeX) {
        this.styleNodeX = styleNodeX;
    }

//    boolean opravujem = false;
    public StyleNode buildSiteStyleTree(String path) throws Exception {
        StyleNode styleNodeA = styleNodeX;
        buildStyleTree(path);
        StyleNode styleNodeB = getRootSn();

        ElementNode elementNodeA = styleNodeA.getElementNodes().get(0);
        ElementNode elementNodeB = styleNodeB.getElementNodes().get(0);

        while (elementNodeA != null) {

            FourReturn<ElementNode, ElementNode, StyleNode, StyleNode> travelRightAndUp;
            travelRightAndUp = travelRightAndUp(elementNodeA, elementNodeB, styleNodeA, styleNodeB);
            elementNodeA = travelRightAndUp.getFirst();
            elementNodeB = travelRightAndUp.getSecond();
            styleNodeA = travelRightAndUp.getThird();
            styleNodeB = travelRightAndUp.getFourth();

            if (elementNodeA == null || elementNodeB == null) {
                break;
            }

            boolean found = false;
            int styleNodeIndex = 0;

            for (int j = 0; j < elementNodeA.styleNodesSize(); j++) {
                StyleNode childSnA = elementNodeA.getStyleNodes().get(j);
                StyleNode childSnB = elementNodeB.getStyleNodes().get(0);

                if (childSnA.getElementNames().compareTo(childSnB.getElementNames()) == 0) {
                    //childSnA.incNumberOfPages();                    
                    found = true;
                    styleNodeIndex = j;
                    break;
                }
            }

            if (found) {

                StyleNode childSnA = elementNodeA.getStyleNodes().get(styleNodeIndex);
                StyleNode childSnB = elementNodeB.getStyleNodes().get(0);

                childSnA.incNumberOfPages();

                //zideme o uroven nizsie v oboch stromoch
                styleNodeA = childSnA;
                styleNodeB = childSnB;

                elementNodeA = styleNodeA.getElementNodes().get(0);
                elementNodeB = styleNodeB.getElementNodes().get(0);

            } else {
                StyleNode childSnA = elementNodeA.getStyleNodes().get(0);
                StyleNode childSnB = elementNodeB.getStyleNodes().get(0);

                //nastavime spravneho parenta
                childSnB.setParent(childSnA.getParent());
                //zlucime podstromy
                childSnA.getParent().addStyleNode(childSnB);

                //chod o 1 doprava, ak sa neda tak hore
                elementNodeA = childSnA.getParent().nextSibling();
                elementNodeB = childSnB.getParent().nextSibling();

                //ak sa neda tak hore je cele toto if
                if (elementNodeA == null) {

                    ElementNode fixedA = styleNodeA.getParent();
                    ElementNode fixedB = styleNodeB.getParent();

                    //v pripade ze zlucujeme dva odlisne subory, ktore sa odlisuju uz v prvom prvku
                    //tak netreba cestovat hore, ale rovno skoncit (napr jeden sa zacina document a druhy doctype)
                    if (fixedA == null) {
                        break;
                    }
                    //ak nemal surodencov, tak pomocou cyklu ideme vyssie a doprava, kolko treba
                    while (elementNodeA == null) {

                        ElementNode eNhelpA = fixedA.getStyleNode().getParent();
                        ElementNode eNhelpB = fixedB.getStyleNode().getParent();
                        fixedA = eNhelpA;
                        fixedB = eNhelpB;

                        //dorazili sme na koniec suboru
                        if (eNhelpA == null) {
                            break;
                        }

                        elementNodeA = eNhelpA.nextSibling();
                        elementNodeB = eNhelpB.nextSibling();
                    }

                    if (elementNodeA == null) {
                        break;
                    }
//                    styleNodeA = elementNodeA.getStyleNode();
//                    styleNodeB = elementNodeB.getStyleNode();

                }

                styleNodeA = elementNodeA.getStyleNode();
                styleNodeB = elementNodeB.getStyleNode();

            }

        }
        allPagesCount++;
        return styleNodeX;
    }

    public void extractPage(StyleNode sst, String path) throws Exception {
        StyleNode styleNodeA = sst;
        buildStyleTree(path);
        StyleNode styleNodeB = getRootSn();

        StringBuilder sb = new StringBuilder();

        ElementNode elementNodeA = styleNodeA.getElementNodes().get(0);
        ElementNode elementNodeB = styleNodeB.getElementNodes().get(0);

        while (elementNodeA != null) {

            FourReturn<ElementNode, ElementNode, StyleNode, StyleNode> travelRightAndUp;
            travelRightAndUp = travelRightAndUp(elementNodeA, elementNodeB, styleNodeA, styleNodeB);
            elementNodeA = travelRightAndUp.getFirst();
            elementNodeB = travelRightAndUp.getSecond();
            styleNodeA = travelRightAndUp.getThird();
            styleNodeB = travelRightAndUp.getFourth();

            if (elementNodeA == null || elementNodeB == null) {
                break;
            }

            boolean found = false;
            int styleNodeIndex = 0;

            for (int j = 0; j < elementNodeA.styleNodesSize(); j++) {
                StyleNode childSnA = elementNodeA.getStyleNodes().get(j);
                StyleNode childSnB = elementNodeB.getStyleNodes().get(0);

                if (childSnA.getElementNames().compareTo(childSnB.getElementNames()) == 0) {
                    found = true;
                    styleNodeIndex = j;
                    break;
                }
            }

            if (found) {

                StyleNode childSnA = elementNodeA.getStyleNodes().get(styleNodeIndex);
                StyleNode childSnB = elementNodeB.getStyleNodes().get(0);

                styleNodeA = childSnA;
                styleNodeB = childSnB;

                if (!styleNodeA.getElementNodes().isEmpty()) {
                    for (int i = 0; i < styleNodeA.getElementNodes().size(); i++) {
                        ElementNode enA = styleNodeA.getElementNodes().get(i);
                        ElementNode enB = styleNodeB.getElementNodes().get(i);
                        //1.0 vynechavam preto, ze je to uplne unikatne a to ma nezaujima
                        if (!enA.isNoisy() && enA.getNodeImp() != 1.0 && !enB.isNoisy()) {
//                            System.out.println(enA.getNodeImp() + " " + enB.getNode());
//                            System.out.println(enB.getNode());
                            sb.append(enB.getNode());
                            setAllBelowNoisy(enB); //veci pod nim by nam robili duplikaty - oznacime ich noisy a tym vynechame
                        }

                        //v pripade ze to chcem otestovat bez tresholdu(vsetko vacsie ako 0)
//                        if (enA.getNodeImp() > 0) {
//                            System.out.println(enA.getNodeImp()+" "+enB.getNode().nodeName());
//                        }
                        // System.out.println(enB.getNode().nodeName());
                    }

                }

                elementNodeA = styleNodeA.getElementNodes().get(0);
                elementNodeB = styleNodeB.getElementNodes().get(0);

            } else {
                break;
            }

        }

        String extractedPage = sb.toString();
        PrintWriter out;
        String outputPath = path.replace("cleaned", "extracted");
        out = new PrintWriter(outputPath, "UTF-8");
        out.println(extractedPage);
        out.close();
        System.out.println("page " + outputPath + " was extracted succesfully");
    }

    private void setAllBelowNoisy(ElementNode enx) {
        //prejst strom do hlbky, nastavit vsetky prvky na noisy - pretoze ked uz mame raz obsah elementu, to pod nim nepotrebujeme
        StyleNode root = enx.getStyleNode();
        for (ElementNode en : root.getElementNodes()) {
            en.setNoisy(true);
            if (!en.getStyleNodes().isEmpty()) {
                for (StyleNode sn : en.getStyleNodes()) {
                    level++;
                    countNodeImpInStyleTree(sn);
                }
            }
        }
    }

    public String example() throws Exception {
        buildStyleTree("c:/Users/Koichi/Documents/NetBeansProjects/1test/18.html");
        StyleNode styleNodeA = getRootSn();
        setStyleNodeX(styleNodeA);
        buildSiteStyleTree("c:/Users/Koichi/Documents/NetBeansProjects/1test/18.html");
        buildSiteStyleTree("c:/Users/Koichi/Documents/NetBeansProjects/1test/18.html");
        buildSiteStyleTree("c:/Users/Koichi/Documents/NetBeansProjects/1test/18.html");
        buildSiteStyleTree("c:/Users/Koichi/Documents/NetBeansProjects/1test/18.html");
        buildSiteStyleTree("c:/Users/Koichi/Documents/NetBeansProjects/1test/20.html");
        buildSiteStyleTree("c:/Users/Koichi/Documents/NetBeansProjects/1test/20.html");
        buildSiteStyleTree("c:/Users/Koichi/Documents/NetBeansProjects/1test/20.html");
        StyleNode bssFinal = buildSiteStyleTree("c:/Users/Koichi/Documents/NetBeansProjects/1test/20.html");

        return bssFinal.toString();
    }

//    public static void main(String[] args) throws Exception {
//        String p = "output/sk/cas/";
//        SiteStyleTree siteStyleTree = new SiteStyleTree(p);
//    }
}
