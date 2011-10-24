
<%@ page import="org.scadajack.grails.comm.RemotePollTarget" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'remotePollTarget.label', default: 'RemotePollTarget')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'remotePollTarget.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="enabled" title="${message(code: 'remotePollTarget.enabled.label', default: 'Enabled')}" />
                        
                            <g:sortableColumn property="operation" title="${message(code: 'remotePollTarget.operation.label', default: 'Operation')}" />
                        
                            <th><g:message code="remotePollTarget.pollConfiguration.label" default="Poll Configuration" /></th>
                        
                            <g:sortableColumn property="remoteAddress" title="${message(code: 'remotePollTarget.remoteAddress.label', default: 'Remote Address')}" />
                        
                            <g:sortableColumn property="tagId" title="${message(code: 'remotePollTarget.tagId.label', default: 'Tag Id')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${remotePollTargetInstanceList}" status="i" var="remotePollTargetInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${remotePollTargetInstance.id}">${fieldValue(bean: remotePollTargetInstance, field: "id")}</g:link></td>
                        
                            <td><g:formatBoolean boolean="${remotePollTargetInstance.enabled}" /></td>
                        
                            <td>${fieldValue(bean: remotePollTargetInstance, field: "operation")}</td>
                        
                            <td>${fieldValue(bean: remotePollTargetInstance, field: "pollConfiguration")}</td>
                        
                            <td>${fieldValue(bean: remotePollTargetInstance, field: "remoteAddress")}</td>
                        
                            <td>${fieldValue(bean: remotePollTargetInstance, field: "tagId")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${remotePollTargetInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
