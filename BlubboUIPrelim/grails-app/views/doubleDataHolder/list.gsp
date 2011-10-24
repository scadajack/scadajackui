
<%@ page import="org.blubbo.grails.data.DoubleDataHolder" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'doubleDataHolder.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="value" title="${message(code: 'doubleDataHolder.value.label', default: 'Value')}" />
                        
                            <g:sortableColumn property="minValue" title="${message(code: 'doubleDataHolder.minValue.label', default: 'Min Value')}" />
                        
                        	<g:sortableColumn property="maxValue" title="${message(code: 'doubleDataHolder.maxValue.label', default: 'Max Value')}" />
                        
                            <g:sortableColumn property="timestamp" title="${message(code: 'doubleDataHolder.timestamp.label', default: 'Timestamp')}" />
                        
                            <g:sortableColumn property="valueClassName" title="${message(code: 'doubleDataHolder.valueClassName.label', default: 'Value Class')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${doubleDataHolderInstanceList}" status="i" var="doubleDataHolderInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${doubleDataHolderInstance.id}">${fieldValue(bean: doubleDataHolderInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: doubleDataHolderInstance, field: "value")}</td>
                        
                        	<td>${fieldValue(bean: doubleDataHolderInstance, field: "minValue")}</td>
                        
                        	<td>${fieldValue(bean: doubleDataHolderInstance, field: "maxValue")}</td>
                        
                            <td>${fieldValue(bean: doubleDataHolderInstance, field: "timestamp")}</td>
                        
                            <td>${fieldValue(bean: doubleDataHolderInstance, field: "valueClassName")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${doubleDataHolderInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
