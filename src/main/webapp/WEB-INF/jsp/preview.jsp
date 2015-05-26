<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>  

<!--na serveri to zobrazuje z priecinka c:\Program Files\Apache Software Foundation\Apache Tomcat 8.0.9\bin\corpus\-->
<h3>Preview</h3>
<a href="<c:url value="/input"/>" class="back_icon"  title="back"></a><br/><br/>
<div class="popis">
    <h2>Autorský korpus z 20 českých a slovenských domén</h2>

    <div class="left_half">
        Success rate of the first 10 sites:<a href="/images/clanky1.png" target="_blank"><img src="/images/clanky1.png" class="graf"/></a> 
    </div>
    <div class="right_half">
        Success rate of next 10 sites: <a href="/images/clanky2.png" target="_blank"><img src="/images/clanky2.png" class="graf"/></a>
    </div>
   <br class="clear"/>
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

<br class="clear"/>
<br/>
<%@include file="../html/footer.html"%>     