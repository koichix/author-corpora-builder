<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>  
<div class="containerHalfBlack">
    <div class="center">  
        <strong>
        Popis aplikácie <img src="images/design/right.png" style="width: 45px;height: 45px; margin-bottom: -1.25em; margin-left: 5px;"><br/>
        </strong>
        
    </div>

</div>
<div class="containerHalfGreen">
    <p>  
        <strong>Aplikácia na automatické extrahovanie informácií z internetových článkov</strong><br/><br/>
        Aplikácia slúži na automatickú detekciu obsahu internetových článkov. Po zadaní url postupne prejde určený počet článkov (možnosť nastaviť v nastaveniach), a extrahuje z nich nadpis, autora, dátum a text článku. Výstupom je ZIP súbor, v ktorom sú XML súbory vytvorené na základe mena autora a k nemu priradené jeho články.                   
    </p>
</div>

<p class="containerHalfGreen">
    <strong>Spustenie aplikácie</strong> - aplikácia automaticky stiahne a analyzuje stránky vrámci domény zadanej v poli URL. Následne analyzuje dáta, a výsledok vypíše do tabuľky. Výsledné dáta je možné stiahnuť jednotlivo alebo ako zip súbor.<br/>
    <strong>Nastavenia</strong> - nastavenie počtu stiahnutých článkov, doby čakania medzi stiahnutím viacerých článkov a iných parametrov. <br/>
    <strong>Výsledky</strong> - podstránka obsahuje ukážkové dáta, ktoré je možno online prezerať, prípadne stiahnuť ako zip súbor. 
</p> 
<div class="containerHalfBlack">
    <div class="center">
              <img src="images/design/left.png" style="width: 45px;height: 45px; margin-bottom: -1.2em; margin-right:  5px;">
      <strong>
     Návod ako používať aplikáciu<br/>
        </strong>
       </div>

</div>

<%@include file="../html/footer.html"%>   
