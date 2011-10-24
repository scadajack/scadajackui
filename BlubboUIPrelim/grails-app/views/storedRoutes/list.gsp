
<%@ page import="com.blubbo.grails.routing.StoredRoutes" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'storedRoutes.label', default: 'StoredRoutes')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        
        <div class="body" style='width:90%'>
            <h1 style='float:left'><g:message code="default.list.label" args="[entityName]" /></h1>
            <div style='clear:both'></div>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:render template='routeList' />
    </body>
</html>
