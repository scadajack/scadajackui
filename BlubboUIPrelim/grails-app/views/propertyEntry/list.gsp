
<%@ page import="com.blubbo.grails.data.PropertyEntry" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'propertyEntry.label', default: 'PropertyEntry')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'propertyEntry.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="key" title="${message(code: 'propertyEntry.key.label', default: 'Key')}" />
                        
                            <g:sortableColumn property="value" title="${message(code: 'propertyEntry.value.label', default: 'Value')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${propertyEntryInstanceList}" status="i" var="propertyEntryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${propertyEntryInstance.id}">${fieldValue(bean: propertyEntryInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: propertyEntryInstance, field: "key")}</td>
                        
                            <td>${fieldValue(bean: propertyEntryInstance, field: "value")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${propertyEntryInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
