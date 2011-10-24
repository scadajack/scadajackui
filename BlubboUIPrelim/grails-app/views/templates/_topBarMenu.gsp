
<div id='top-bar-menu' class='nav clearfix'>
    <script type="text/javascript">
	</script>

	<ul class='alwaysVisible'>
		<li><h4>File</h4>
			<ul class = 'hoverVisible'>
				<li><a class="home" href="${createLink(uri: '/')}">
						<div><g:message code="default.home.label"/></div>
					</a></li>
				<li><g:remoteLink controller='camel' action='exportData' update="camelControlState" asynchronous="false"
							onLoading='pageDisable()' 
							onComplete='pageEnable()'>
						<div>Export</div>
					</g:remoteLink></li>
				<li>	
					<a onclick='openImportDialog()'>		
						<div>Import</div>
					</a>
				</li>
				<g:if env="development">
					<li><g:remoteLink controller='camel' action='importData' update="camelControlState" asynchronous="false"
							onLoading='pageDisable()' 
							onComplete='pageEnable()'>
						<div>Test Import</div>
					</g:remoteLink></li>
				</g:if>
			</ul>
		</li>
	</ul>

	<ul class='alwaysVisible'>
		<li><h4>Operations</h4>
			<ul class = 'hoverVisible'>
				<li class='menu-entry'>Run<sj:menuRightArrow/>
					<ul class='hoverVisible'>
						<li><div id='menu-start-btn' class="btn">
							<g:remoteLink controller='camel' action='startCamel' update="camelControlState" asynchronous="false" after="updateFooter()"
									onLoading='pageDisable()' 
									onComplete='camelStartEffects()'>
								<div>Start</div>
							</g:remoteLink></div></li>
						<li><div id='menu-stop-btn' class="btn"><g:remoteLink controller='camel' action='stopCamel' update="camelControlState" asynchronous="false" after="updateFooter()"
									onLoading='pageDisable()' 
									onComplete='camelStopEffects()'>
								<div>Stop</div>
							</g:remoteLink></div></li>
					</ul>
				</li>
				<li class='menu-entry'>File<sj:menuRightArrow/>
					<ul class='hoverVisible'>
						<li><div class="btn"><g:remoteLink controller='camel' action='exportData' update="camelControlState" asynchronous="false">
								<div>Export</div>
							</g:remoteLink></div></li>
						<li><div class="btn"><g:remoteLink controller='camel' action='importData' update="camelControlState" asynchronous="false">
								<div>Import</div>
							</g:remoteLink></div></li>
					</ul>
				</li>
				<li><g:remoteLink controller='camel' action='reloadCamel' update="camelControlState" asynchronous="false"
							onLoading='pageDisable()' 
							onComplete='pageEnable()'>						
						<div>Reload</div></g:remoteLink></li>
			</ul>
		</li>
	</ul>
	<ul class='alwaysVisible'>
		<li class='list'><h4>Setup</h4>
			<ul class='hoverVisible'>
				<li class='menu-entry'>Options<sj:menuRightArrow/>
					<ul class='hoverVisible'>
						<li><div id='menu-start-btn' class="btn">
							<g:link controller='applicationProps' action='list'>
								<div>Properties</div>
							</g:link></div>
						</li>
						<li>
						    <a class="home" href="${createLink(uri: '/applicationProps/grailsIndex.gsp')}">
								Grails Information
							</a>
						</li>
					</ul>
				</li>
				<li><g:link class="list" controller='pollingConfiguration' action="list">
							<div><g:message code="menu.polling.label" default="Polling" /></div>
					</g:link></li>
				<li><g:link class="list" controller='storedRoutes' action="list">
							<div><g:message code="menu.routes.label" default="Routes" /></div>
					</g:link></li>
				<li><g:link class="list" controller='commServerSetup' action="list">
							<div><g:message code="menu.servers.label" default="Servers" /></div>
					</g:link></li>
				<li><g:link class="list" controller='dataTag' action="list">
							<div><g:message code="menu.tag.label" default="Tags" /></div>
					</g:link></li>
				<li><g:link class="list" controller="dataRepository" action="list">
							<div><g:message code="menu.repositories.label" default="Repositories" /></div>
					</g:link></li>
			</ul>
		</li>
	</ul>
</div>