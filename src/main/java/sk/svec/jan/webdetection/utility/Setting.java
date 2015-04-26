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
package sk.svec.jan.webdetection.utility;

/**
 *
 * @author Ján Švec
 */
public class Setting {

    private String setting1;
    private String setting2;
    private String setting3;
    private String setting4;
    private String setting5;
    private String setting6;

    public Setting() {
    }

    public Setting(String setting1, String setting2, String setting3, String setting4, String setting5, String setting6) {
        this.setting1 = setting1;
        this.setting2 = setting2;
        this.setting3 = setting3;
        this.setting4 = setting4;
        this.setting5 = setting5;
        this.setting6 = setting6;
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
