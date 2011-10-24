<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'sjControl.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'topMenu.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'sjPageStyles.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />
        <g:javascript library="jquery" plugin="jquery"/>
        <g:javascript library="sjEffects"/>
        
        
        
        
        <link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.15.custom.css')}"/>
        <link rel="stylesheet" href="${resource(dir:'css',file:'ui.jqgrid.css')}"/>
        <g:if env='development'>
        	<g:javascript library="jquery/jquery-ui-1.8.15.custom"/>
        </g:if>
        <g:else>
        	<g:javascript library="jquery/jquery-ui-1.8.15.custom.min"/>
        </g:else>
        
        <!--  Multiselect: -->
        <link rel="stylesheet" href="${resource(dir:'css',file:'ui.multiselect.css')}"/>
        <g:javascript library="jquery/ui.multiselect"/>
        
        <!--  For Grid Tables -->
        <g:javascript library="jquery/grid.locale-en"/>
        <g:if env='development'>
        	<g:javascript library="jquery/jquery.jqGrid.src"/>
        </g:if>
        <g:else>
        	<g:javascript library="jquery/jquery.jqGrid.min"/>
        </g:else>
        
        <uploader:head />
    </head>
    <body>
    	<div id="page-busy-overlay">
	        <div id="page-disable-overlay" style="height: 100%;width: 100%; position: fixed; left: 0px; top: 0px; z-index: 949;opacity: 0.3;" class="ui-widget-overlay">
					
			</div>
			<div id="spinner" class="spinner">
		            <img src="${resource(dir:'images',file:'ajax-loader.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
		    </div>	
		</div>
		
			<!-- Import Dialog -->
		<g:render template='/templates/importDialog' />
        <div id="file-uploader-box">
        	<uploader:uploader id="imageUpload" url="${[controller:'fileUpload', action:'upload']}" multiple="true" />
        </div>
        
        <div id="scadajackLogo"><a href="http://www.kantrol.com"><img src="${resource(dir:'images',file:'scadajack-logo-sm.png')}" alt="ScadaJack" border="0" /></a></div>
        <g:render template='/templates/topBarMenu'/>
        
        
        
        <g:layoutBody />
        <div style="clear:both">
<!--         <g:render template='/templates/camelControl'/>   -->
	        <div id="camelControl"><%!  %>
				<g:remoteLink controller='camel' action='retrieveRunState' update='rstate'><div id='rstate'></div></g:remoteLink>
			</div>
			<script type='text/javascript'>
				function updateFooter(){
					var cc = $('#camelControl>a');
					cc.click();
				}
			
				$(document).ready(function(){
						updateFooter();
					});
			</script>
        </div>
    </body>
</html>