<!-- 
	Template to provide a property editor for name-value pairs. The data should be supplied via json. The format for the 
	data is:
		{"rows":[{"cell":["key1","value1"]},{"cell":["key2","value2"]}],"page":1,"records":2,"total":1}
	In Groovy, the format looks like this (using the 'toString' function on the output. Note that the Eclipse debugger 
	formats the data differently for display than the toString function.):	
		[rows:[[cell:[hh, kk]], [cell:[ss, ff]]], page:1, records:2, total:1.0]
	Template needs the following values defined:
		customController : (optional) specify controller containing list and edit action.
		listAction: name of the controller action for providing the list data.
		editAction: name of the controller action for providing the add, delete, edit functions.
		domainId: Id to access domain instance of interest.
		tableId: HTML id to use for the table element. {tableId}-pager will be used for the pager element.
		caption: (optional) Specify a title for property table
 -->
<script type='text/javascript'>
	$(document).ready(function(){
		jQuery("${tableId?.startsWith('#') ? tableId : '#' + tableId}").jqGrid({
	url: "${createLink(controller:customController ?: controllerName,action:listAction,  id:domainId)}",
	editurl: "${createLink(controller:customController ?: controllerName,action:editAction, id:domainId)}",
	datatype : 'json',
	caption  : "${caption?: 'Properties'}",
	//cmTemplate : {},  // remove sortable columns for now
	colNames : ['Name','Value'],
	colModel : [{name:'name',
				key:true,
					width:50,
					editable:true,editoptions:{size:40},editrules:{required:true},
					cellattr: function(rowId,tv,rawObject,cm,rdata){return "style='white-space:normal;'"}},
				{name:'value',width:50,editable:true,editoptions:{size:40},
						cellattr: function(rowId,tv,rawObject,cm,rdata){return "style='white-space:normal;'"}}
				],
		// pager setup
	rowNum	: "${initialRows ?: 5}",
	rowList : [3,5,10,25,50,100],
	pager    : jQuery("${tableId?.startsWith('#') ? tableId : '#' + tableId}-pager"),
		viewrecords : true,
		autowidth : true,
		height : 'auto',
		shrinkToFit : true,
		gridview : true
	}).navGrid("${tableId?.startsWith('#') ? tableId : '#' + tableId}-pager",
			{add:!${addDisable ?: false},
				edit:!${editDisable ?: false},
				del:!${delDisable ?: false},search:false,refresh:true},
			{closeAfterEdit:true, afterShowForm: pollDisableNameFieldEdits, onClose:pollEnableNameFieldEdits}, // edit options
			{addCaption: 'Add Property', savekey:[true,13]}, // add options
			{onclickSubmit:pollDeleteRowIdent} // delete options
		);
			// Disable editing of key value. Only want to edit key when adding
		function pollDeleteRowIdent(params,idv){
			
			var postdata = {'delId':idv};
			return postdata;
		}
		 	// Disable editing of key value. Only want to edit key when adding
		function pollDisableNameFieldEdits(formid){
			jQuery('#tr_name input').attr('readonly','readonly');
		}
		function pollEnableNameFieldEdits(formid){
			jQuery('#tr_name input').removeAttr('readonly');
		}
	});
</script>
<table id="${tableId}" class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
<div id="${tableId}-pager" class="scroll" style="text-align:center;height:45px"></div>   

