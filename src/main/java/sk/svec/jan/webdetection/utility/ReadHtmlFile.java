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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ján Švec
 */
public class ReadHtmlFile {

    private String fileText;

    public ReadHtmlFile(String path) {
        BufferedReader br = null;
        try {
            File input = new File(path);
            br = new BufferedReader(new FileReader(input));

            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                fileText = sb.toString();
            } finally {
                br.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(ReadHtmlFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadHtmlFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getFileText() {
        return fileText;
    }

}
