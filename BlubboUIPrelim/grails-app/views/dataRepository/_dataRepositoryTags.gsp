<div id='dataRepositoryTags'>
	<table id='dataRepositoryTagsTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
	<div id="dataRepositoryTags-pager" class="scroll" style="text-align:center;height:45px"></div>

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#dataRepositoryTagsTbl').jqGrid({
			url: "${createLink(controller:'dataRepository', action:'dataTagList', id:dataRepositoryInstance.id)}",
			editurl: "/BlubboUIPrelim/commServerSetup/dataTagEdit",
			datatype : 'json',
			//cmTemplate : {sortable:false},  // remove sortable columns for now
			colNames : ['Id','UID','UID','Name','Value','Type','Timestamp','repository'],
			colModel : [{name:'id',width:30,hidden:true},
						{name:'tagId',width:15,hidden:true},
						{name:'sid',width:10},
						{name:'name',
							width:40},
						{name:'value',width:30,editable:true,search:false},
						{name:'type',width:30,editable:true},
						{name:'timestamp',width:25},
						{name:'repository',width:30,editable:true,search:false},
						],
				// pager setup
			rowNum	: 25,
			rowList : [10,25,50,100],
			pager    : jQuery('#dataRepositoryTags-pager'),
			scroll  : true,
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			ondblClickRow:clickTagUpdate
		}).navGrid('#dataRepositoryTags-pager',
			{add:false,edit:false,del:false,search:false,refresh:true},
			{}, // edit options
			{}, // add options
			{} // delete options
		);
		jQuery('#dataTagSelectorTbl').jqGrid('filterToolbar',{autosearch:true});
		
		function clickTagUpdate(rowid,irow,icol){
			var np = "${createLink(controller:'dataTag',action:'show')}";
			window.location = np + "/" + rowid;
		}
	});
	 
</script> 
</div> 	 