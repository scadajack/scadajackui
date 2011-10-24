<div id='serverConfigBriefList'>
	<table id='serverConfigTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
<!--	<div id="serverConfig-pager" class="scroll" style="text-align:center;height:45px"></div> -->

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#serverConfigTbl').jqGrid({
			url: "${createLink(controller:'commServerSetup', action:'briefList')}",
			datatype : 'json',
			cmTemplate : {cellattr: function(rowId,tv,rawObject,cm,rdata){return "style='white-space:normal;'"}},  // allow text wrapping
			colNames : ['Id','Name','Comm Route','Repository'],
			colModel : [{name:'id',key:true,width:7,hidden:true},
						{name:'name',
							width:25},
						{name:'commRoute',width:40,editable:true,search:false},
						{name:'repoRoute',width:40,editable:true}
						],
				// pager setup
			rowNum	: 3,
			//rowList : [3,5,10,25],
			//pager    : jQuery('#serverConfig-pager'),
			scroll	: true,
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			caption : 'SERVER CONFIGURATIONS',
			ondblClickRow:clickTagUpdate
		})//.navGrid('#serverConfig-pager',
		//	{add:false,edit:false,del:false,search:false,refresh:true}
			
		//);

		function clickTagUpdate(rowid,irow,icol){
			var np = "${createLink(controller:'commServerSetup',action:'show')}";
			window.location = np + "/" + rowid;
		}
	});
</script> 
</div> 	 