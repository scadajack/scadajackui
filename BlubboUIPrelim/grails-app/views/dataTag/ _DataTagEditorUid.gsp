<!-- Version of dataTagEditor that uses UUID for id -->
<g:form method="post" >
   <g:hiddenField name="id" value="${dataTagInstanceId}" />
   <g:hiddenField name="name" value="${dataTagInstance?.name}" />
   <g:hiddenField name="timestamp" value="${dataTagInstance?.timestamp}" />
   <g:hiddenField name="version" value="${dataTagInstance?.version}" />
   <g:hiddenField name="tagId" value="${dataTagInstance?.tagId}" />
   <g:hiddenField name="tagVersion" value="${dataTagInstance?.tagVersion}" />
   <div class="dialog">
 		<table>
           <tbody>
                <tr class="prop">
                    <td valign="top" class="name">
                      <label for="name"><g:message code="dataTag.name.label" default="Name" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'name', 'errors')}">
                        <label for="name">${fieldValue(bean: dataTagInstance, field: "name")}</label>
                    </td>
                </tr>  
                <tr class="prop">
	                 <td valign="top" class="name">
	                     <label for="dataHolder.value"><g:message code="dataTag.dataHolder.value.label" default="Value" /></label>
	                 </td>
	                 <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'dataHolder', 'errors')}">
	                     <g:textField name="dataHolder.value" value="${dataTagInstance?.dataHolder?.value}"/>
	                 </td>
	             </tr> 
	             
	             <tr class="prop">
	                 <td valign="top" class="name">
	                     <label for="dataHolder.class"><g:message code="dataTag.dataHolder.class.label" default="Type" /></label>
	                 </td>
	                 <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'dataHolder', 'errors')}">
	                     <g:textField name="dataHolder.valueClass" value="${dataTagInstance?.dataHolder?.valueClass.getSimpleName()}"/>
	                 </td>
	             </tr>  
	             
	              <tr class="prop">
	                 <td valign="top" class="name">
	                     <label for="timestamp"><g:message code="dataTag.timestamp.label" default="Timestamp" /></label>
	                 </td>
	                 <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'timestamp', 'errors')}">
	                     <label for="timestamp"><g:formatDate type="datetime" date="${new Date(dataTagInstance.timestamp)}"/></label>
	                 </td>
	             </tr> 
	             <tr class="prop">
                    <td valign="bottom" class="name">
                      <label for="repository"><g:message code="dataTag.repository.label" default="Repository" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'repository', 'errors')}">
                        <g:select name="repository.id" from="${org.scadajack.grails.repository.DataRepository.list()}" 
                        	optionKey="id" value="${dataTagInstance?.repository?.id}" optionValue="name" noSelection="['null': '']" />
                    </td>
                </tr>  
                <tr class="prop">
	                 <td valign="top" class="name">
	                     <label for="tagId"><g:message code="dataTag.tagId.label" default="Tag Id" /></label>
	                 </td>
	                 <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'tagId', 'errors')}">
	                     <label for="tagId">${fieldValue(bean: dataTagInstance, field: "tagId")}</label>
	                 </td>
	             </tr>
	             <tr class="prop">
	                 <td valign="top" class="name">
	                     <label for="tagVersion"><g:message code="dataTag.tagVersion.label" default="Version" /></label>
	                 </td>
	                 <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'tagVersion', 'errors')}">
	                     <label for="tagVersion">${fieldValue(bean: dataTagInstance, field: "tagVersion")}</label>
	                 </td>
	             </tr>     
            </tbody>
        </table>
        <table>
        	<tbody>
 <!--        		<tr>
        			<td>
        				<h1 style="font-size:12px;text-align:center;margin-bottom:0em" >METADATA</h1>
        			</td>
        			<td>
        				<h1 style="font-size:12px;text-align:center;margin-bottom:0em" >PROPERTIES</h1>
        			</td>
        		</tr>
 -->        		
        		<tr>
            		<td>
            			<g:render template="/templates/propertyValueEditor" 
			            			model="['tableId':'metadataProps',
			            				    'domainId':dataTagInstanceId,
			            				    'listAction':'metadataList',
			            				    'editAction':'metadataEdit',
			            				    'caption':'METADATA']"/>
            				<!--     <g:render template="dataTagMetadataEditor"/>   -->
            		</td>
            	</tr>
            	<tr>
            		<td>
            			<g:render template="/templates/propertyValueEditor" 
			            			model="['tableId':'propertyProps',
			            				    'domainId':dataTagInstanceId,
			            				    'listAction':'propertyList',
			            				    'editAction':'propertyEdit',
			            				    'caption':'PROPERTIES']"/>
            			<!-- <g:render template="dataTagPropEditor"/> -->
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