<div id='serverConfigList'>
	<table id='serverConfigTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
	<div id="serverConfig-pager" class="scroll" style="text-align:center;height:45px"></div>

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#serverConfigTbl').jqGrid({
			url: "${createLink(controller:'commServerSetup', action:'briefList')}",
			editurl: "${createLink(controller:'commServerSetup', action:'listEdit')}",
			datatype : 'json',
			cmTemplate : {cellattr: function(rowId,tv,rawObject,cm,rdata){return "style='white-space:normal;'"}},  // allow text wrapping
			colNames : ['Id','Name','Comm Route','Repository'],
			colModel : [{name:'id',key:true,width:7},
						{name:'name',width:25,editable:true},
						{name:'commRoute',width:40,editable:true,search:false,
							edittype:'select',
							editoptions:{dataUrl:"${createLink(controller:'pollingConfiguration', action:'storedRoutesList')}"}},
					{name:'repoRoute',width:40,editable:true,
							edittype:'select',
							editoptions:{dataUrl:"${createLink(controller:'pollingConfiguration', action:'storedRoutesList')}"}},
						],
				// pager setup
			rowNum	: 3,
			rowList : [3,5,10,25],
			pager    : jQuery('#serverConfig-pager'),
			//scroll	: true,
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			caption : 'SERVER CONFIGURATIONS',
			ondblClickRow:clickTagUpdate
		}).navGrid('#serverConfig-pager',
				{add:true,edit:true,del:true,search:false,refresh:true},
				{}, // edit options
				{addCaption: 'Add Property', savekey:[true,13]}, // add options
				{} // delete options
				
			);

		function clickTagUpdate(rowid,irow,icol){
			var np = "${createLink(controller:'commServerSetup',action:'show')}";
			window.location = np + "/" + rowid;
		}
	});
</script> 
</div> 	 