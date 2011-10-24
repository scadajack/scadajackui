

<%@ page import="org.scadajack.grails.comm.RemotePollTarget" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'remotePollTarget.label', default: 'RemotePollTarget')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${remotePollTargetInstance}">
            <div class="errors">
                <g:renderErrors bean="${remotePollTargetInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="enabled"><g:message code="remotePollTarget.enabled.label" default="Enabled" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: remotePollTargetInstance, field: 'enabled', 'errors')}">
                                    <g:checkBox name="enabled" value="${remotePollTargetInstance?.enabled}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="operation"><g:message code="remotePollTarget.operation.label" default="Operation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: remotePollTargetInstance, field: 'operation', 'errors')}">
                                    <g:textField name="operation" value="${remotePollTargetInstance?.operation}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="pollConfiguration"><g:message code="remotePollTarget.pollConfiguration.label" default="Poll Configuration" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: remotePollTargetInstance, field: 'pollConfiguration', 'errors')}">
                                    <g:select name="pollConfiguration.id" from="${org.scadajack.grails.comm.PollingConfiguration.list()}" optionKey="id" value="${remotePollTargetInstance?.pollConfiguration?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="remoteAddress"><g:message code="remotePollTarget.remoteAddress.label" default="Remote Address" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: remotePollTargetInstance, field: 'remoteAddress', 'errors')}">
                                    <g:textField name="remoteAddress" value="${remotePollTargetInstance?.remoteAddress}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="tagId"><g:message code="remotePollTarget.tagId.label" default="Tag Id" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: remotePollTargetInstance, field: 'tagId', 'errors')}">
                                	<g:select name="tagId" from="${org.blubbo.grails.data.DataTag.list()}" optionKey="tagId" value="${remotePollTargetInstance?.tagId}"  />    
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
