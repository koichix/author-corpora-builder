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
package sk.svec.jan.acb.utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author Ján Švec
 */
public class ConfigCreator {

    public void saveProperties(Setting settings) {

        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("src/resources/config.properties");

            // set the properties value            
            prop.setProperty("MaxPagesToFetch", settings.getSetting1());
            prop.setProperty("PolitenessDelay", settings.getSetting2());
            prop.setProperty("CustomUrl", settings.getSetting3());
            prop.setProperty("CustomUrlSet", settings.getSetting4());
            prop.setProperty("Threshold", settings.getSetting5());
            prop.setProperty("MaxDepth", settings.getSetting6());
            // save properties to selected folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public void initialize() {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("src/resources/config.properties");

            // set the properties value            
            prop.setProperty("MaxPagesToFetch", "20");
            prop.setProperty("PolitenessDelay", "200");
            prop.setProperty("CustomUrl", "http://www.cas.sk/clanok/");
            prop.setProperty("CustomUrlSet", "false");
            prop.setProperty("Threshold", "0.2");
            prop.setProperty("MaxDepth", "-1");

            // save properties to selected folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public void initializeLog4j() {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("src/resources/log4j.properties");

            // set the properties value     
            prop.setProperty("log4j.rootCategory", "DEBUG, stdout");
            prop.setProperty("log4j.appender.stdout.Threshold", "INFO");
            prop.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
            prop.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
            String pr = "%5p [%t] %m%n";
            prop.setProperty("log4j.appender.stdout.layout.ConversionPattern", pr);

            // save properties to selected folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
