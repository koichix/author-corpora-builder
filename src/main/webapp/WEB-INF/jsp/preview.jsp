<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>  

<h1><a href="<c:url value="/input"/>">Authorship corpora builder</a></h1>     
<h2>Aplikácia na automatické extrahovanie informácií z internetových článkov</h2>
<br/>
<!--na serveri to zobrazuje z priecinka c:\Program Files\Apache Software Foundation\Apache Tomcat 8.0.9\bin\corpus\-->
<div class="popis">
    <fmt:message key="preview"/>    <a href="/getfile?name=corpus\author_corpus.zip">author_corpus.zip</a>
</div>
<br>

<div class="left_column">  File manager:<br/>
    <a id="showall">Show all</a>  <a  id="hideall">Hide all</a>
    <br/><br/>
    <c:forEach items="${corpusFiles}" var="link"> 
        ${link}  
    </c:forEach>
</div>
<div class="right_column">
    Actual XML file:<br/>
    ${xml}
</div>


<p align="center" class="clear"><a href="<c:url value="/input"/>" class="jq">Späť</a></p>
<br>

<%@include file="../html/footer.html"%>     