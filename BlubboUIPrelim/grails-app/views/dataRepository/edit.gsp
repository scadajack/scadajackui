

<%@ page import="org.scadajack.grails.repository.DataRepository" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'dataRepository.label', default: 'DataRepository')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="body" style="width:90%">
        	<div style="float:left;margin-right:170px">
            <h1><g:message code="default.edit.label" args="[entityName]"/></h1>
            </div>
            <button class="menuButton" style="float:left;height:25px;margin:1em 0.2em 0.2em 0.2em" type="submit">
            	<span class="button" ><g:link class="list" action="list" style='margin:0.4em 0'>List</g:link></span>
            </button>
            <div style="clear:both"></div>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${dataRepositoryInstance}">
            <div class="errors">
                <g:renderErrors bean="${dataRepositoryInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${dataRepositoryInstance?.id}" />
                <g:hiddenField name="version" value="${dataRepositoryInstance?.version}" />
                <div class="dialog" style="float:left;margin-right:10px;margin-bottom:10px">
                    <table>
                        <tbody>
                        	<tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="dataRepository.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dataRepositoryInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${dataRepositoryInstance?.name}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="dataRepository.uid.label" default="UID" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dataRepositoryInstance, field: 'uid', 'errors')}">
                                    <g:textField name="name" value="${dataRepositoryInstance?.uid}" size='40'/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="repoRoute"><g:message code="dataRepository.repoRoute.label" default="Repo Route" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dataRepositoryInstance, field: 'repoRoute', 'errors')}">
                                    <g:select name="repoRoute.id" 
                                    			from="${com.blubbo.grails.routing.StoredRoutes.list()}" 
                                    			optionKey="id" 
                                    			value="${dataRepositoryInstance?.repoRoute?.id}"
                                    			noSelection="['null':'none']"></g:select>
                                    <g:checkBox name="autoGenerateRoute" value="${true}"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    
                </div>
                <div style="float:left;min-width:600px;">
                    	<g:render template="/dataTag/dataTagBriefList" />
			            <script type='text/javascript'>
			            	$(document).ready(function(){
			            		jQuery('#dataRepositoryBriefList').jqGrid({
			            				url: "/BlubboUIPrelim/dataTag/dataTagList",			     
			            				addFunc: openTagSelectorDialog
			            		}).hideCol('repository')
			            		  .setCaption("REPOSITORY DATA TAGS");
			                });
			            	
							
			            </script>
                    </div>
                <div style="clear:both"></div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="removeAllTags" value="${message(code: 'repository.remove.all.label', default: 'Clear Tags')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>

    </body>
</html>
