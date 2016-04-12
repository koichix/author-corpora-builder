/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run_module;

import java.io.File;
import java.util.List;
import sk.svec.jan.acb.cleaning.HtmlCleaner;
import sk.svec.jan.acb.extraction.DiscussionFinder;
import sk.svec.jan.acb.extraction.Finder;
import sk.svec.jan.acb.site_style_tree.SiteStyleTree;
import sk.svec.jan.acb.utility.CreateZip;
import sk.svec.jan.acb.utility.TravelDirectory;

/**
 *
 * @author Koichi
 */
public class Analyzer_run {

    private String path;

    public void analyze(boolean disc) throws Exception {
        this.path = "output/";
      

        //travel all the directories
        TravelDirectory travelDirectory = new TravelDirectory(path);
        List<String> directories = travelDirectory.getDirectories();
        for (String p : directories) {

            File file = new File(p + "downloaded/");
            if (file.listFiles().length > 1) {

                //html cleaner
                new File(p + "cleaned/").mkdirs();
                HtmlCleaner htmlCleaner = new HtmlCleaner(p);
                System.out.println("All pages cleaned succesfully!");

                //site style tree
                new File(p + "extracted/").mkdirs();
                SiteStyleTree siteStyleTree = new SiteStyleTree(p);
                System.out.println("All pages extracted succesfully!");

                if (disc) {
                    //discussion finder
                    new File(p + "results/").mkdirs();
                    DiscussionFinder discussionFinder = new DiscussionFinder(p);
                    System.out.println("All data saved succesfully!");
                } else {
                    //finder
                    new File(p + "results/").mkdirs();
                    Finder finder = new Finder(p);
                    System.out.println("All data saved succesfully!");

                }
            }
        }

        //vytvori zip z priecinku autorov       
        CreateZip createZip = new CreateZip(path);
    }

    public static void main(String[] args) throws Exception {
        Analyzer_run run = new Analyzer_run();
        run.analyze(false);
    }
}
