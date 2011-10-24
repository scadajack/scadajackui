<div id='dataTagBriefList'>
	<table id='dataTagBriefListTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
	<div id="dataTagBriefList-pager" class="scroll" style="text-align:center;height:45px"></div>

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#dataTagBriefListTbl').jqGrid({
			url: "${createLink(controller:'dataTag', action:'briefList')}",
			datatype : 'json',
			cmTemplate : {cellattr: function(rowId,tv,rawObject,cm,rdata){return "style='white-space:normal;'"}},  // allow text wrapping
			colNames : ['Id','Name','Value','Type','TagId','repository'],
			colModel : [{name:'id',key:true,hidden:true},
						{name:'name',
							width:50},
						{name:'value',width:30,editable:true,search:false},
						{name:'type',width:20,editable:true},
						{name:'tagId',width:50,editable:false,hidden:true},
						{name:'repository',width:50,editable:true,search:false},
						],
				// pager setup
			rowNum	: 25,
			rowList : [10,25,50,100],
			pager    : jQuery('#dataTagBriefList-pager'),
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			caption : 'DATA TAGS',
			ondblClickRow:clickTagUpdate
		}).navGrid('#dataTagBriefList-pager',
			{add:false,edit:false,del:false,search:false,refresh:true}
			
		);
		jQuery('#dataTagBriefListTbl').jqGrid('filterToolbar',{autosearch:true});

		function clickTagUpdate(rowid,irow,icol){
			var np = "${createLink(controller:'dataTag',action:'show')}";
			window.location = np + "/" + rowid;
		}
	});
</script> 
</div> 	 