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

/**
 *
 * @author Ján Švec
 */
public class StyleNode {

    private int hashCode;
    private int numberOfPages; // dalsie data
    private ElementNode parent;
    private List<ElementNode> elementNodes; // toto niesu potomkovia, iba "obsah", data
    private String elementNames; //nazvy vsetkych elementov spolu napr p img p , pimgp
    private StringBuilder sb;

    public StyleNode(int numberOfPages, List<ElementNode> elementNodes, ElementNode parent) {
        this.parent = parent;
        this.numberOfPages = numberOfPages;
        this.elementNodes = new ArrayList<ElementNode>();
        this.sb = new StringBuilder();
        this.hashCode = this.hashCode();
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public ElementNode getParent() {
        return parent;
    }

    public void setParent(ElementNode parent) {
        this.parent = parent;
    }

    public void addElementNode(ElementNode elementNode) {
        elementNode.setSiblingIndex(this.elementNodes.size());
        this.elementNodes.add(elementNode);

        this.sb.append(elementNode.getNode().nodeName());
        this.sb.append(" ");
        this.elementNames = sb.toString();
    }

//    public ElementNode nextSibling(ElementNode elementNode) {
//        if (parent == null) {
//            return null; // root
//        }
//        List<ElementNode> siblings = this.elementNodes;
//        int index = elementNode.getSiblingIndex();
//        if (siblings.size() > index + 1) {
//            return siblings.get(index + 1);
//        } else {
//            return null;
//        }
//
//    }
    public String getElementNames() {
        return elementNames;
    }

    public void incNumberOfPages() {
        numberOfPages++;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public List<ElementNode> getElementNodes() {
        return elementNodes;
    }

    @Override
    public String toString() {
        return "\nStyleNode{" + "numberOfPages=" + numberOfPages + ", elementNodes=" + elementNodes + '}';
    }
}
