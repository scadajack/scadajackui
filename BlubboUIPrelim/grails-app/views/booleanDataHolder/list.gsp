
<%@ page import="org.blubbo.grails.data.BooleanDataHolder" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'booleanDataHolder.label', default: 'BooleanDataHolder')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'booleanDataHolder.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="timestamp" title="${message(code: 'booleanDataHolder.timestamp.label', default: 'Timestamp')}" />
                        
                            <g:sortableColumn property="value" title="${message(code: 'booleanDataHolder.value.label', default: 'Value')}" />
                        
                            <g:sortableColumn property="valueClassName" title="${message(code: 'booleanDataHolder.valueClassName.label', default: 'Value Class')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${booleanDataHolderInstanceList}" status="i" var="booleanDataHolderInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${booleanDataHolderInstance.id}">${fieldValue(bean: booleanDataHolderInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: booleanDataHolderInstance, field: "timestamp")}</td>
                        
                            <td><g:formatBoolean boolean="${booleanDataHolderInstance.value}" /></td>
                        
                            <td>${fieldValue(bean: booleanDataHolderInstance, field: "valueClassName")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${booleanDataHolderInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
