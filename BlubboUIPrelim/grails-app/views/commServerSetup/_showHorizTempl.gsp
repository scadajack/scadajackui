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
	                <td valign="top" class="name"><g:message code="commServerSetup.name.label" default="Name" /></td>
	                
	                <td valign="top" class="value">${fieldValue(bean: commServerSetupInstance, field: "name")}</td>
	                
	            </tr>
	        
	            <tr class="prop">
	                <td valign="top" class="name"><g:message code="commServerSetup.commRoute.label" default="Comm Route" /></td>
	                
	                <td valign="top" class="value"><g:link controller="storedRoutes" action="show" id="${commServerSetupInstance?.commRoute?.id}">${commServerSetupInstance?.commRoute?.encodeAsHTML()}</g:link></td>
	                
	            </tr>
	            
	            <tr class="prop">
	                <td valign="top" class="name"><g:message code="commServerSetup.repoRoute.label" default="Repo Route" /></td>
	                
	                <td valign="top" class="value"><g:link controller="storedRoutes" action="show" id="${commServerSetupInstance?.repoRoute?.id}">${commServerSetupInstance?.repoRoute?.encodeAsHTML()}</g:link></td>
	                
	            </tr>
	        
	        </tbody>
	    </table>
	    </td>
	    <td style="width:50%" rowspan=2>
			<g:render template="/templates/propertyValueEditor" 
            			model="['tableId':'commServerProps',
            				    'domainId':commServerSetupInstance.id,
            				    'listAction':'commServerPropsList',
            				    'editAction':'commServerEditProps']"/>

	    </td>
	    </tr>
	    <tr>
	    	<td>
				<div class="buttons">
				    <g:form>
				        <g:hiddenField name="id" value="${commServerSetupInstance?.id}" />
				        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
				        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
				    </g:form>
				</div>	    	
	    	</td>
<!-- 	    	
             <td>
				<div class="buttons">
				    <g:form>
				        <g:hiddenField name="id" value="${commServerSetupInstance?.id}" />
				        <span class="button"><g:actionSubmit class="create" action="create" value="New"><g:message code="default.new.label" args="[' ']" /></g:actionSubmit></span>
				        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
				    </g:form>
				</div>	    	
	    	</td>
 -->	    	
	    </tr>
	    </tbody>
    </table>
</div>
