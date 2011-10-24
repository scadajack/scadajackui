
<%@ page import="com.blubbo.grails.routing.StoredRoutes" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'storedRoutes.label', default: 'StoredRoutes')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="storedRoutes.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: storedRoutesInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="storedRoutes.enable.label" default="Enable" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${storedRoutesInstance?.enable}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="storedRoutes.name.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: storedRoutesInstance, field: "name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="storedRoutes.routeDescription.label" default="Route Description" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: storedRoutesInstance, field: "routeDescription")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="storedRoutes.targetUrl.label" default="Target Url" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: storedRoutesInstance, field: "targetUrl")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${storedRoutesInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
