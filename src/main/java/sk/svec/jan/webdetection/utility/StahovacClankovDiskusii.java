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
public class StahovacClankovDiskusii {
////        ******************************************
////        *****************DISKUSIE*****************        
////        ******************************************
//        DiskusieDownloader dd = new DiskusieDownloader("http://www.birdz.sk/forum/oblubena-distribucia-linuxu/151760-tema.html", "http://www.birdz.sk/forum/");
//        DiskusieDownloader dd2 = new DiskusieDownloader("http://debata.pravda.sk/debata/341181-merkelova-tlaci-na-kremel-putin-nevrati-krym-kyjevu/", "http://debata.pravda.sk");
//        DiskusieDownloader dd3 = new DiskusieDownloader("http://diskuse.tiscali.cz/games/clanek/vsechno-nejlepsi-v-roce-2015-preje-redakce-games_cz-246305/", "http://diskuse.tiscali.cz/games/clanek/");
//        DiskusieDownloader dd4 = new DiskusieDownloader("http://www.cas.sk/forum/288148/exfarmar-tibor-zhana-zamestnanie-nema-problem-vypytat-si-poriadny-plat-tu-je-jeho-inzerat/1", "http://www.cas.sk/clanok/");
//        DiskusieDownloader dd5 = new DiskusieDownloader("http://www.novinky.cz/diskuse?id=385326&articleId=/veda-skoly/355004-vedci-objevili-vypinac-bolesti.html&sectionId=986", "http://www.novinky.cz/diskuse");
//        DiskusieDownloader dd7 = new DiskusieDownloader("http://www.sme.sk/diskusie/2379272/1/Meseznikov-Kiska-narusil-plany-na-maksiu-verziu-rezimu-putinovskeho-typu.html", "http://www.sme.sk/diskusie/");
//        DiskusieDownloader dd8 = new DiskusieDownloader("http://forum.twinstar.cz/showthread.php/27-Pravidla-f%C3%B3ra-Posledn%C3%AD-zm%C4%9Bna-27-6-2012", "http://forum.twinstar.cz/showthread.php");
//        DiskusieDownloader dd9 = new DiskusieDownloader("http://www.blesk.cz/diskuse/372213/1", "http://www.blesk.cz/diskuse/");
//        DiskusieDownloader dd10 = new DiskusieDownloader("http://www.fony.sk/forum/clanky/8815-blackberry-classic", "http://www.fony.sk/forum/clanky/");
//        DiskusieDownloader dd11 = new DiskusieDownloader("http://www.cuketka.cz/forum/topic.php?id=130", "http://www.cuketka.cz/forum/");
//        DiskusieDownloader dd12 = new DiskusieDownloader("http://www.lidovky.cz/diskuse.aspx?iddiskuse=A141203_210641_ln_zahranici_ele", "http://www.lidovky.cz/diskuse");
//        DiskusieDownloader dd13 = new DiskusieDownloader("http://www.denik.cz/diskuse/ve-veku-sedmdesati-let-zemrel-slavny-zpevak-joe-cocker.html", "http://www.denik.cz/diskuse/");
//        DiskusieDownloader dd14 = new DiskusieDownloader("http://sport.sme.sk/diskusie/2376515/1/Halak-vychytal-vitazstvo-Gaborik-mal-11-Oveckin-sa-zaskvel.html", "http://sport.sme.sk/diskusie/");
//        DiskusieDownloader dd15 = new DiskusieDownloader("http://debata.pravda.sk/debata/340326-glvac-povolil-vojakom-na-misiach-sviatky-pripitok/", "http://debata.pravda.sk/debata/");
//        DiskusieDownloader dd16 = new DiskusieDownloader("http://varecha.pravda.sk/diskusie/ako-upiect-fajneho-moriaka/6548-forum.html", "http://varecha.pravda.sk/diskusie/");
//        DiskusieDownloader dd17 = new DiskusieDownloader("http://www.cas.sk/forum/288148/exfarmar-tibor-zhana-zamestnanie-nema-problem-vypytat-si-poriadny-plat-tu-je-jeho-inzerat/1", "http://www.cas.sk/forum/");
//        analyzuj diskusie
//        WebDetectionMain wdm = new WebDetectionMain();
//        wdm.analyze(true);
//        DiscussionFinder j = new DiscussionFinder("output/cz/cuketka/");
//
//        ClanokDownloader cd = new ClanokDownloader("http://zpravy.idnes.cz/situace-na-silnicich-ledovka-dgr-/domaci.aspx?c=A150102_073424_domaci_jj", "http://zpravy.idnes.cz/");
//        ClanokDownloader cd4 = new ClanokDownloader("http://www.novinky.cz/domaci/357621-silnice-jsou-s-opatrnosti-sjizdne-a-bez-nehod.html", "http://www.novinky.cz/domaci/");
//        ClanokDownloader cd3 = new ClanokDownloader("http://www.topky.sk/cl/100313/1447073/Lekarka-zo-slovenskeho-soubiznisu--Tragedia-na-Stedry-den--obet-bojuje-o-zivot-", "http://www.topky.sk/cl/");
//        ClanokDownloader cd5 = new ClanokDownloader("http://www.bbc.com/news/uk-england-merseyside-30656011", "http://www.bbc.com/news/uk-england");
//        ClanokDownloader cd6 = new ClanokDownloader("http://news.discovery.com/tech/apps/holiday-apps-lower-stress-pile-on-smiles-141217.htm", "http://news.discovery.com/tech/");
//        ClanokDownloader cd7 = new ClanokDownloader("http://zpravy.aktualne.cz/domaci/nahe-fotky-vydirani-kybersikana-se-rozmaha-i-v-cesku/r~63bee80485fd11e4bdad0025900fea04/", "http://zpravy.aktualne.cz/domaci/");
//        ClanokDownloader cd7 = new ClanokDownloader("http://zahranicni.ihned.cz/c3-63326830-090000_d-63326830-obama-vyhlasil-sankce-proti-severni-koreji-jsou-odvetou-za-hackersky-utok", "http://zahranicni.ihned.cz/");
//        ClanokDownloader cd8 = new ClanokDownloader("http://www.aktuality.sk/clanok/268053/shmu-predpoved-pocasia-na-vikend/?utm_source=azet.sk&utm_medium=box-magaziny-aktuality&utm_content=clanok1&utm_campaign=HP-new2#", "http://www.aktuality.sk/clanok/");
//        ClanokDownloader cd9 = new ClanokDownloader("http://www.noviny.sk/c/krimi/raper-32-dobodal-na-novy-rok-kamarata", "http://www.noviny.sk/c/");
//        ClanokDownloader cd70 = new ClanokDownloader("http://www.tvnoviny.sk/domace/1782038_pozor-na-odvody-od-noveho-roka-zaplatime-viac", "http://www.tvnoviny.sk/domace/");
//        ClanokDownloader cd70 = new ClanokDownloader("http://www.dailymail.co.uk/news/article-2894480/Prince-Andrew-repeatedly-slept-sex-slave-controlled-underage-prostitution-ring-U-S-court-papers-claim.html", "http://www.dailymail.co.uk/news/article");
//        ClanokDownloader cd70 = new ClanokDownloader("http://www.reuters.com/article/2015/01/02/us-indonesia-airplane-idUSKBN0K900D20150102", "http://www.reuters.com/article/");
//        ClanokDownloader cd70 = new ClanokDownloader("http://www.independent.co.uk/news/world/asia/no-google-this-is-not-a-photo-of-kim-jonguns-sister-9954590.html", "http://www.independent.co.uk/news/");
//        analyzuj clanky
//        WebDetectionMain wdm = new WebDetectionMain();
//        wdm.analyze(false);
////////////////////////////////////////////////////////////////////////////////////////   
////        ******************************************
////        *****************CLANKY*******************        
////        ******************************************
//        setting.setSetting3("http://www.cas.sk/clanok/"); //"CustomUrl" vlastne url        
//        link.add("http://www.cas.sk/clanok/303038/exfarmar-tibor-zhana-zamestnanie-nema-problem-vypytat-si-poriadny-plat-tu-je-jeho-inzerat.html");
////        setting.setSetting3("http://varecha.pravda.sk/recepty/"); //"CustomUrl" vlastne url        
////        link.add("http://varecha.pravda.sk/recepty/steak-z-lahkym-salatom/55881-recept.html");
////        setting.setSetting3("http://pctuning.tyden.cz/navody/"); //"CustomUrl" vlastne url        
////        link.add("http://pctuning.tyden.cz/navody/upravy-modding/32079-stroje-selective-fire-vykonne-elegantni-a-pekelne-drahe");
////        setting.setSetting3("http://uk.businessinsider.com/"); //"CustomUrl" vlastne url        
////        link.add("http://uk.businessinsider.com/isis-is-much-stronger-and-much-more-dangerous-than-people-realize-2014-12");
////        setting.setSetting3("http://sport.sme.sk/c/"); //"CustomUrl" vlastne url        
////        link.add("http://sport.sme.sk/c/7559688/slovan-ziskal-trojku-draftu-nhl-2004-obrancu-barkera.html");
////        setting.setSetting3("http://mojdom.zoznam.sk/cl/"); //"CustomUrl" vlastne url        
////        link.add("http://mojdom.zoznam.sk/cl/10065/1445929/10-domacich-kniznic--ake-ste-este-nevideli");
////        setting.setSetting3("http://www.svetandroida.cz/"); //"CustomUrl" vlastne url        
////        link.add("http://www.svetandroida.cz/vanoce-hry-aplikace-201412");
////        setting.setSetting3("http://www.nytimes.com/2014/"); //"CustomUrl" vlastne url        
////        link.add("http://www.nytimes.com/2014/12/22/nyregion/a-widening-rift-between-de-blasio-and-the-police-is-savagely-ripped-open.html?rref=homepage&module=Ribbon&version=origin&region=Header&action=click&contentCollection=Home%20Page&pgtype=article");
////        setting.setSetting3("http://auto.idnes.cz/"); //"CustomUrl" vlastne url        
////        link.add("http://auto.idnes.cz/couvani-s-vozikem-0ma-/automoto.aspx?c=A141217_105706_automoto_fdv");
////        setting.setSetting3("http://www.webnoviny.sk/slovensko/clanok/"); //"CustomUrl" vlastne url        
////        link.add("http://www.webnoviny.sk/slovensko/clanok/904476-kvalita-demokracie-na-slovensku-klesla-prikladov-je-viac/");
////        setting.setSetting3("http://kotaku.com/"); //"CustomUrl" vlastne url        
////        link.add("http://kotaku.com/13-things-you-might-not-know-about-the-last-of-us-1674102405");
}
