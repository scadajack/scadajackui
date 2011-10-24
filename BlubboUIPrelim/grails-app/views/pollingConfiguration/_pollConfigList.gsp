<div id='pollConfigList'>
	<table id='pollConfigTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
 	<div id="pollConfig-pager" class="scroll" style="text-align:center;height:45px"></div> 

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#pollConfigTbl').jqGrid({
			url: "${createLink(controller:'pollingConfiguration', action:'briefList')}",
			editurl:"${createLink(controller:'pollingConfiguration', action:'pollConfigEdit')}",
			datatype : 'json',
			cmTemplate : {editrules:{edithidden:true},cellattr: function(rowId,tv,rawObject,cm,rdata){return "style='white-space:normal;'"}},  // allow text wrapping
			colNames : ['Id','Name','Comm Route','Repository','ENA','Period','Force Write Delta','Refresh Before Poll'],
			colModel : [{name:'id',key:true,width:7},
						{name:'name',editable:true,
							width:25},
						{name:'commRoute',width:40,editable:true,search:false,
								edittype:'select',
								editoptions:{dataUrl:"${createLink(controller:'pollingConfiguration', action:'storedRoutesList')}"}},
						{name:'repoRoute',width:40,editable:true,
								edittype:'select',
								editoptions:{dataUrl:"${createLink(controller:'pollingConfiguration', action:'storedRoutesList')}"}},
						{name:'enabled',width:8,edittype:'checkbox',editable:true,editoptions:{value:"true:false"},search:false},
						{name:'period',width:10,editable:true},
						{name:'forceWriteDelta',edittype:'checkbox',
							editable:true,editoptions:{value:"true:false"},
							hidden:true},
						{name:'refreshBeforePoll',edittype:'checkbox',
								editable:true,editoptions:{value:"true:false"},hidden:true}
						],
				// pager setup
			rowNum	: 3,
			rowList : [3,5,10,25],
			pager    : jQuery('#pollConfig-pager'),
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			caption : 'POLLING CONFIGURATIONS',
			ondblClickRow:clickTagUpdate
		}).navGrid('#pollConfig-pager',
			{add:true,edit:true,del:true,search:false,refresh:true},
			{}, // edit options
			{addCaption: 'Add Property', savekey:[true,13]}, // add options
			{} // delete options
			
		);

		function clickTagUpdate(rowid,irow,icol){
			var np = "${createLink(controller:'pollingConfiguration',action:'show')}";
			window.location = np + "/" + rowid;
		}

		function setInitRepo(rowid){
			var r = jQuery('#pollConfigTbl').getGrigParam('selarrrow');
			r[0] = ''
		}
		function upd2(butt,formid,rowid){
			var r = jQuery('#pollConfigTbl').getGrigParam('selarrrow');
			r = r+ 1;
		}
	});
</script> 
</div> 
 
