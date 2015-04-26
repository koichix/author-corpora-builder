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
                $("input[type=Submit]").button();
            });
            $(document).ready(function () {
                $('#checkbox1').change(function () {
                    if (this.checked)
                        $('#autoUpdate').fadeOut('slow');
                    else
                        $('#autoUpdate').fadeIn('slow');

                });
                $('#checkbox1').change(function () {
                    if (this.checked)
                        $('#autoUpdate').fadeOut('slow');
                    else
                        $('#autoUpdate').fadeIn('slow');

                });

                if ($("#checkbox1").is(':checked'))
                    $("#autoUpdate").hide();  // checked
                else
                    $("#autoUpdate").show();  // unchecked
            });
        </script>
        <style>
            label {
                display: inline-block;
                width: 5em;
            }
        </style>


    </head>
    <body>
        <div class="telo">
            <h1>Authorship corpora builder</h1>
            <h2>Aplikácia na automatické extrahovanie informácií z internetových článkov</h2>
            <br/>            

            <c:if test="${not empty error}">
                <div class="ui-widget" >
                    <div class="ui-state-highlight ui-corner-all" style="margin-top: 10px; padding:0.7em; margin-bottom: 5px">
                        <p> <span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em; margin-top: 4px"></span><c:out escapeXml="false" value="${error}"/>
                        </p>
                    </div>
                </div>
                <br/>
            </c:if>

            <div class="popis2">      
                <form action="<c:url value="/settings"/>" method="post">

                    <c:if test="${not empty error}">
                        <table>
                            <tr>
                                <th>Počet stránok:</th>
                                <td><input type="text" name="setting1" title="Počet stránok na stiahnutie [celé číslo]." value="${settingsForm.setting1}"/></td>
                            </tr>
                            <tr>
                                <th>Doba čakania (politeness):</th>
                                <td><input type="text" name="setting2" title="Doba čakania medzi stiahnutím viacerých stránok [milisekundy]." value="${settingsForm.setting2}"/></td>
                            </tr>     
                            <tr>
                                <th>Vlastné URL:</th>
                                <td><input type="text" name="setting3" title="Filter URL adresy [URL adresa]." value="${settingsForm.setting3}"/> <label><input type="checkbox" name="setting4" title="Zapnúť používanie vlastného URL." id="checkbox1" value="${settingsForm.setting4}" value="${settingsForm.setting4}" <c:if test="${settingsForm.setting4}">checked="checked"</c:if>/> Zapnúť </label></td>
                                </tr>   
                                <tr>
                                    <th>Prahová hodnota:</th>
                                    <td><input type="text" name="setting5" title="Prahová hodnota pre SST [desatinné číslo medzi 0 až 1]." value="${settingsForm.setting5}"/></td>
                            </tr>    

                            <tr id="autoUpdate">
                                <th>Hĺbka zanorenia crawleru:</th>
                                <td><input type="text" name="setting6" title="Hĺbka zanorenia crawleru [celé číslo]." value="${settingsForm.setting6}"/></td>
                            </tr>    

                        </table>        
                    </c:if>
                    <c:if test="${empty error}">
                        <table>
                            <tr>
                                <th>Počet stránok:</th>
                                <td><input type="text" name="setting1" title="Počet stránok na stiahnutie [celé číslo]." value="${settingsForm.setting1}"/></td>
                            </tr>
                            <tr>
                                <th>Doba čakania (politeness):</th>
                                <td><input type="text" name="setting2" title="Doba čakania medzi stiahnutím viacerých stránok [milisekundy]." value="${settingsForm.setting2}"/></td>
                            </tr>     
                            <tr>
                                <th>Vlastné URL:</th>
                                <td><input type="text" name="setting3" title="Filter URL adresy [URL adresa]." value="${settingsForm.setting3}"/> <label><input type="checkbox" name="setting4" title="Zapnúť používanie vlastného URL." id="checkbox1" value="${settingsForm.setting4}" value="${settingsForm.setting4}" <c:if test="${settingsForm.setting4}">checked="checked"</c:if>/> Zapnúť </label></td>
                                </tr>   
                                <tr>
                                    <th>Prahová hodnota:</th>
                                    <td><input type="text" name="setting5" title="Prahová hodnota pre SST [desatinné číslo medzi 0 až 1]." value="${settingsForm.setting5}"/></td>
                            </tr>    

                            <tr id="autoUpdate">
                                <th>Hĺbka zanorenia crawleru:</th>
                                <td><input type="text" name="setting6" title="Hĺbka zanorenia crawleru [celé číslo]." value="${settingsForm.setting6}"/></td>
                            </tr>    

                        </table>              
                    </c:if>

            </div> <br>
            <p align="center">
                <input type="Submit" value="Uložiť" name="submit"/>
                <input type="Submit" value="Späť" name="cancel"/>
            </p>
        </form>
        <br/>
        <h3>Popis jednotlivých nastavení:</h3> <br/>  
        <ul class="popis">
            <li><strong>Počet stránok</strong> - nastavenie počtu stiahnutých článkov. </li>
            <li><strong>Doba čakania (politeness)</strong> - nastavenie doby čakania medzi stiahnutím viacerých stránok v milisekundách. </li>
            <li><strong>Vlastné URL</strong> - nastavenie filtra URL adresy, pomocou ktorého bude filtrovať sťahované stránky.</li>
            <li><strong>Prahová hodnota</strong> - nastavenie váhy prahovej hodnoty pre čistenie stránky od boilerplate pomocou SST.</li>
            <li><strong>Hĺbka zanorenia crawleru</strong> - celé číslo od 1 do N symbolizuje hĺbku zanorenia crawleru vrámci URL adresy. Napr.: na stránke "kultura.sme.sk/clanok1" je hĺbka zanorenia 1 časť URL adresy "sme.sk". Hĺbka zanorenia -1 znamená, že sa pôjde do maximálnej hĺbky URL.</li>
        </ul>

        <br><br>
        <hr/>
        <div class="footer">

            <h2>&COPY; Ján Švec | Design by: Mária Svateníková </h2>
        </div>
    </div>
</body>
</html>
