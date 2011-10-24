<div id="camelControl">
	<script type="text/javascript">
		var editMode=false;
		$(function(){
			$('#camelControl .camelControlTitle').click(function(){
					var body$=$('#camelControl .btnArea');
					if (body$.is(':hidden')){
						body$.show();
					} else {
						body$.hide();
					}
			});
		});
			
	
	</script>

	<div class="camelControlTitle" id="camelControlState"><sj:camelState/></div>
	<div class="btnArea">
		<div class="btn"><g:remoteLink controller='camel' action='startCamel' update="camelControlState">Start</g:remoteLink></div>
		<div class="btn"><g:remoteLink controller='camel' action='stopCamel' update="camelControlState">Stop</g:remoteLink></div>
		<div class="btn"><g:remoteLink controller='camel' action='reloadCamel' update="camelControlState">Reload</g:remoteLink></div>
		<div class="btn"><g:remoteLink controller='camel' action='exportData' update="camelControlState">Export</g:remoteLink></div>
		<div class="btn"><g:remoteLink controller='camel' action='importData' update="camelControlState">Import</g:remoteLink></div>
	</div>	
</div>