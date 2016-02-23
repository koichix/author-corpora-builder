/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run_module;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import sk.svec.jan.acb.crawling.Crawler;
import sk.svec.jan.acb.utility.CleanDirectory;
import sk.svec.jan.acb.utility.ConfigCreator;

/**
 *
 * @author Koichi
 */
public class Crawler_run {

    private String path;
    private List<String> seeds;

    public void setSeeds(List<String> seeds) {
        this.seeds = seeds;
    }

    public List<String> getSeeds() {
        return seeds;
    }

    public void crawlWithoutClean(boolean disc) throws Exception {
//        //clean directory output
//        CleanDirectory cd = new CleanDirectory(path);
//        cd.run();

        //crawler        
        Crawler crawler = new Crawler(seeds, disc);
        crawler.startCrawling();
    }

    public void crawl(boolean disc) throws Exception {
//        //clean directory output
        this.path = "output/";
        CleanDirectory cd = new CleanDirectory(path);
        cd.run();

        //crawler        
        Crawler crawler = new Crawler(seeds, disc);       
        crawler.startCrawling();
    }

    public static void main(String[] args) throws Exception {
         //create properties files
        new File("src/resources/").mkdirs();
        ConfigCreator configCreator = new ConfigCreator();
        //ak config subor existuje, nevytvarame ho znova
        if (!new File("src/resources/config.properties").isFile()) {
            configCreator.initialize();
        }
        configCreator.initializeLog4j();
        org.apache.log4j.PropertyConfigurator.configure("src/resources/log4j.properties");

        
        String url = "http://zpravy.idnes.cz/za-zlocin-proti-bezpeci-chce-usvit-brat-pristehovalcum-obcanstvi-cr-1g8-/domaci.aspx?c=A151116_115152_domaci_kop";
        Crawler_run wdm = new Crawler_run();
      
        List<String> link = new ArrayList();
        link.add(url);
        wdm.setSeeds(link);
        wdm.crawlWithoutClean(false);
    }
}
