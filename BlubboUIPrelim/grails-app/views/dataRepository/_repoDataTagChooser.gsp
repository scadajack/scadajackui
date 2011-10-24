<div id='dataTagChooser'>
	<table id='dataTagChooserTbl' class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
	<div id="dataTagChooser-pager" class="scroll" style="text-align:center;height:45px"></div>

<script type='text/javascript'>
	$(document).ready(function(){
		jQuery('#dataTagChooserTbl').jqGrid({
			url: "${createLink(controller:'dataRepository', action:'addDataTagList', id:dataRepositoryInstance.id)}",
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
			pager    : jQuery('#dataTagChooser-pager'),
			scroll  : true,
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			shrinkToFit : true,
			gridview : true,
			ondblClickRow:clickTagUpdate
		}).navGrid('#dataTagChooser-pager',
			{add:true,edit:false,del:true,search:false,refresh:true},
			{closeAfterEdit:true, afterShowForm: disableNameFieldEdits, onClose:enableNameFieldEdits}, // edit options
			{addCaption: 'Add Property', savekey:[true,13]}, // add options
			{onclickSubmit:deleteRowIdent} // delete options
		);
		jQuery('#dataTagChooserTbl').jqGrid('filterToolbar',{autosearch:true});
	});
	 // Disable editing of key value. Only want to edit key when adding
	var disableNameFieldEdits = function(formid){
		jQuery('#tr_name input').attr('readonly','readonly');
	}
	var enableNameFieldEdits = function(formid){
		jQuery('#tr_tag input').removeAttr('readonly');
	}
	var deleteRowIdent = function(params,idv){
		
		var postdata = {'delId':idv};
		return postdata;
	}
</script> 
</div> 	 