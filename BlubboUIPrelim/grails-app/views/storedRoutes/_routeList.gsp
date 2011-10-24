<div id='routeList'>
	<table id='routeTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
	<div id="route-pager" class="scroll" style="text-align:center;height:45px"></div>

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#routeTbl').jqGrid({
			url: "${createLink(controller:'storedRoutes', action:'briefList')}",
			editurl:"${createLink(controller:'storedRoutes', action:'listEdit')}",
			datatype : 'json',
			cmTemplate : {cellattr: function(rowId,tv,rawObject,cm,rdata){return "style='white-space:normal;'"}},  // allow text wrapping
			colNames : ['Id','Name','URL','ENA','Description'],
			colModel : [{name:'id',key:true,width:7,hidden:true},
						{name:'name',width:25,editable:true},
						{name:'targetUrl',width:40,editable:true,search:false},
						{name:'enabled',width:8,editable:true,edittype:'checkbox',editoptions:{value:'true:false'}},
						{name:'routeDescription',width:80,editable:true,edittype:'textarea',editoptions: {rows:2,cols:80}}
						],
				// pager setup
			rowNum	: 3,
			rowList : [3,5,10,25],
			pager    : jQuery('#route-pager'),
			//scroll	: true,
			viewrecords : true,  // show record numbers in pager
			autowidth : true,  
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			caption : 'STORED ROUTES',
			ondblClickRow:clickTagUpdate
		}).navGrid('#route-pager',
				{add:true,edit:true,del:true,search:false,refresh:true},
				{}, // edit options
				{addCaption: 'Add Property', savekey:[true,13]}, // add options
				{} // delete options
			
		);

		function clickTagUpdate(rowid,irow,icol){
			var np = "${createLink(controller:'storedRoutes',action:'show')}";
			window.location = np + "/" + rowid;
		}
	});
</script> 
</div> 