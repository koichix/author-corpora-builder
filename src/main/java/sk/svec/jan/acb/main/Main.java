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
package sk.svec.jan.acb.main;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.validator.routines.UrlValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sk.svec.jan.acb.cleaning.HtmlCleaner;
import sk.svec.jan.acb.crawling.Crawler;
import sk.svec.jan.acb.extraction.DiscussionFinder;
import sk.svec.jan.acb.extraction.Finder;
import sk.svec.jan.acb.site_style_tree.SiteStyleTree;
import sk.svec.jan.acb.utility.CleanDirectory;
import sk.svec.jan.acb.utility.ConfigCreator;
import sk.svec.jan.acb.utility.CreateZip;
import sk.svec.jan.acb.utility.Setting;
import sk.svec.jan.acb.web.TravelCorpusDirectory;
import sk.svec.jan.acb.utility.TravelDirectory;
import sk.svec.jan.acb.web.ViewXML;

/**
 *
 * @author Ján Švec
 */
public class Main {

    private List<String> seeds;
    private Setting settings;
    private String path;
    private List<String> output;
    private String outputInfo;
    private List<String> zipPath;
    public static boolean diskusia;

    public static ResourceBundle getResourceBundle() {
        File file = new File("src/resources/");
        URL[] urls = null;
        try {
            urls = new URL[]{file.toURI().toURL()};
        } catch (MalformedURLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClassLoader loader = new URLClassLoader(urls);

        ResourceBundle rb = ResourceBundle.getBundle("config", Locale.getDefault(), loader);

        return rb;
//        return java.util.ResourceBundle.getBundle("resources/config");
    }

    public void setSeeds(List<String> seeds) {
        this.seeds = seeds;
    }

    public List<String> getSeeds() {
        return seeds;
    }

    public Setting getSettings() {
        return settings;
    }

    public String getOutputInfo() {
        outputInfo = "Aplikácia úspešne extrahovala " + getResourceBundle().getString("MaxPagesToFetch") + " článkov. Nastavenie politeness bolo: " + getResourceBundle().getString("PolitenessDelay") + " ms. Prahová hodnota pre čistenie bola nastavená na: " + getResourceBundle().getString("Threshold") + ". ";
        if (Boolean.parseBoolean(getResourceBundle().getString("CustomUrlSet"))) {
            outputInfo = outputInfo + "Filter URL adresy bol nastavený na adresu: " + getResourceBundle().getString("CustomUrl");
        }
        return outputInfo;
    }

    public List<String> getZipPath() {
        zipPath = new ArrayList<>();
        TravelDirectory travelDirectory = new TravelDirectory(path);
        List<String> directories = travelDirectory.getDirectories();
        for (String p : directories) {
            if (new File(p + "author.zip").exists()) {
                String authorPath = p + "author.zip";
                zipPath.add("<a href=\"/getfile?name=" + authorPath + "\"> " + authorPath.substring(authorPath.indexOf("/") + 1, authorPath.lastIndexOf("/")).replace("/", ".") + ".zip</a>");
            }

        }
        return zipPath;
    }

    public void setSettings(Setting settings) {
        this.settings = settings;
        ConfigCreator c = new ConfigCreator();
        c.saveProperties(settings);
    }

    public Main() {
        //create properties files
        new File("src/resources/").mkdirs();
        ConfigCreator configCreator = new ConfigCreator();
        //ak config subor existuje, nevytvarame ho znova
        if (!new File("src/resources/config.properties").isFile()) {
            configCreator.initialize();
        }
        configCreator.initializeLog4j();
        org.apache.log4j.PropertyConfigurator.configure("src/resources/log4j.properties");

        this.settings = new Setting(getResourceBundle().getString("MaxPagesToFetch"), getResourceBundle().getString("PolitenessDelay"), getResourceBundle().getString("CustomUrl"), getResourceBundle().getString("CustomUrlSet"), getResourceBundle().getString("Threshold"), getResourceBundle().getString("MaxDepth"));
        this.path = "output/"; //tento path potom natiahneme do nastaveni, a tym umoznime jeho zavolanie aj v crawlerConfig() kde je zatial napevno      

    }

    public void clean() {
        //clean directory output
        CleanDirectory cd = new CleanDirectory(path);
        cd.run();
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
        CleanDirectory cd = new CleanDirectory(path);
        cd.run();

        //crawler        
        Crawler crawler = new Crawler(seeds, disc);
        crawler.startCrawling();
    }

    public void analyze(boolean disc) throws Exception {
        if (disc) {
            diskusia = true;
        } else {
            diskusia = false;
        }

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

    public void start(boolean disc) throws Exception {
        //clean directory output
//        CleanDirectory cd = new CleanDirectory(path);
//        cd.run();

        //crawler        
        Crawler crawler = new Crawler(seeds, disc);
        crawler.startCrawling();
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
                    diskusia = true;
                    //discussion finder
                    new File(p + "results/").mkdirs();
                    DiscussionFinder discussionFinder = new DiscussionFinder(p);
                    System.out.println("All data saved succesfully!");
                } else {
                    diskusia = false;
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

    private String findLink(File f) throws Exception {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Document doc = dbFactory.newDocumentBuilder().parse(f);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("Article");

        for (int node = 0; node < nList.getLength(); node++) {
            Node nNode = nList.item(node);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                return eElement.getElementsByTagName("Link").item(0).getTextContent();

            }
        }
        return "No link";
    }

    public List<String> getOutput() throws Exception {
        this.output = new ArrayList<String>();
        TravelDirectory travelDirectory = new TravelDirectory(path);
        List<String> directories = travelDirectory.getDirectories();
        for (String p : directories) {
            if (new File(p + "results/").exists()) {
                File folder = new File(p + "results/");
                File[] listOfFiles = folder.listFiles();

                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) {
                        String link = findLink(listOfFile);
                        String xmlPath = listOfFile.getPath();
                        //System.out.println( xmlPath+ " " + link);
                        link = (link.length() > 36) ? link.substring(0, 40) + "..." : link;
                        output.add("<td><a href=\"" + link + "\">" + link + "</a></td> <td> <a href=\"/getfile?name=" + xmlPath + "\"> " + xmlPath.substring(xmlPath.length() - 36) + "</a></td>");
                    }
                }
            }

        }
        return output;
    }

    private static boolean validateUrl(String value) {
        StringBuilder errors = new StringBuilder();
        UrlValidator urlValidator = new UrlValidator();
        if (!value.startsWith("http://")) {
            value = "http://" + value;
        }
        boolean valid = urlValidator.isValid(value);
        if (value == null || value.isEmpty()) {
            errors.append("Zadajte prosím url.");
            return false;
        } else if (!valid) {
            errors.append("Url ").append(value).append(" je neplatné. Zadajte prosím platné url.");
            return false;
        }
        return true;
    }

    public List<String> getCorpusFiles() throws Exception {
        List<String> output = new ArrayList<String>();
        TravelCorpusDirectory travelCorpusDirectory = new TravelCorpusDirectory("corpus/");
        List<String> directories = travelCorpusDirectory.getDirectories();
        int i = 0;
        for (String p : directories) {
            output.add("<a class=\"showSingle\" target=\"" + i + "\">" + p + "</a>");
            output.add("<div id=\"div" + i + "\" class=\"targetDiv\">");

            if (new File(p + "author/").exists()) {
                File folder = new File(p + "author/");
                File[] listOfFiles = folder.listFiles();

                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) {
                        String xmlPath = listOfFile.getPath();

                        if (listOfFile.getName().compareTo("deletedLinksLog.xml") != 0) {//don't view deletetlinksLog.xml    
                            String link = "<a href=\"/preview?name=" + xmlPath + "\"> " + xmlPath.substring(xmlPath.length() - 36) + "</a>";
                            //System.out.println(link);
                            output.add(link);
                        }

                    }
                }
            }
            output.add("</div></br>");
            i++;
        }
        return output;
    }

    public String viewXML(String path) {
        ViewXML xml = new ViewXML();
        try {
            return (xml.getXMLcontent(path));
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zvoľte prosím vybraný súbor z menu.";
    }

    public static void main(String[] args) throws Exception {
        Main test = new Main();
        System.out.println(test.getCorpusFiles());

//        JSAP jsap = new JSAP();
//
//        FlaggedOption opt1 = new FlaggedOption("stiahni")
//                .setStringParser(JSAP.STRING_PARSER)
//                .setRequired(false)
//                .setShortFlag('s')
//                .setLongFlag("stiahni");
//        opt1.setHelp("aplikácia stiahne určený počet stránok zo zadaného URL");
//        jsap.registerParameter(opt1);
//
//        FlaggedOption opt2 = new FlaggedOption("spusti")
//                .setStringParser(JSAP.STRING_PARSER)
//                .setRequired(false)
//                .setShortFlag('x')
//                .setLongFlag("spusti");
//        opt2.setHelp("aplikácia stiahne určený počet stránok zo zadaného URL a analyzuje stiahnuté dáta");
//        jsap.registerParameter(opt2);
//
//        Switch sw1 = new Switch("diskusia")
//                .setShortFlag('d')
//                .setLongFlag("diskusia");
//        sw1.setHelp("nastavenie či sa jedná o diskusiu alebo článok");
//        jsap.registerParameter(sw1);
//
//        Switch sw2 = new Switch("analyzuj")
//                .setShortFlag('a')
//                .setLongFlag("analyzuj");
//        sw2.setHelp("aplikácia analyzuje stiahnuté dáta");
//        jsap.registerParameter(sw2);
//
//        Switch sw3 = new Switch("vymaz")
//                .setShortFlag('v')
//                .setLongFlag("vymaz");
//        sw3.setHelp("aplikácia vymaže všetky predchádzajúce stiahnuté dáta");
//        jsap.registerParameter(sw3);
//
//        FlaggedOption opt3 = new FlaggedOption("maxpages")
//                .setStringParser(JSAP.STRING_PARSER)
//                .setRequired(false)
//                .setShortFlag('m')
//                .setLongFlag("maxpages");
//        opt3.setHelp("nastavenie počtu stiahnutých článkov");
//        jsap.registerParameter(opt3);
//
//        FlaggedOption opt4 = new FlaggedOption("politeness")
//                .setStringParser(JSAP.STRING_PARSER)
//                .setRequired(false)
//                .setShortFlag('p')
//                .setLongFlag("politeness");
//        opt4.setHelp("nastavenie doby čakania medzi stiahnutím viacerých stránok v milisekundách");
//        jsap.registerParameter(opt4);
//
//        FlaggedOption opt5 = new FlaggedOption("threshold")
//                .setStringParser(JSAP.STRING_PARSER)
//                .setRequired(false)
//                .setShortFlag('t')
//                .setLongFlag("threshold");
//        opt5.setHelp("nastavenie váhy prahovej hodnoty pre čistenie stránky od boilerplate pomocou SST");
//        jsap.registerParameter(opt5);
//
//        FlaggedOption opt6 = new FlaggedOption("maxdepth")
//                .setStringParser(JSAP.STRING_PARSER)
//                .setRequired(false)
//                .setShortFlag('h')
//                .setLongFlag("maxdepth");
//        opt6.setHelp("celé číslo od 1 do N symbolizuje hĺbku zanorenia crawleru vrámci URL adresy");
//        jsap.registerParameter(opt6);
//
//        FlaggedOption opt7 = new FlaggedOption("customurl")
//                .setStringParser(JSAP.STRING_PARSER)
//                .setRequired(false)
//                .setShortFlag('c')
//                .setLongFlag("customurl");
//        opt7.setHelp("nastavenie filtra URL adresy, pomocou ktorého bude filtrovať sťahované stránky");
//        jsap.registerParameter(opt7);
//
//        FlaggedOption opt8 = new FlaggedOption("setcustomurl")
//                .setStringParser(JSAP.BOOLEAN_PARSER)
//                .setRequired(false)
//                .setShortFlag('u')
//                .setLongFlag("setcustomurl");
//        opt8.setHelp("vypne alebo zapne použitie custom url [t/f]");
//        jsap.registerParameter(opt8);
//
//        Switch sw4 = new Switch("napoveda")
//                .setShortFlag('n')
//                .setLongFlag("napoveda");
//        sw4.setHelp("zobrazí nápovedu");
//        jsap.registerParameter(sw4);
//
//        JSAPResult config = jsap.parse(args);
//
//        if (!config.success()) {
//            System.err.println();
//            for (java.util.Iterator errs = config.getErrorMessageIterator();
//                    errs.hasNext();) {
//                System.err.println("Chyba: " + errs.next());
//            }
//            System.err.println("Použitie: java " + Main.class.getName());
//            System.err.println(jsap.getUsage());
//            System.err.println();
//            System.err.println(jsap.getHelp());
//            System.exit(1);
//        }
//
//        if (config.getBoolean("napoveda")) {
//            System.out.println(jsap.getUsage());
//            System.out.println();
//            System.out.println(jsap.getHelp());
//        }
//
//        Main wdm = new Main();
//        if (config.getBoolean("vymaz")) {
//            System.out.println("mazem priecinok");
//            wdm.clean();
//        }
//
//        String maxpages = config.getString("maxpages");
//        if (maxpages != null) {
//            if (maxpages.compareTo("null") != 0) {
//                System.out.println("nastavujem maxpages na " + maxpages);
//
//                Setting setting = new Setting();
//                setting.setSetting1(maxpages); //"MaxPagesToFetch"
//                setting.setSetting2(wdm.getSettings().getSetting2()); //"PolitenessDelay"
//                setting.setSetting3(wdm.getSettings().getSetting3()); //"CustomUrl"
//                setting.setSetting4(wdm.getSettings().getSetting4()); //"CustomUrlSet"
//                setting.setSetting5(wdm.getSettings().getSetting5()); //"Threshold"
//                setting.setSetting6(wdm.getSettings().getSetting6()); //"MaxDepth" 
//                wdm.setSettings(setting);
//            }
//        }
//        String politeness = config.getString("politeness");
//        if (politeness != null) {
//            if (politeness.compareTo("null") != 0) {
//                System.out.println("nastavujem politeness na " + politeness);
//                Setting setting = new Setting();
//                setting.setSetting1(wdm.getSettings().getSetting1()); //"MaxPagesToFetch"
//                setting.setSetting2(politeness); //"PolitenessDelay"
//                setting.setSetting3(wdm.getSettings().getSetting3()); //"CustomUrl"
//                setting.setSetting4(wdm.getSettings().getSetting4()); //"CustomUrlSet"
//                setting.setSetting5(wdm.getSettings().getSetting5()); //"Threshold"
//                setting.setSetting6(wdm.getSettings().getSetting6()); //"MaxDepth" 
//                wdm.setSettings(setting);
//            }
//        }
//        String threshold = config.getString("threshold");
//        if (threshold != null) {
//            if (threshold.compareTo("null") != 0) {
//                System.out.println("nastavujem threshold na " + threshold);
//
//                Setting setting = new Setting();
//                setting.setSetting1(wdm.getSettings().getSetting1()); //"MaxPagesToFetch"
//                setting.setSetting2(wdm.getSettings().getSetting2()); //"PolitenessDelay"
//                setting.setSetting3(wdm.getSettings().getSetting3()); //"CustomUrl"
//                setting.setSetting4(wdm.getSettings().getSetting4()); //"CustomUrlSet"
//                setting.setSetting5(threshold); //"Threshold"
//                setting.setSetting6(wdm.getSettings().getSetting6()); //"MaxDepth" 
//                wdm.setSettings(setting);
//            }
//        }
//        String maxdepth = config.getString("maxdepth");
//        if (maxdepth != null) {
//            if (maxdepth.compareTo("null") != 0) {
//                System.out.println("nastavujem maxdepth na " + maxdepth);
//
//                Setting setting = new Setting();
//                setting.setSetting1(wdm.getSettings().getSetting1()); //"MaxPagesToFetch"
//                setting.setSetting2(wdm.getSettings().getSetting2()); //"PolitenessDelay"
//                setting.setSetting3(wdm.getSettings().getSetting3()); //"CustomUrl"
//                setting.setSetting4(wdm.getSettings().getSetting4()); //"CustomUrlSet"
//                setting.setSetting5(wdm.getSettings().getSetting5()); //"Threshold"
//                setting.setSetting6(maxdepth); //"MaxDepth" 
//                wdm.setSettings(setting);
//            }
//        }
//        String customurl = config.getString("customurl");
//        if (customurl != null) {
//            if (customurl.compareTo("null") != 0) {
//                if (!customurl.startsWith("http://")) {
//                    customurl = "http://" + customurl;
//                }
//                if (validateUrl(customurl)) {
//
//                    System.out.println("nastavujem customurl na " + customurl);
//                    System.out.println("zapinam customurl na true");
//
//                    Setting setting = new Setting();
//                    setting.setSetting1(wdm.getSettings().getSetting1()); //"MaxPagesToFetch"
//                    setting.setSetting2(wdm.getSettings().getSetting2()); //"PolitenessDelay"
//                    setting.setSetting3(customurl); //"CustomUrl"
//                    setting.setSetting4("true"); //"CustomUrlSet"
//                    setting.setSetting5(wdm.getSettings().getSetting5()); //"Threshold"
//                    setting.setSetting6(wdm.getSettings().getSetting6()); //"MaxDepth" 
//                    wdm.setSettings(setting);
//                } else {
//                    System.err.println("Url " + customurl + " je neplatné. Zadajte prosím platné url.");
//                }
//            }
//        }
//        if (config.contains("setcustomurl")) {
//            boolean setcustomurl = config.getBoolean("setcustomurl");
//            if (setcustomurl) {
//                System.out.println("nastavujem pouzitie custom url na true");
//
//                Setting setting = new Setting();
//                setting.setSetting1(wdm.getSettings().getSetting1()); //"MaxPagesToFetch"
//                setting.setSetting2(wdm.getSettings().getSetting2()); //"PolitenessDelay"
//                setting.setSetting3(wdm.getSettings().getSetting3()); //"CustomUrl"
//                setting.setSetting4("true"); //"CustomUrlSet"
//                setting.setSetting5(wdm.getSettings().getSetting5()); //"Threshold"
//                setting.setSetting6(wdm.getSettings().getSetting6()); //"MaxDepth" 
//                wdm.setSettings(setting);
//            } else {
//                System.out.println("nastavujem pouzitie custom url na false");
//
//                Setting setting = new Setting();
//                setting.setSetting1(wdm.getSettings().getSetting1()); //"MaxPagesToFetch"
//                setting.setSetting2(wdm.getSettings().getSetting2()); //"PolitenessDelay"
//                setting.setSetting3(wdm.getSettings().getSetting3()); //"CustomUrl"
//                setting.setSetting4("false"); //"CustomUrlSet"
//                setting.setSetting5(wdm.getSettings().getSetting5()); //"Threshold"
//                setting.setSetting6(wdm.getSettings().getSetting6()); //"MaxDepth" 
//                wdm.setSettings(setting);
//            }
//        }
//
//        String url2 = config.getString("spusti");
//        if (url2 != null) {
//            if (url2.compareTo("null") != 0) {
//                if (!url2.startsWith("http://")) {
//                    url2 = "http://" + url2;
//                }
//                if (validateUrl(url2)) {
//                    System.out.println("stahujem aj analyzujem url " + validateUrl(url2));
//                    if (config.getBoolean("diskusia")) {
//                        System.out.println("je to diskusia");
//                        List<String> link = new ArrayList();
//                        link.add(url2);
//                        wdm.setSeeds(link);
//                        wdm.start(true);
//
//                    } else {
//                        System.out.println("nieje to diskusia");
//                        List<String> link = new ArrayList();
//                        link.add(url2);
//                        wdm.setSeeds(link);
//                        wdm.start(false);
//                    }
//                } else {
//                    System.err.println("Url " + url2 + " je neplatné. Zadajte prosím platné url.");
//                }
//            }
//        }
//
//        String url = config.getString("stiahni");
//        if (url != null) {
//            if (url.compareTo("null") != 0) {
//                if (!url.startsWith("http://")) {
//                    url = "http://" + url;
//                }
//                if (validateUrl(url)) {
//                    System.out.println("stahujem z url " + validateUrl(url));
//                    if (config.getBoolean("diskusia")) {
//                        System.out.println("je to diskusia");
//                        System.out.println("je to diskusia");
//                        List<String> link = new ArrayList();
//                        link.add(url);
//                        wdm.setSeeds(link);
//                        wdm.crawlWithoutClean(true);
//
//                    } else {
//                        System.out.println("nieje to diskusia");
//                        List<String> link = new ArrayList();
//                        link.add(url);
//                        wdm.setSeeds(link);
//                        wdm.crawlWithoutClean(false);
//                    }
//                } else {
//                    System.err.println("Url " + url + " je neplatné. Zadajte prosím platné url.");
//                }
//            }
//        }
//
//        if (config.getBoolean("analyzuj")) {
//            System.out.println("analyzujem");
//            if (config.getBoolean("diskusia")) {
//                System.out.println("je to diskusia");
//                wdm.analyze(true);
//            } else {
//                System.out.println("nieje to diskusia");
//                wdm.analyze(false);
//            }
//
//        }

        /*
         java -jar wsd-1.0-jar-with-dependencies.jar -s www.cas.sk
   
         //vsetky prepinace
         -spusti -diskusia http://cas.sk -maxpages 20 -politeness 200  -threshold 0.2 -maxdepth 2 -customurl url -setcustomurl t

         -stiahni -diskusia http://cas.sk
         -analyzuj -diskusia

         -vymaz
         -napoveda
         */
    }
}
