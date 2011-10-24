

<%@ page import="com.blubbo.grails.routing.StoredRoutes" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'storedRoutes.label', default: 'StoredRoutes')}" />
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
            <g:hasErrors bean="${storedRoutesInstance}">
            <div class="errors">
                <g:renderErrors bean="${storedRoutesInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="enable"><g:message code="storedRoutes.enable.label" default="Enable" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: storedRoutesInstance, field: 'enable', 'errors')}">
                                    <g:checkBox name="enable" value="${storedRoutesInstance?.enable}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="storedRoutes.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: storedRoutesInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${storedRoutesInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="routeDescription"><g:message code="storedRoutes.routeDescription.label" default="Route Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: storedRoutesInstance, field: 'routeDescription', 'errors')}">
                                    <g:textField name="routeDescription" value="${storedRoutesInstance?.routeDescription}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="targetUrl"><g:message code="storedRoutes.targetUrl.label" default="Target Url" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: storedRoutesInstance, field: 'targetUrl', 'errors')}">
                                    <g:textField name="targetUrl" value="${storedRoutesInstance?.targetUrl}" />
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
