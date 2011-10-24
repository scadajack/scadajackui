<div id='dataTagList'>
	<table id='dataTagListTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
	<div id="dataTagList-pager" class="scroll" style="text-align:center;height:45px"></div>

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#dataTagListTbl').jqGrid({
			url: "${createLink(controller:'dataTag', action:'dataTagList')}",
			editurl: "${createLink(controller:'dataTag', action:'dataTagEdit')}",
			datatype : 'json',
			//cmTemplate : {sortable:false},  // remove sortable columns for now
			colNames : ['Id','TagId','TagId','Name','Value','Type','Time Stamp','repository'],
			colModel : [{name:'id',key:true,width:10,hidden:true},
						{name:'tagId',width:32,hidden:true},
						{name:'shortTagId',width:10},
						{name:'name',width:20,editable:true},
						{name:'value',width:30,editable:true,search:false},
						{name:'type',width:20,editable:true,edittype:'select',
							editoptions:{dataUrl:"${createLink(controller:'dataTag', action:'knownDataHolderClasses')}"}},
						{name:'timestamp',width:20, search:true},
						{name:'repository',width:30,editable:true,search:false,
							edittype:'select',
							editoptions:{dataUrl:"${createLink(controller:'dataRepository', action:'getRepositoriesOptions')}"}},
						],
				// pager setup
			rowNum	: 50,
			rowList : [10,25,50,100,250,500],
			pager    : jQuery('#dataTagList-pager'),
			//scroll   : true,
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			ondblClickRow:clickTagUpdate,
			multiselect: true
		}).navGrid('#dataTagList-pager',
			{add:true,edit:true,del:true,search:false,refresh:true,
				editfunc:clickTagUpdate /*,addfunc:doAddTagFunc*/},
			{}, // edit options
			{}, // add options
			{onclickSubmit:deleteRowIdent} // delete options
		).navButtonAdd('#dataTagList-pager',
				{onClickButton:showColumnChooser,
				 title:'Choose Columns',
				 caption:'',
				 buttonicon:'ui-icon-transfer-e-w'});

		jQuery('#dataTagListTbl').jqGrid('filterToolbar',{autosearch:true});

//		function doAddTagFunc(){
//			window.location = "${createLink(controller:'dataTag',action:'create')}";		
//		}
		
		function clickTagUpdate(rowid,iRow,iCol,e){
			var np = "${createLink(controller:'dataTag',action:'edit')}";
			var val = jQuery('#dataTagListTbl').getCell(rowid,1);
			var res = np + "/" + val;
			window.location = res;		
		}

		function showColumnChooser(){
			jQuery('#dataTagListTbl').columnChooser();
		}
		
		function deleteRowIdent(params,idv){
			
			var postdata = {'delId':idv};
			return postdata;
		}
			
	});
</script> 
</div> 	 