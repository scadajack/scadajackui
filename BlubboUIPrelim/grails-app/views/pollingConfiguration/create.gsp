

<%@ page import="org.scadajack.grails.comm.PollingConfiguration" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'pollingConfiguration.label', default: 'PollingConfiguration')}" />
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
            <g:hasErrors bean="${pollingConfigurationInstance}">
            <div class="errors">
                <g:renderErrors bean="${pollingConfigurationInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:render template='pollingConfigEditForm' />
<!-- 		<g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="commRoute"><g:message code="pollingConfiguration.commRoute.label" default="Comm Route" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pollingConfigurationInstance, field: 'commRoute', 'errors')}">
                                    <g:select name="commRoute.id" from="${com.blubbo.grails.routing.StoredRoutes.list()}" optionKey="id" value="${pollingConfigurationInstance?.commRoute?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="repoRoute"><g:message code="pollingConfiguration.repoRoute.label" default="Repo Route" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pollingConfigurationInstance, field: 'repoRoute', 'errors')}">
                                    <g:select name="repoRoute.id" from="${com.blubbo.grails.routing.StoredRoutes.list()}" optionKey="id" value="${pollingConfigurationInstance?.repoRoute?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="configProperties"><g:message code="pollingConfiguration.configProperties.label" default="Config Properties" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pollingConfigurationInstance, field: 'configProperties', 'errors')}">
                                    
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="enabled"><g:message code="pollingConfiguration.enabled.label" default="Enabled" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pollingConfigurationInstance, field: 'enabled', 'errors')}">
                                    <g:checkBox name="enabled" value="${pollingConfigurationInstance?.enabled}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="forceWriteDelta"><g:message code="pollingConfiguration.forceWriteDelta.label" default="Force Write Delta" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pollingConfigurationInstance, field: 'forceWriteDelta', 'errors')}">
                                    <g:checkBox name="forceWriteDelta" value="${pollingConfigurationInstance?.forceWriteDelta}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="pollingConfiguration.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pollingConfigurationInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${pollingConfigurationInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="period"><g:message code="pollingConfiguration.period.label" default="Period" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pollingConfigurationInstance, field: 'period', 'errors')}">
                                    <g:textField name="period" value="${fieldValue(bean: pollingConfigurationInstance, field: 'period')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="refreshTagsBeforePoll"><g:message code="pollingConfiguration.refreshTagsBeforePoll.label" default="Refresh Tags Before Poll" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pollingConfigurationInstance, field: 'refreshTagsBeforePoll', 'errors')}">
                                    <g:checkBox name="refreshTagsBeforePoll" value="${pollingConfigurationInstance?.refreshTagsBeforePoll}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
 -->
        </div>
    </body>
</html>
