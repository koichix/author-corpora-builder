<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>  

<!--na serveri to zobrazuje z priecinka c:\Program Files\Apache Software Foundation\Apache Tomcat 8.0.9\bin\corpus\-->


<div class="containerHalfGreen"> 

    <fmt:message key="preview.info" />    

    <br/> <br/> 
    <fmt:message key="preview.download" /> <a href="https://github.com/koichix/author-corpora-builder"  target="_blank">GIThub.</a>  
    <br/>
    <fmt:message key="preview.download2" /> <a href="getfile?name=corpus/author_corpus_v1.zip">version_1.zip</a>,  
    <a href="getfile?name=corpus/author_corpus_v2.zip">version_2.zip</a> 
    <br/> 

</div>

<div class="containerHalfBlack">
    <strong><fmt:message key="preview.graph" /></strong>
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
    <div class="left_column"><div class="nadpis"><fmt:message key="preview.manager" /></div><br/>
        <div id="fileTree"></div>
    </div>
    <div class="right_column"><div class="nadpis"><fmt:message key="preview.file" /></div><br/>
        ${xml}
    </div>
</div>
<%@include file="../html/footer.html"%>     