<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>  
<c:if test="${not empty error}">
    <div class="ui-widget" >
        <div class="ui-state-highlight ui-corner-all" style="margin-top: 10px; padding:0.7em; margin-bottom: 5px">
            <p> <span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span><c:out escapeXml="false" value="${error}"/>
            </p>
        </div>
    </div>
    <br/>     
</c:if>

<c:if test="${not empty downloaded}">
    <div class="ui-widget" >
        <div class="ui-state-highlight ui-corner-all" style="margin-top: 10px; padding:0.7em; margin-bottom: 5px">
            <p> <span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span><c:out escapeXml="false" value="${downloaded}"/>
            </p>
        </div>
    </div>  
    <br/>
</c:if>


<div class="container">
    <a class="center" href="<c:url value="help"/>">
        <img src="images/design/info.png" style="width: 75px; height: 75px;">
        <br/><fmt:message key="menu.info" />
    </a>
</div>        

<div class="containerTwoColumn"> <div class="containerTwoColumnInside">
        <form action="<c:url value="input"/>" method="post" name="form" >

            <table>
                <tr>                  
                    <td><input type="text" name="link1" class="link" placeholder="<fmt:message key="homepage.input" />" title="<fmt:message key="homepage.input.info" />" value="${inputForm.link1}"/></td>
                    <!-- <td><label><input type="checkbox" name="disc" title="Zaškrtnúť, v prípade že chceme analyzovať diskusie."  value="true"/>diskusia</label></td>
                  
                    <td><input type="Submit" value="Stiahni" title="Stiahne určený počet webstránok z danej domény." name="submit" id="submit"/></td>
                    <td><input type="Submit" value="Analyzuj" id="analyze" title="Analyzuje stiahnuté dáta." name="analyze"/></td>
                    --> 

                    <td><input type="Submit" value="" id="run" title="<fmt:message key="homepage.run" />" name="run" class="run_icon"/></td>
                </tr>

            </table> 
        </form>
        <div id="loading" style="display:none;"><img src="images/design/loading.gif" alt="loading" style="float:left"  /><p style="float:left;margin-top: 6px;margin-left: 2px">Načítavam</p></div>
        <br/>

        <p class="vzor">
            <a href="#" onclick="document.form.link1.value = 'http://mladypodnikatel.cz/tomas-svoboda-pokud-chcete-dosahnout-aktivity-svych-ctenaru-a-divaku-musite-byt-aktivni-predevsim-sami-t11501';
                return false;">mladypodnikatel.cz</a><br/>
            <a href="#" onclick="document.form.link1.value = 'http://tn.nova.cz/clanek/v-cesku-jsou-stale-desetitisice-pozemku-i-domu-bez-majitele.html';
                return false;">tn.nova.cz</a><br/>
            <a href="#" onclick="document.form.link1.value = 'http://www.cas.sk/clanok/303038/exfarmar-tibor-zhana-zamestnanie-nema-problem-vypytat-si-poriadny-plat-tu-je-jeho-inzerat.html';
                return false;">cas.sk</a><br/>
            <a href="#" onclick="document.form.link1.value = 'http://www.novinky.cz/domaci/357474-majitelum-starsich-domu-hrozi-ze-zaplati-za-merice-i-miliony.html';
                return false;">novinky.cz</a><br/>
            <a href="#" onclick="document.form.link1.value = 'http://www.svetandroida.cz/vanoce-hry-aplikace-201412';
                return false;">svetandroida.cz</a><br/>
            <a href="#" onclick="document.form.link1.value = 'http://zpravy.aktualne.cz/domaci/nahe-fotky-vydirani-kybersikana-se-rozmaha-i-v-cesku/r~63bee80485fd11e4bdad0025900fea04/';
                return false;">aktualne.cz</a><br/>
            <a href="#" onclick="document.form.link1.value = 'http://uk.businessinsider.com/isis-is-much-stronger-and-much-more-dangerous-than-people-realize-2014-12';
                return false;">businessinsider.com</a><br/>

        </p>
    </div></div>
<div class="container">
    <a class="center" href="<c:url value="preview"/>">
        <img src="images/design/preview.png" style="width: 75px; height: 75px;">
        <br/><fmt:message key="menu.vysledky" />
    </a>
</div>   

<div class="container">
    <a class="center" href="<c:url value="settings"/>">
        <img src="images/design/settings.png" style="width: 75px; height: 75px;">
        <br/><fmt:message key="menu.nastavenia" />
    </a>
</div>   

<div class="container">
    <a class="center" href="<c:url value="author"/>">
        <img src="images/design/author.png" style="width: 75px; height: 75px;">
        <br/><fmt:message key="menu.autor" />
    </a>
</div>   



<%@include file="../html/footer.html"%>     
