

<%@ page import="org.blubbo.grails.data.DataTag" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'dataTag.label', default: 'DataTag')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>     
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${dataTagInstance}">
            <div class="errors">
                <g:renderErrors bean="${dataTagInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:render template='createForm' />
        </div>
    </body>
</html>
