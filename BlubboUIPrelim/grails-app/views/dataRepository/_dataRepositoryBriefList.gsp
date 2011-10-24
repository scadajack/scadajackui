<div id='dataRepositoryBriefList'>
	<table id='dataRepositoryTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
<!--	<div id="dataRepository-pager" class="scroll" style="text-align:center;height:45px"></div> -->

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#dataRepositoryTbl').jqGrid({
			url: "${createLink(controller:'dataRepository', action:'briefList')}",
			datatype : 'json',
			cmTemplate : {cellattr: function(rowId,tv,rawObject,cm,rdata){return "style='white-space:normal;'"}},  // allow text wrapping
			colNames : ['Id','Name','Route'],
			colModel : [{name:'id',key:true,width:7,hidden:true},
						{name:'name',
							width:25},
						{name:'repoRoute',width:40}
						],
				// pager setup
			rowNum	: 3,
			//rowList : [3,5,10,25],
			//pager    : jQuery('#dataRepository-pager'),
			scroll	: true,
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			caption : 'Repository Routes',
			ondblClickRow:clickTagUpdate
		})//.navGrid('#dataRepository-pager',
		//	{add:false,edit:false,del:false,search:false,refresh:true}
			
		//);

		function clickTagUpdate(rowid,irow,icol){
			var np = "${createLink(controller:'dataRepository',action:'show')}";
			window.location = np + "/" + rowid;
		}
	});
</script> 
</div> 
 
