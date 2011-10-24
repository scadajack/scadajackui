<div id='pollConfigBriefList'>
	<table id='pollConfigTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
<!-- 	<div id="pollConfig-pager" class="scroll" style="text-align:center;height:45px"></div>  -->

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#pollConfigTbl').jqGrid({
			url: "${createLink(controller:'pollingConfiguration', action:'briefList')}",
			datatype : 'json',
			cmTemplate : {cellattr: function(rowId,tv,rawObject,cm,rdata){return "style='white-space:normal;'"}},  // allow text wrapping
			colNames : ['Id','Name','Comm Route','Repository','ENA'],
			colModel : [{name:'id',key:true,width:7,hidden:true},
						{name:'name',
							width:25},
						{name:'commRoute',width:40,editable:true,search:false},
						{name:'repoRoute',width:40,editable:true},
						{name:'enabled',width:8,search:false}
						],
				// pager setup
			rowNum	: 3,
			//rowList : [3,5,10,25],
			//pager    : jQuery('#pollConfig-pager'),
			scroll	: true,
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			caption : 'POLLING CONFIGURATIONS',
			ondblClickRow:clickTagUpdate
		})//.navGrid('#pollConfig-pager',
		//	{add:false,edit:false,del:false,search:false,refresh:true}
			
		//);

		function clickTagUpdate(rowid,irow,icol){
			var np = "${createLink(controller:'pollingConfiguration',action:'show')}";
			window.location = np + "/" + rowid;
		}
	});
</script> 
</div> 
 
