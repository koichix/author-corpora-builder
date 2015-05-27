<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>  
<h3>Ako používať aplikáciu (popis jednotlivých funkcií):</h3>
<a href="<c:url value="input"/>" class="back_icon"  title="back"></a><br/><br/>
<p class="popis">
    <strong>Nastavenia</strong> - nastavenie počtu stiahnutých článkov, doby čakania medzi stiahnutím viacerých článkov a iných parametrov. <br/>
    <strong>Stiahni</strong> - aplikácia stiahne určený počet stránok, ktoré sú súčasťou obsahu zadanej webstránky a jej podstránok. V prípade potreby stiahnutia len článkov typu "diskusia/fórum/komentáre..", zaškrtneme políčko diskusia. <br/>
    <strong>Analyzuj</strong> - aplikácia analyzuje dáta, ktoré sme v predchádzajúcom kroku stiahli a následne vypíše výstup v podobe odkazov na konkrétne stránky/podstránky + extrahované dáta z nich, ktoré je možné stiahnuť. Všetky tieto dáta budú uložené v tabuľke.
</p> 
<br/> 

<%@include file="../html/footer.html"%>   
