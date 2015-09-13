<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>  

<!--na serveri to zobrazuje z priecinka c:\Program Files\Apache Software Foundation\Apache Tomcat 8.0.9\bin\corpus\-->


<div class="containerHalfGreen"> 
   
    Aplikácia bola otestovaná na 20 Českých a Slovenských doménach, z ktorých sa náhodne vybralo 500 čánkov. Priemerná úspešnosť nášho algoritmu je 90,68%. 
    Vybudovali sme korpus určený na štýlometrický výskum. Vybudovaný korpus obsahuje okrem samotného článku aj doplnkové informácie: meno autora, nadpis a dátum vyvorenia.
    
    
    <br/>  <br/> 
    Aplikáciu si môžete stiahnuť na: <a href="https://github.com/koichix/author-corpora-builder"  target="_blank">GIThube.</a>  
    <br/>
    Vybudovaný autorský korpus si môžete stiahnuť: <a href="getfile?name=corpus/author_corpus.zip">author_corpus.zip</a> 
    <br/> 

</div>

<div class="containerHalfBlack">
    <strong>Autorský korpus vybudovaný z 20 domén</strong>
    <br/>
    <a href="images/clanky1.png" target="_blank"><img src="images/clanky1.png" class="graf" style="margin-top: 10px"/></a>     
    <a href="images/clanky2.png" target="_blank"><img src="images/clanky2.png" class="graf"/></a>

    <br class="clear"/>
</div>
<br>
<div class="containerThreeColumnBlack">
    <!--
    <div class="left_column"><div class="nadpis">Správca súborov</div><br/>
        <a id="showall">Show all</a>  <a  id="hideall">Hide all</a>
        <br/><br/>
        <c:forEach items="${corpusFiles}" var="link"> 
            ${link}  
        </c:forEach>        
    </div>
    -->
    <div class="left_column"><div class="nadpis">Správca súborov</div><br/>
        <div id="fileTree"></div>
    </div>
    <div class="right_column"><div class="nadpis">Náhľad vybraného XML súboru</div><br/>
        ${xml}
    </div>
</div>
<%@include file="../html/footer.html"%>     