<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>  
<h1><a href="<c:url value="/input"/>">Authorship corpora builder</a></h1>     
<h2>Aplikácia na automatické extrahovanie informácií z internetových článkov</h2>
<br/>

<div class="popis2">             
    ${info} Súbor s autormi je možné stiahnuť: <c:forEach items="${zip}" var="zi"> ${zi}</c:forEach>         
    </div> <br/>
    <h3>Zoznam článkov spolu s výsledými xml súbormi</h3>
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

<br/>
<p align="center"><a href="<c:url value="/input"/>" class="jq">Späť</a></p>
<br>

<%@include file="../html/footer.html"%>   
