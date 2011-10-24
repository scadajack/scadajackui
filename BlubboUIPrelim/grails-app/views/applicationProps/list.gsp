<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
<!--         <g:set var="entityName" value="${message(code: 'dataTag.label', default: 'DataTag')}" />  -->
        <title><g:message code="default.ApplicationProperties.label" /></title>
    </head>
    <body>
        <div class="body">
            <h1><g:message code="default.ApplicationProperties.SystemProperties.label" default="System Properties"/></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div style='min-width:600'>
	            <table>
	            	<tbody>
		            <tr>
			            <td style='width:600px'>
			            	<g:render template="/templates/propertyValueEditor" 
						            			model="['tableId':'systemProps',
						            				    'listAction':'listSysProps',
						            				    'editAction':'editAppProps',
						            				    'caption':'SYSTEM PROPERTIES',
						            				    'addDisable':true,
						            				    'editDisable':true,
						            				    'delDisable':true]"/>
						</td>
					</tr>
					<tr>
			            <td style='width:600px'>
			            	<g:render template="/templates/propertyValueEditor" 
						            			model="['tableId':'ApplicationUIProps',
						            				    'listAction':'listAppUIProps',
						            				    'editAction':'editAppUIProps',
						            				    'caption':'APPLICATION UI PROPERTIES',
						            				    'addDisable':true,
						            				    'editDisable':true,
						            				    'delDisable':true]"/>
						</td>
					</tr>
					</tbody>
				</table>
			</div>
            
    </body>
</html>