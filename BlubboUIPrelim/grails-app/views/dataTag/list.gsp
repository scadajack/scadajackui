
<%@ page import="org.blubbo.grails.data.DataTag" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'dataTag.label', default: 'DataTag')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="body" style="width:90%">
            <h1 style='float:left'><g:message code="default.list.label" args="[entityName]" /></h1>
            <div style='clear:both'></div>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div style='width:90%'>
        		<g:render template='dataTagList'/>
        	</div>
        </div>
        <div style='clear:both'></div>
        
    </body>
</html>
