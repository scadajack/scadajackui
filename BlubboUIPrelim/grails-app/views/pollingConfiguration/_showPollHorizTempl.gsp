<g:if test="${flash.message}">
<div class="message">${flash.message}</div>
</g:if>
<div class="dialog">
	<table style="border:0;padding:0;margin:0">
		<tbody>
			<tr>
				<td>
	    			<table>
                    	<tbody>
                    	
                    	<tr class="prop">
                            <td valign="top" class="name"><g:message code="pollingConfiguration.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pollingConfigurationInstance, field: "id")}</td>
                            
                        </tr>
                    	
                    	<tr class="prop">
                            <td valign="top" class="name"><g:message code="pollingConfiguration.name.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pollingConfigurationInstance, field: "name")}</td>
                            
                        </tr>
                      
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pollingConfiguration.commRoute.label" default="Comm Route" /></td>
                            
                            <td valign="top" class="value"><g:link controller="storedRoutes" action="show" id="${pollingConfigurationInstance?.commRoute?.id}">${pollingConfigurationInstance?.commRoute?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pollingConfiguration.repoRoute.label" default="Repo Route" /></td>
                            
                            <td valign="top" class="value"><g:link controller="storedRoutes" action="show" id="${pollingConfigurationInstance?.repoRoute?.id}">${pollingConfigurationInstance?.repoRoute?.encodeAsHTML()}</g:link></td>      
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pollingConfiguration.enabled.label" default="Enabled" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${pollingConfigurationInstance?.enabled}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pollingConfiguration.forceWriteDelta.label" default="Force Write Delta" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${pollingConfigurationInstance?.forceWriteDelta}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pollingConfiguration.refreshTagsBeforePoll.label" default="Refresh Tags Before Poll" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${pollingConfigurationInstance?.refreshTagsBeforePoll}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pollingConfiguration.period.label" default="Period" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pollingConfigurationInstance, field: "period")}</td>        
                        </tr>
                    
                    </tbody>
                </table>
	    </td>
	    <td style="width:50%" rowspan=2>
			<g:render template="/templates/propertyValueEditor" 
            			model="['tableId':'pollConfigProps',
            				    'domainId':pollingConfigurationInstance.id,
            				    'listAction':'pollConfigPropsList',
            				    'editAction':'pollConfigEditProps']"/>

	    </td>
	    </tr>
	    <tr>
	    	<td>
				<div class="buttons">
				    <g:form>
				        <g:hiddenField name="id" value="${pollingConfigurationInstance?.id}" />
				        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
				        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
				    </g:form>
				</div>	    	
	    	</td>   	
	    </tr>
	    </tbody>
    </table>
</div>
