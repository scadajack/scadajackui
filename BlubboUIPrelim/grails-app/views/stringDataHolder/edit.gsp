

<%@ page import="org.blubbo.grails.data.StringDataHolder" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'stringDataHolder.label', default: 'StringDataHolder')}" />
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
            <g:hasErrors bean="${stringDataHolderInstance}">
            <div class="errors">
                <g:renderErrors bean="${stringDataHolderInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${stringDataHolderInstance?.id}" />
                <g:hiddenField name="version" value="${stringDataHolderInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="value"><g:message code="stringDataHolder.value.label" default="Value" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: stringDataHolderInstance, field: 'value', 'errors')}">
                                    <g:textField name="value" value="${stringDataHolderInstance?.value}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="maxSize"><g:message code="stringDataHolder.maxSize.label" default="Max Size" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: stringDataHolderInstance, field: 'maxSize', 'errors')}">
                                    <g:textField name="maxSize" value="${fieldValue(bean: stringDataHolderInstance, field: 'maxSize')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="timestamp"><g:message code="stringDataHolder.timestamp.label" default="Timestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: stringDataHolderInstance, field: 'timestamp', 'errors')}">
                                    <g:textField name="timestamp" value="${fieldValue(bean: stringDataHolderInstance, field: 'timestamp')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="valueClassName"><g:message code="stringDataHolder.valueClassName.label" default="Value Class" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: stringDataHolderInstance, field: 'valueClassName', 'errors')}">
                                    <g:textField name="valueClassName" value="${stringDataHolderInstance?.valueClassName}" />
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
