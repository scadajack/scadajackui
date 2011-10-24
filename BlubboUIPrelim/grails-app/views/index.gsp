<html>
    <head>
        <title>Welcome to Grails</title>
        <meta name="layout" content="main" />
    </head>
    <body>
        
        <div id="body">
            <h1>Welcome to ScadaJack Configuration</h1>
            <p style='width:600px;margin:20px'>This page provides access to the ScadaJack Configuration and a portal to the runtime application.
            	The menu bar provides links to configuring data and communications, starting and stopping the 
            	ScadaJack engine, and importing and exporting data, among other functions. The tables below show a 
            	summary of the current setup.</p>
			<table>
				<tbody>
					<tr>
						<td width=50%>
							<g:render template='pollingConfiguration/pollConfigBriefList' />
						</td>
						<td width=50% rowspan=4>
							<g:render template='dataTag/dataTagBriefList' />
						</td>
					</tr>
					<tr>
						<td>
							<g:render template='commServerSetup/serverConfigBriefList' />	
						<td>
					</tr>
					<tr>
						<td>
							<g:render template='storedRoutes/routeBriefList' />
						</td>
						
					</tr>
					<tr>
						<td>
							<g:render template='dataRepository/dataRepositoryBriefList' />
						</td>
					</tr>
				</tbody>
			</table>
            
        </div>
    </body>
</html>
