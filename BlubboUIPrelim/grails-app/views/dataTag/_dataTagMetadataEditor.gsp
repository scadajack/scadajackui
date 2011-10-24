<table>
	<thead>
		<tr>
			<th>Key</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${dataTagInstance?.metadata}" status='i' var="it">
			<tr>
				<td valign="top" class="name">
					<g:textField name="metadata.key" value="${it.key}"/>
	            </td>
	            <td valign="top" class="value">
	                <g:textField name="metadata.value" value="${it.value}"/>
	            </td>
			</tr>
		</g:each>
		<tr>
			<g:set var="rowCount" value="${dataTagInstance?.metadata?.size()}" }/>
			<td valign="top" class="name">
				<g:textField name="metadata.key"/>
	        </td>
            <td valign="top" class="value">
                <g:textField name="metadata.value"/>
            </td>
		</tr>	
	</tbody>

</table>