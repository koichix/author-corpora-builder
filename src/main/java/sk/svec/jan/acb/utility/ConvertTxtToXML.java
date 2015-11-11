/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.svec.jan.acb.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;
import sk.svec.jan.acb.extraction.WriteXMLFile;

/**
 *
 * @author Koichi
 */
public class ConvertTxtToXML {

    public static void main(String[] args) throws Exception {

        for (int i = 0; i <= 14; i++) {
            BufferedReader br = null;
            try {
                File input = new File(i + ".txt");
                br = new BufferedReader(new FileReader(input));

                try {
                    String line = br.readLine();

                    while (line != null) {
                        StringTokenizer st = new StringTokenizer(line, "\t");
                        String author = st.nextToken();
                        String link = st.nextToken();
                        String title = st.nextToken();
                        String date = st.nextToken();
                        String text = st.nextToken();

                    //System.out.println(author + " " + link + " " + title + " " + date + " " + text);
                        String xmlFileName = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(author.getBytes())) + ".xml";

                        String xmlAuthorPath = i+"author/" + xmlFileName;
                        new File(i+"author/").mkdirs();
                        //ukladanie pod menom autora, ak existuje subor s autorom, tak ho vezme a ulozi priamo tam
                        WriteXMLFile wxmlf = new WriteXMLFile();

                        File f = new File(xmlAuthorPath);
                        if (f.isFile()) {
                            wxmlf.addToXmlFile(link.trim(), title.trim(), date.trim(), text.trim(), xmlAuthorPath);
                        } else {
                            wxmlf.createXmlFile(author.trim(), link.trim(), title.trim(), date.trim(), text.trim(), xmlAuthorPath);
                        }

                        line = br.readLine();
                    }

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
    }
}
