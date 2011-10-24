
<%@ page import="org.scadajack.grails.comm.CommServerSetup" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'commServerSetup.label', default: 'CommServerSetup')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'commServerSetup.id.label', default: 'Id')}" />
                        
                            <th><g:message code="commServerSetup.commRoute.label" default="Comm Route" /></th>
                        
                            <g:sortableColumn property="configProperties" title="${message(code: 'commServerSetup.configProperties.label', default: 'Config Properties')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'commServerSetup.name.label', default: 'Name')}" />
                        
                            <th><g:message code="commServerSetup.repoRoute.label" default="Repo Route" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${commServerSetupInstanceList}" status="i" var="commServerSetupInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${commServerSetupInstance.id}">${fieldValue(bean: commServerSetupInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: commServerSetupInstance, field: "commRoute")}</td>
                        
                            <td>${fieldValue(bean: commServerSetupInstance, field: "configProperties")}</td>
                        
                            <td>${fieldValue(bean: commServerSetupInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: commServerSetupInstance, field: "repoRoute")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${commServerSetupInstanceTotal}" />
            </div>
        </div>
    </body>
</html>