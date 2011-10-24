<div id='dataTagCreateDiv'>
	<g:form name='dataTagCreateForm' 
			url="[controller:'dataTag', action='save']"
			update='dataTagCreateDiv' >
       <div class="dialog">
           <table>
               <tbody>
               	<tr class="prop">
                       <td valign="top" class="name">
                           <label for="name"><g:message code="dataTag.name.label" default="Name" /></label>
                       </td>
                       <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'name', 'errors')}">
                           <g:textField name="name" value="${dataTagInstance?.name}" />
                       </td>
                   </tr>
                   
                   <tr class="prop">
                       <td valign="top" class="name">
                           <label for="dataHolder.type"><g:message code="dataTag.dataHolder.type.label" default="Type" /></label>
                       </td>
                       <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'dataHolder.value', 'errors')}">
                           <g:select name="dataHolder.valueClass" from="${org.blubbo.grails.data.AbstractDataHolder.knownDataHolderClasses}" />
                       </td>
                   </tr>
                   
                   <tr class="prop">
                       <td valign="top" class="name">
                           <label for="dataHolder.value"><g:message code="dataTag.dataHolder.value.label" default="Value" /></label>
                       </td>
                       <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'dataHolder', 'errors')}">
                           <g:textField name="dataHolder.value" value="${dataTagInstance?.dataHolder?.value}" />
                       </td>
                   </tr>
                   
                   <tr class="prop">
                       <td valign="top" class="name">
                           <label for="repository"><g:message code="dataTag.repository.label" default="Repository" /></label>
                       </td>
                       <td valign="top" class="value ${hasErrors(bean: dataTagInstance, field: 'repository', 'errors')}">
                           <g:select name="repository.id" from="${org.scadajack.grails.repository.DataRepository.list()}" optionKey="id" optionValue="name" value="${dataTagInstance?.repository?.id}" noSelection="['null': '']" />
                       </td>
                   </tr>
               
               </tbody>
           </table>
           <table class="map-table">
  	<tbody>
  		<tr>
  			<td>
  				<h1 style="font-size:12px;text-align:center;margin-bottom:0em" >METADATA</h1>
  			</td>
  			<td>
  				<h1 style="font-size:12px;text-align:center;margin-bottom:0em" >PROPERTIES</h1>
  			</td>
  		</tr>
  		<tr>
      		<td>
      			<g:render template="dataTagMetadataEditor"/>
      		</td>
      		<td>
      			<g:render template="dataTagPropEditor"/>
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