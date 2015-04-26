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

import java.io.File;

/**
 *
 * @author Ján Švec
 */
public class CleanDirectory extends Thread {

    private String path;

    public CleanDirectory(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 2; i++) {

            File f = new File(path);
            if (f.exists() && f.isDirectory()) {
                cleanDir(f, i);
            }

        }

    }

    public void cleanDir(File f, int i2) {
        File[] files = f.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    cleanDir(files[i], i2);
                    if (files[i].listFiles().length <= 0) {
                        files[i].delete();
                    }
                } else {
                    cleanFile(files[i]);
                }
            }
        }
    }

    public void cleanFile(File f) {
        f.delete();
    }

}
