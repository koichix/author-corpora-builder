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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Ján Švec
 */
public class CreateZip {

    List<String> fileList;

    public CreateZip(String path) {

        FindDirectoryAuthor fd = new FindDirectoryAuthor(path);
        for (String dir : fd.getDirectories()) {
            fileList = new ArrayList<>();
            String sourceFolder = dir + "author/";
            String outputZipFile = dir + "author.zip";
            generateFileList(new File(sourceFolder));
            zipIt(sourceFolder, outputZipFile);
        }

    }

    public List<String> getFileList() {
        return fileList;
    }

    private void zipIt(String sourceFolder, String outputZipFile) {

        byte[] buffer = new byte[1024];

        try {

            FileOutputStream fos = new FileOutputStream(outputZipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + outputZipFile);

            for (String file : this.fileList) {

                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);

                FileInputStream in
                        = new FileInputStream(new File(sourceFolder + file));

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                in.close();
            }

            zos.closeEntry();
            //remember close it
            zos.close();

            System.out.println("Done");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void generateFileList(File node) {

        //add file only
        if (node.isFile()) {
            fileList.add(node.getName());
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileList(new File(node, filename));
            }
        }

    }

}
