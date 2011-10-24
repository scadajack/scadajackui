<div id='routeBriefList'>
	<table id='routeTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
<!-- <div id="route-pager" class="scroll" style="text-align:center;height:45px"></div>  -->	

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#routeTbl').jqGrid({
			url: "${createLink(controller:'storedRoutes', action:'briefList')}",
			datatype : 'json',
			cmTemplate : {cellattr: function(rowId,tv,rawObject,cm,rdata){return "style='white-space:normal;'"}},  // allow text wrapping
			colNames : ['Id','Name','URL','ENA'],
			colModel : [{name:'id',key:true,width:7,hidden:true},
						{name:'name',
							width:25},
						{name:'url',width:40,editable:true,search:false},
						{name:'enabled',width:8,search:false}
						],
				// pager setup
			rowNum	: 3,
			//rowList : [3,5,10,25],
			//pager    : jQuery('#route-pager'),
			scroll	: true,
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			caption : 'STORED ROUTES',
			ondblClickRow:clickTagUpdate
		})//.navGrid('#route-pager',
			//{add:false,edit:false,del:false,search:false,refresh:true}
			
		//);

		function clickTagUpdate(rowid,irow,icol){
			var np = "${createLink(controller:'storedRoutes',action:'show')}";
			window.location = np + "/" + rowid;
		}
	});
</script> 
</div> 