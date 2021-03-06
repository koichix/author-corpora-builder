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
package evaluation;

import java.util.ArrayList;
import java.util.List;
import sk.svec.jan.acb.main.Main;
import sk.svec.jan.acb.utility.Setting;
import sk.svec.jan.acb.utility.Setting;

/**
 *
 * @author Ján Švec
 */
public class ClanokDownloader {

    public ClanokDownloader(String url,String customUrl) throws Exception {
        List<String> link = new ArrayList();
        Setting setting = new Setting();
        setting.setSetting1("100"); //"MaxPagesToFetch" pocet stiahnutych stranok
        setting.setSetting2("100"); //"PolitenessDelay" doba cakania medzi strankami      
        setting.setSetting5("0.2"); //"Threshold" prahova hodnota
        setting.setSetting6("-1"); //"MaxDepth" maximalna hlbka
        setting.setSetting4("true"); //"CustomUrlSet" zapnute vlastne url 
        setting.setSetting3(customUrl); //"CustomUrl" vlastne url        
        link.add(url);
        boolean stahuj = true; //true= stahovanie, false =analyza
        boolean diskusia = false; //false = clanok, true =diskusia

        //nemenit
        Main wdm = new Main();
        wdm.setSettings(setting);
        wdm.setSeeds(link);
        if (stahuj) {
            wdm.crawlWithoutClean(diskusia);
        } else {
            wdm.analyze(diskusia);
        }
    }

}
