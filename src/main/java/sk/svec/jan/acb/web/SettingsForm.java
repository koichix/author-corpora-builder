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

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.UrlValidator;
import sk.svec.jan.acb.utility.Setting;

/**
 *
 * @author Ján Švec
 */
public class SettingsForm {

    private String setting1;
    private String setting2;
    private String setting3;
    private String setting4;
    private String setting5;
    private String setting6;

    public static SettingsForm extractFromRequest(HttpServletRequest request) {
        SettingsForm settingsForm = new SettingsForm();
        settingsForm.setSetting1(request.getParameter("setting1"));
        settingsForm.setSetting2(request.getParameter("setting2"));
        settingsForm.setSetting3(request.getParameter("setting3"));
        settingsForm.setSetting4(request.getParameter("setting4"));
        settingsForm.setSetting5(request.getParameter("setting5"));
        settingsForm.setSetting6(request.getParameter("setting6"));
        return settingsForm;
    }

    private static String validateValue(String value, String fieldName, StringBuilder errors) {
        if (value == null || value.isEmpty()) {
            errors.append("Vyplňte prosím pole '").append(fieldName).append("'").append(". <br />");
        }

        return value;
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static String validateInt(String value, String fieldName, StringBuilder errors) {

        if (value == null || value.isEmpty()) {
            errors.append("Vyplňte prosím pole '").append(fieldName).append("'").append(". <br />");
        } else if (!isInteger(value)) {
            errors.append("Vložte prosím celé číslo do poľa '").append(fieldName).append("'").append(". <br />");
        }
        return value;
    }

    private static String validateDouble(String value, String fieldName, StringBuilder errors) {
        if (value == null || value.isEmpty()) {
            errors.append("Vyplňte prosím pole '").append(fieldName).append("'").append(". <br />");
        } else if (!isDouble(value)) {
            errors.append("Vložte prosím desatinné číslo do poľa '").append(fieldName).append("'").append(". <br />");
        }
        return value;
    }

    private static String validateUrl(String value, String fieldName, StringBuilder errors) {
        UrlValidator urlValidator = new UrlValidator();
        if (!value.startsWith("http://")) {
            value = "http://" + value;
        }
        boolean valid = urlValidator.isValid(value);
        if (value == null || value.isEmpty()) {
            errors.append("Zadajte prosím url. do poľa '").append(fieldName).append("'").append(". <br />");
        } else if (!valid) {
            errors.append("Url ").append(value).append(" je neplatné. Zadajte prosím platné url. <br />");
        }

        return value;
    }

    public Setting validateAndToSettings(StringBuilder errors) {
        Setting setting = new Setting();
        setting.setSetting1(validateInt(getSetting1(), "počet stránok", errors));
        setting.setSetting2(validateInt(getSetting2(), "doba čakania", errors));
        setting.setSetting3(validateUrl(getSetting3(), "vlastné URL", errors));
        setting.setSetting4(getSetting4());
        setting.setSetting5(validateDouble(getSetting5(), "prahová hodnota", errors));
        setting.setSetting6(validateInt(getSetting6(), "hĺbka zanorenia crawleru", errors));

        if (errors.length() > 0) {
            return null;
        } else {
            return setting;
        }
    }

    public boolean isUrlOn() {
        if (setting4 == null) {
            return false;
        }
        return true;
    }

    public String getSetting1() {
        return setting1;
    }

    public void setSetting1(String setting1) {
        this.setting1 = setting1;
    }

    public String getSetting2() {
        return setting2;
    }

    public void setSetting2(String setting2) {
        this.setting2 = setting2;
    }

    public String getSetting3() {
        return setting3;
    }

    public void setSetting3(String setting3) {
        this.setting3 = setting3;
    }

    public String getSetting4() {
        return setting4;
    }

    public void setSetting4(String setting4) {
        this.setting4 = setting4;
    }

    public String getSetting5() {
        return setting5;
    }

    public void setSetting5(String setting5) {
        this.setting5 = setting5;
    }

    public String getSetting6() {
        return setting6;
    }

    public void setSetting6(String setting6) {
        this.setting6 = setting6;
    }

}
