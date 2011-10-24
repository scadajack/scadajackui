
<%@ page import="org.blubbo.grails.data.DataTag" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'dataTag.label', default: 'DataTag')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
<!--         
		<div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
 -->        
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
			</g:if>
			<div class="dialog">
			    <table>
			        <tbody>
			        
			        	<tr class="prop">
			                <td valign="top" class="name"><g:message code="dataTag.name.label" default="Name" /></td>                            
			                <td valign="top" class="value">${fieldValue(bean: dataTagInstance, field: "name")}</td>
			            </tr>
			            
			        	<tr class="prop">
			                <td valign="top" class="name"><g:message code="dataTag.dataHolder.value" default="Value" /></td>                            
			                <td valign="top" class="value">${fieldValue(bean: dataTagInstance.dataHolder, field: "value")}</td>  
			            </tr>
			            
			            <tr class="prop">
			                <td valign="top" class="name"><g:message code="dataTag.dataHolder.class" default="Type" /></td>                            
			                <td valign="top" class="value">${dataTagInstance?.dataHolder?.valueClass.getSimpleName()}</td>  
			            </tr>
			            
			            <tr class="prop">
			                <td valign="top" class="name"><g:message code="dataTag.metadata.label" default="Metadata" /></td>                            
			                <td valign="top" class="value">${fieldValue(bean: dataTagInstance, field: "metadata")}</td>                            
			            </tr>                    
			
			            <tr class="prop">
			                <td valign="top" class="name"><g:message code="dataTag.propertyEntries.label" default="Property Entries" /></td>                            
			                <td valign="top" class="value">${fieldValue(bean: dataTagInstance, field: "propertyEntries")}</td>                            
			            </tr>
			
			            <tr class="prop">
			                <td valign="top" class="name"><g:message code="dataTag.timestamp.label" default="Timestamp" /></td>                            
			                <td valign="top" class="value"><g:formatDate type="datetime" date="${new Date(dataTagInstance.timestamp)}"/></td>
			            </tr>                        
			
			            <tr class="prop">
			                <td valign="top" class="name"><g:message code="dataTag.repository.label" default="Repository" /></td>                            
			                <td valign="top" class="value"><g:link controller="dataRepository" action="show" id="${dataTagInstance?.repository?.id}">${dataTagInstance?.repository?.name}</g:link></td>
			            </tr>
			            
			        	<tr class="prop">
			                <td valign="top" class="name"><g:message code="dataTag.tagId.label" default="Tag Id" /></td>                          
			                <td valign="top" class="value">${fieldValue(bean: dataTagInstance, field: "tagId")}</td>  
			            </tr>
			            
			            <tr class="prop">
			                <td valign="top" class="name"><g:message code="dataTag.version.label" default="Version" /></td>                            
			                <td valign="top" class="value">${fieldValue(bean: dataTagInstance, field: "tagVersion")}</td>
			            </tr>
			        
			        </tbody>
			    </table>
			</div>
			<div class="buttons">
			    <g:form>
			        <g:hiddenField name="id" value="${dataTagInstance?.id}" />
			        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
			        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
			    </g:form>
			</div>
        </div>
    </body>
</html>
