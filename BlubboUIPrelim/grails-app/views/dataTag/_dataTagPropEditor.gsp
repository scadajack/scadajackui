<table>
	<thead>
		<tr>
			<th>Key</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${dataTagInstance?.propertyEntries}" status='i' var="it">
			<tr>
				<td valign="top" class="name">
					<g:textField name="propertyEntries.key" value="${it.key}"/>
	            </td>
	            <td valign="top" class="value">
	                <g:textField name="propertyEntries.value" value="${it.value}"/>
	            </td>
            
			</tr>
		</g:each>
		<tr>
			<td valign="top" class="name">
				<g:textField name="propertyEntries.key"/>
	        </td>
            <td valign="top" class="value">
                <g:textField name="propertyEntries.value"/>
            </td>
		</tr>		
	</tbody>

</table>