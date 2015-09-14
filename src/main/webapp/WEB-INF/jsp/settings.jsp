<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../html/header.html"%>           

<c:if test="${not empty error}">
    <div class="ui-widget" >
        <div class="ui-state-highlight ui-corner-all" style="margin-top: 10px; padding:0.7em; margin-bottom: 5px">
            <p> <span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em; margin-top: 4px"></span><c:out escapeXml="false" value="${error}"/>
            </p>
        </div>
    </div>
    <br/>
</c:if>

<div class="containerThreeColumn">    
    <strong><fmt:message key="settings.heading" /></strong><br/><br/>
    <ul>       
        <li><strong><fmt:message key="settings.number" /></strong> - <fmt:message key="settings.number2" /></li>
        <li><strong><fmt:message key="settings.wait" /></strong> - <fmt:message key="settings.wait2" /></li>
        <li><strong><fmt:message key="settings.url" /></strong> - <fmt:message key="settings.url2" /></li>
        <li><strong><fmt:message key="settings.thresh" /></strong> - <fmt:message key="settings.thresh2" /></li>
        <li><strong><fmt:message key="settings.depth" /></strong> - <fmt:message key="settings.depth2" /></li>
    </ul>
</div>

<div class="containerThreeColumnBlack">      
    <form action="<c:url value="settings"/>" method="post">

        <c:if test="${not empty error}">
            <table>
                <tr>
                    <th><fmt:message key="settings.number" />:</th>
                    <td><input type="text" name="setting1" title="<fmt:message key="settings.number3" />" value="${settingsForm.setting1}"/></td>
                </tr>
                <tr>
                    <th><fmt:message key="settings.wait" />:</th>
                    <td><input type="text" name="setting2" title="<fmt:message key="settings.wait3" />" value="${settingsForm.setting2}"/></td>
                </tr>     
                <tr>
                    <th><fmt:message key="settings.url" />:</th>
                    <td><input type="text" name="setting3" title="<fmt:message key="settings.url3" />" value="${settingsForm.setting3}"/> <label><input type="checkbox" name="setting4" title="<fmt:message key="settings.url.on" />" id="checkbox1" value="${settingsForm.setting4}" value="${settingsForm.setting4}" <c:if test="${settingsForm.setting4}">checked="checked"</c:if>/> <fmt:message key="settings.url.on1" /> </label></td>
                    </tr>   
                    <tr>
                        <th><fmt:message key="settings.thresh" />:</th>
                    <td><input type="text" name="setting5" title="<fmt:message key="settings.thresh3" />" value="${settingsForm.setting5}"/></td>
                </tr>    

                <tr id="autoUpdate">
                    <th><fmt:message key="settings.depth" />:</th>
                    <td><input type="text" name="setting6" title="<fmt:message key="settings.depth3" />" value="${settingsForm.setting6}"/></td>
                </tr>    

            </table>        
        </c:if>
        <c:if test="${empty error}">
            <table>
                <tr>
                    <th><fmt:message key="settings.number" />:</th>
                    <td><input type="text" name="setting1" title="<fmt:message key="settings.number3" />" value="${settingsForm.setting1}"/></td>
                </tr>
                <tr>
                    <th><fmt:message key="settings.wait" />:</th>
                    <td><input type="text" name="setting2" title="<fmt:message key="settings.wait3" />" value="${settingsForm.setting2}"/></td>
                </tr>     
                <tr>
                    <th><fmt:message key="settings.url" />:</th>
                    <td><input type="text" name="setting3" title="<fmt:message key="settings.url3" />" value="${settingsForm.setting3}"/> <label><input type="checkbox" name="setting4" title="<fmt:message key="settings.url.on" />" id="checkbox1" value="${settingsForm.setting4}" value="${settingsForm.setting4}" <c:if test="${settingsForm.setting4}">checked="checked"</c:if>/> <fmt:message key="settings.url.on1" /> </label></td>
                    </tr>   
                    <tr>
                        <th><fmt:message key="settings.thresh" />:</th>
                    <td><input type="text" name="setting5" title="<fmt:message key="settings.thresh3" />" value="${settingsForm.setting5}"/></td>
                </tr>    

                <tr id="autoUpdate">
                    <th><fmt:message key="settings.depth" />:</th>
                    <td><input type="text" name="setting6" title="<fmt:message key="settings.depth3" />" value="${settingsForm.setting6}"/></td>
                </tr>    
            </table>              
        </c:if>
        <p align="center">
            <input type="Submit" value="" title="<fmt:message key="settings.save" />" name="submit" class="save_icon"/>    
        </p>
    </form>
</div>

<%@include file="../html/footer.html"%>   