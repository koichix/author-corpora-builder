<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>  

<!--na serveri to zobrazuje z priecinka c:\Program Files\Apache Software Foundation\Apache Tomcat 8.0.9\bin\corpus\-->


<div class="containerHalfGreen">    
    Sem pôjde ešte detailný popis čo sme vlastne dosiahli touto aplikáciou.
    <br/>  <br/> 
    Aplikáciu si môžete stiahnuť na: <a href="https://github.com/koichix/author-corpora-builder">GIThube.</a>  
    <br/>
    Vybudovaný autorský korpus si môžete stiahnuť: <a href="getfile?name=corpus/author_corpus.zip">author_corpus.zip</a> 
    <br/> 

</div>

<div class="containerHalfBlack">
    <strong>Autorský korpus vybudovaný z 20 domén</strong>
    <br/>
    <a href="images/clanky1.png" target="_blank"><img src="images/clanky1.png" class="graf"/></a>     
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