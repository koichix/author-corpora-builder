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
package sk.svec.jan.acb.site_style_tree;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import org.jsoup.nodes.Node;
import sk.svec.jan.acb.main.WebDetectionMain;

/**
 *
 * @author Ján Švec
 */
public class ElementNode {

    private int hashCode;
    private StyleNode parent; //nadradeny style node
    private Node node; //data
    private List<StyleNode> styleNodes; //potomkovia
    private int siblingIndex;

    private int m;
    private int l;
    private double p[];
    private double nodeImp;
    private boolean noisy;

//        private int hash;
    public ElementNode(Node node, List<StyleNode> styleNodes, StyleNode parent) {
        this.node = node;
//            this.hash = node.nodeName().hashCode();
        this.styleNodes = new ArrayList<StyleNode>();
        this.parent = parent;
        this.hashCode = this.hashCode();
    }

    private double logOfBase(int base, double num) {
        return Math.log(num) / Math.log(base);
    }

    public double countNodeImp() {
        m = this.getStyleNode().getNumberOfPages();
        l = this.styleNodes.size();
        p = new double[l];
        for (int i = 0; i < l; i++) {
            int snNumP = this.styleNodes.get(i).getNumberOfPages(); //pocet stranok konkretneho sn
            p[i] = (double) snNumP / m;
        }

        if (m > 1) {
            for (int i = 0; i < l; i++) {
                double n = -(p[i] * logOfBase(m, p[i]));
                nodeImp += n;
            }

        } else if (m == 1) {
            nodeImp = 1;
        }

        if (!WebDetectionMain.diskusia) {
            //priradenie priority explicitne
            //datum    
            Pattern datePattern = Pattern.compile("date|datum|dátum", CASE_INSENSITIVE);
            Matcher dateMatcher = datePattern.matcher(this.node.attr("class"));
            while (dateMatcher.find()) {
                nodeImp = 0.99;
            }

            //autor
            Pattern authorPattern = Pattern.compile("autor|autori|autoři|author|authors|name|user|nick|profil", CASE_INSENSITIVE);
            Matcher authorMatcher = authorPattern.matcher(this.node.attr("class"));
            while (authorMatcher.find()) {
                nodeImp = 0.99;
            }

            //nadpis
            if (this.node.nodeName().compareTo("h1") == 0) {
                nodeImp = 0.99;
            }

            if (this.node.nodeName().compareTo("title") == 0) {
                nodeImp = 0.99;
            }
        }
        return nodeImp;
    }

    public int getHashCode() {
        return hashCode;
    }

    public StyleNode getStyleNode() {
        return parent;
    }

    public double getNodeImp() {
        return nodeImp;
    }

    public void setNoisy(boolean noisy) {
        this.noisy = noisy;
    }

    public boolean isNoisy() {
        return noisy;
    }

    public ElementNode nextSibling() {
        if (parent == null) {
            return null; // root
        }

        List<ElementNode> siblings = parent.getElementNodes();
        int index = this.siblingIndex;
        if (siblings.size() > index + 1) {
            return siblings.get(index + 1);
        } else {
            return null;
        }

    }

    public void addStyleNode(StyleNode styleNode) {
        this.styleNodes.add(styleNode);
    }

    public Node getNode() {
        return node;
    }

    public List<StyleNode> getStyleNodes() {
        return styleNodes;
    }

    public int styleNodesSize() {
        return styleNodes.size();
    }

    public int getSiblingIndex() {
        return siblingIndex;
    }

    public void setSiblingIndex(int siblingIndex) {
        this.siblingIndex = siblingIndex;
    }

//    @Override
//    public String toString() {
////        if (styleNodes.size() > 0) {
////            return "ElementNode{" + "node=" + node.nodeName() + ", styleNodes=" + styleNodes + '}';
////        }
////        return "ElementNode{" + "node=" + node.nodeName() + '}';
//        return "ElementNode{" + "node=" + node.nodeName() + ", styleNodes=" + styleNodes + '}';
//    }
    @Override
    public String toString() {
        return "ElementNode{" + "node=" + node.nodeName() + ", styleNodes=" + styleNodes + ", nodeImp=" + nodeImp + ", noisy=" + noisy + '}';
    }

}
