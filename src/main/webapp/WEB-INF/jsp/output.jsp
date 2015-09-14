<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>  

<!--  <h3>Výstup (zoznam článkov spolu s extrahovanými dátami)</h3>
<br/>
 <div id="tabs">
      <ul> <li style="padding-top: 8px">Stránky: </li>
<c:forEach items="${outputs}" var="output" varStatus="loopStatus">                
    <c:if test="${loopStatus.index % 10 == 0}">
    <li><a href="#tabs-${loopStatus.index}"><fmt:formatNumber value="${(loopStatus.index/10)+1}" maxFractionDigits="0" /> </a></li>
    </c:if>

</c:forEach>
</ul>    

<c:forEach items="${outputs}" var="output" varStatus="loopStatus"> 
    <c:if test="${loopStatus.index % 10 == 0}">
        <div id="tabs-${loopStatus.index}">  
            <table><thead><td>Pôvodný článok</td><td>Extrahované dáta</td></thead>
        <c:forEach begin="${loopStatus.index}" end="${loopStatus.index+9}" varStatus="loop">
            <tr class="${loop.index % 2 == 0 ? 'odd' : 'even'}">  ${outputs[loop.index]}  </tr> 
        </c:forEach>
    </table>
</div>
    </c:if>
</c:forEach>
</div>
-->

<div class="containerThreeColumnBlack">

    <div class="outputTable">
        <div class="outputHeading"><span class="black"><fmt:message key="output" /></span> <fmt:message key="output.info" /> </div>
        <table><thead><td><fmt:message key="output.original" /></td><td><fmt:message key="output.extracted" /></td></thead>     
            <c:forEach items="${outputs}" var="output" varStatus="loopStatus">    
                <tr class="evenOdd">  ${output}  </tr>
            </c:forEach>
        </table>  
    </div>

    <!-- ${info} -->
    <fmt:message key="output.xml" /> <br/>
    <fmt:message key="output.download" /><strong> <c:forEach items="${zip}" var="zi"> ${zi}</c:forEach></strong>         

        <br/>
    </div>
<%@include file="../html/footer.html"%>   
