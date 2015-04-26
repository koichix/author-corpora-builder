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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ján Švec
 */
public class FindDirectoryAuthor {

    private List<String> directories;
//    private int count = 0;
//
//    public int getCount() {
//        return count;
//    }

    public List<String> getDirectories() {
        return directories;
    }

    public FindDirectoryAuthor(String path) {
        directories = new ArrayList<String>();
        File f = new File(path);
        if (f.exists() && f.isDirectory()) {
            travelDir(f);
        }
    }

    public void travelDir(File f) {
        File[] files = f.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    if (files[i].getPath().contains("author")) {
//                        System.out.println(files[i].getPath());
//                        count++;
                        directories.add(files[i].getPath().replace("author", "").replaceAll("\\\\", "/"));
                    }
                    travelDir(files[i]);

                }
            }
        }
    }

}
