<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Web Structure Detection</title>
        <link rel="stylesheet" href="<c:url value="/default.css"/>" type="text/css">
        <script src="<c:url value="/jquery/external/jquery/jquery.js"/>"></script>
        <script src="<c:url value="/jquery/jquery-ui.js"/>"></script>
        <link rel="stylesheet" href="<c:url value="/jquery/jquery-ui.css"/>">
        <script>
            $(function () {
                $(document).tooltip();
            });

            $(function () {              
                $("input[type=Submit]").button();
            });
            $(function () {
                $("#loading").dialog({
                    draggable: false,
                    dialogClass: 'no-close',
                    title: "Čakajte prosím",
                    autoOpen: false,
                    show: {
                        effect: "blind",
                        duration: 1000
                    },
                    hide: {
                        effect: "blind",
                        duration: 1000
                    }
                });

                $("#submit").click(function () {
                    $("#loading").dialog("open");
                });

                $("#analyze").click(function () {
                    $("#loading").dialog("open");
                });

            });


        </script>
        <style>
            .no-close .ui-dialog-titlebar-close {display: none }
            label {
                display: inline-block;
                width: 5em;
            }
        </style>

    </head>
    <body>
        <div class="telo">
            <h1>Web Structure Detection</h1>           
            <h2>Aplikácia na automatické extrahovanie informácií z internetových článkov</h2>
            <a href="<c:url value="/settings"/>" class="nastavenia" title="Nastavenia"></a>
            <br/>
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

            <div class="popis2"> <h4>Zadajte URL adresu webstránky:</h4>
                <form action="<c:url value="/input"/>" method="post" name="form" >

                    <table>
                        <tr>                  
                            <td><input type="text" name="link1" class="link"  title="Miesto pre vloženie url adresy." value="${inputForm.link1}"/></td>
                            <td><label><input type="checkbox" name="disc" title="Zaškrtnúť, v prípade že chceme analyzovať diskusie."  value="true"/>diskusia</label></td> <td><input type="Submit" value="Stiahni" title="Stiahne určený počet webstránok z danej domény." name="submit" id="submit"/></td> <td><input type="Submit" value="Analyzuj" id="analyze" title="Analyzuje stiahnuté dáta." name="analyze"/></td>
                        </tr>

                    </table> 
                </form>
                <div id="loading" style="display:none;"><img src="loading.gif" alt="loading" style="float:left"  /><p style="float:left;margin-top: 6px;margin-left: 2px">Načítavam</p></div>
                <br/>
                <h3>Vzorové URL adresy:</h3>
                <p class="vzor">
                    <a href="#" onclick="document.form.link1.value = 'http://www.cas.sk/clanok/303038/exfarmar-tibor-zhana-zamestnanie-nema-problem-vypytat-si-poriadny-plat-tu-je-jeho-inzerat.html';
                            return false;"> cas.sk </a>
                    <a href="#" onclick="document.form.link1.value = 'http://www.novinky.cz/domaci/357474-majitelum-starsich-domu-hrozi-ze-zaplati-za-merice-i-miliony.html';
                            return false;"> novinky.cz </a>
                    <a href="#" onclick="document.form.link1.value = 'http://www.svetandroida.cz/vanoce-hry-aplikace-201412';
                            return false;"> svetandroida.cz</a>
                    <a href="#" onclick="document.form.link1.value = 'http://uk.businessinsider.com/isis-is-much-stronger-and-much-more-dangerous-than-people-realize-2014-12';
                            return false;"> businessinsider.com </a>
                    <a href="#" onclick="document.form.link1.value = 'http://zpravy.aktualne.cz/domaci/nahe-fotky-vydirani-kybersikana-se-rozmaha-i-v-cesku/r~63bee80485fd11e4bdad0025900fea04/';
                            return false;"> aktualne.cz </a>
                </p>
            </div>
            <br/>
            <h3>Popis aplikácie</h3>
            <br/>
            <p class="popis">
                Aplikácia slúži na automatickú detekciu obsahu internetových článkov. Po zadaní url postupne prejde určený počet článkov (možnosť nastaviť v nastaveniach), a extrahuje z nich nadpis, autora, dátum a text článku. Výstupom je ZIP súbor, v ktorom sú XML súbory vytvorené na základe mena autora a k nemu priradené jeho články.                   
            </p>
            <br/>


            <h3>Ako používať aplikáciu (popis jednotlivých funkcií):</h3> <br/>  
            <p class="popis">
                <strong>Nastavenia</strong> - nastavenie počtu stiahnutých článkov, doby čakania medzi stiahnutím viacerých článkov a iných parametrov. <br/>
                <strong>Stiahni</strong> - aplikácia stiahne určený počet stránok, ktoré sú súčasťou obsahu zadanej webstránky a jej podstránok. V prípade potreby stiahnutia len článkov typu "diskusia/fórum/komentáre..", zaškrtneme políčko diskusia. <br/>
                <strong>Analyzuj</strong> - aplikácia analyzuje dáta, ktoré sme v predchádzajúcom kroku stiahli a následne vypíše výstup v podobe odkazov na konkrétne stránky/podstránky + extrahované dáta z nich, ktoré je možné stiahnuť. Všetky tieto dáta budú uložené v tabuľke.
            </p> 
            <br/>
            <br/>
            <hr/>

            <div class="footer">

                <h2>&COPY; Ján Švec | Design by: Mária Svateníková</h2>
            </div>
        </div>
    </body>
</html>
