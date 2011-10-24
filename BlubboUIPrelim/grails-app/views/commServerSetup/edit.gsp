

<%@ page import="org.scadajack.grails.comm.CommServerSetup" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'commServerSetup.label', default: 'CommServerSetup')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${commServerSetupInstance}">
            <div class="errors">
                <g:renderErrors bean="${commServerSetupInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${commServerSetupInstance?.id}" />
                <g:hiddenField name="version" value="${commServerSetupInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        	<tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="commServerSetup.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: commServerSetupInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${commServerSetupInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="commRoute"><g:message code="commServerSetup.commRoute.label" default="Comm Route" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: commServerSetupInstance, field: 'commRoute', 'errors')}">
                                    <g:select name="commRoute.id" from="${com.blubbo.grails.routing.StoredRoutes.list()}" optionKey="id" value="${commServerSetupInstance?.commRoute?.id}"  />
                                </td>
                            </tr>
                         
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="repoRoute"><g:message code="commServerSetup.repoRoute.label" default="Repo Route" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: commServerSetupInstance, field: 'repoRoute', 'errors')}">
                                    <g:select name="repoRoute.id" from="${com.blubbo.grails.routing.StoredRoutes.list()}" optionKey="id" value="${commServerSetupInstance?.repoRoute?.id}"  />
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
