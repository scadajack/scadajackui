
<%@ page import="com.blubbo.grails.data.MetadataEntry" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'metadataEntry.label', default: 'MetadataEntry')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'metadataEntry.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="key" title="${message(code: 'metadataEntry.key.label', default: 'Key')}" />
                        
                            <g:sortableColumn property="value" title="${message(code: 'metadataEntry.value.label', default: 'Value')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${metadataEntryInstanceList}" status="i" var="metadataEntryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${metadataEntryInstance.id}">${fieldValue(bean: metadataEntryInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: metadataEntryInstance, field: "key")}</td>
                        
                            <td>${fieldValue(bean: metadataEntryInstance, field: "value")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${metadataEntryInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
