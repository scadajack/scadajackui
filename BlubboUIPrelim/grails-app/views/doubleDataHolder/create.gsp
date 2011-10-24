

<%@ page import="org.blubbo.grails.data.DoubleDataHolder" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder')}" />
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
            <g:hasErrors bean="${doubleDataHolderInstance}">
            <div class="errors">
                <g:renderErrors bean="${doubleDataHolderInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="value"><g:message code="doubleDataHolder.value.label" default="Value" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: doubleDataHolderInstance, field: 'value', 'errors')}">
                                    <g:textField name="value" value="${fieldValue(bean: doubleDataHolderInstance, field: 'value')}" />
                                </td>
                            </tr>
                        
                        	<tr class="prop">
                                <td valign="top" class="name">
                                    <label for="minValue"><g:message code="doubleDataHolder.minValue.label" default="Min Value" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: doubleDataHolderInstance, field: 'minValue', 'errors')}">
                                    <g:textField name="minValue" value="${fieldValue(bean: doubleDataHolderInstance, field: 'minValue')}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="maxValue"><g:message code="doubleDataHolder.maxValue.label" default="Max Value" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: doubleDataHolderInstance, field: 'maxValue', 'errors')}">
                                    <g:textField name="maxValue" value="${fieldValue(bean: doubleDataHolderInstance, field: 'maxValue')}" />
                                </td>
                            </tr>
                            
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="timestamp"><g:message code="doubleDataHolder.timestamp.label" default="Timestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: doubleDataHolderInstance, field: 'timestamp', 'errors')}">
                                    <g:textField name="timestamp" value="${fieldValue(bean: doubleDataHolderInstance, field: 'timestamp')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="valueClassName"><g:message code="doubleDataHolder.valueClassName.label" default="Value Class" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: doubleDataHolderInstance, field: 'valueClassName', 'errors')}">
                                    <g:textField name="valueClassName" value="${doubleDataHolderInstance?.valueClassName}" />
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
