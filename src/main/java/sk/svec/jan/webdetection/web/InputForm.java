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
package sk.svec.jan.webdetection.web;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.UrlValidator;

/**
 *
 * @author Ján Švec
 */
public class InputForm {

    private String link1;
    private String link2;
    private String link3;
    private String disc;

    public static InputForm extractFromRequest(HttpServletRequest request) {
        InputForm inputForm = new InputForm();
        inputForm.setLink1(request.getParameter("link1"));
        inputForm.setLink2(request.getParameter("link2"));
        inputForm.setLink3(request.getParameter("link3"));
        inputForm.setDisc(request.getParameter("disc"));
        return inputForm;
    }

    private static String validateValue(String value, String fieldName, StringBuilder errors) {
        UrlValidator urlValidator = new UrlValidator();
        if (!value.startsWith("http://")) {
            value = "http://" + value;
        }
        boolean valid = urlValidator.isValid(value);
        if (value == null || value.isEmpty()) {
            errors.append("Zadajte prosím url. <br />");
        } else if (!valid) {
            errors.append("Url ").append(value).append(" je neplatné. Zadajte prosím platné url. <br />");
        }

        return value;
    }

    public boolean isDisc() {
        if (disc == null) {
            return false;
        }
        return disc.compareTo("true") == 0;
    }

    public List<String> validateAndToSeeds(StringBuilder errors) {
        List<String> seeds = new ArrayList<String>();

        seeds.add(validateValue(getLink1(), "link1", errors));
//        seeds.add(validateValue(getLink2(), "link2", errors));
//        seeds.add(validateValue(getLink3(), "link3", errors));

        if (errors.length() > 0) {
            return null;
        } else {
            return seeds;
        }
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public String getLink3() {
        return link3;
    }

    public void setLink3(String link3) {
        this.link3 = link3;
    }

}
