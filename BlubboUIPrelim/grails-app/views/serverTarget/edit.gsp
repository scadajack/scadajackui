

<%@ page import="org.scadajack.grails.comm.ServerTarget" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'serverTarget.label', default: 'ServerTarget')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${serverTargetInstance}">
            <div class="errors">
                <g:renderErrors bean="${serverTargetInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${serverTargetInstance?.id}" />
                <g:hiddenField name="version" value="${serverTargetInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="enabled"><g:message code="serverTarget.enabled.label" default="Enabled" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: serverTargetInstance, field: 'enabled', 'errors')}">
                                    <g:checkBox name="enabled" value="${serverTargetInstance?.enabled}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="address"><g:message code="serverTarget.address.label" default="Address" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: serverTargetInstance, field: 'address', 'errors')}">
                                    <g:textField name="address" value="${serverTargetInstance?.address}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="operation"><g:message code="serverTarget.operation.label" default="Operation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: serverTargetInstance, field: 'operation', 'errors')}">
              						<g:textField name="operation" value="read-write" readonly="true" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="serverSetup"><g:message code="serverTarget.serverSetup.label" default="Server Setup" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: serverTargetInstance, field: 'serverSetup', 'errors')}">
                                    <g:select name="serverSetup.id" from="${org.scadajack.grails.comm.CommServerSetup.list()}" optionKey="id" optionValue="name" value="${serverTargetInstance?.serverSetup?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="tagId"><g:message code="serverTarget.tagId.label" default="Tag Id" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: serverTargetInstance, field: 'tagId', 'errors')}">
                                    <g:select name="tagId" from="${org.blubbo.grails.data.DataTag.list()}" optionKey="tagId" optionValue="name" value="${serverTargetInstance?.tagId}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
