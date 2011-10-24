<g:if test="${flash.message}">
<div class="message">${flash.message}</div>
</g:if>

<div>
	<table id="serverTargets" class="scroll jqTable" cellpadding="0" cellspacing="0"></table>	 
	<div id="serverTargets-pager" class="scroll" style="text-align:center;height:45px"></div> 
	<script type='text/javascript'>
				$(document).ready(function(){
					jQuery('#serverTargets').jqGrid({
						url: "/BlubboUIPrelim/commServerSetup/commServerTargetList/${commServerSetupInstance.id}",
						editurl: "/BlubboUIPrelim/commServerSetup/commServerEditTargets/${commServerSetupInstance.id}",
						datatype : 'json',
						caption  : 'Target Tags',
						//cmTemplate : {sortable:false},  // remove sortable columns for now
						colNames : ['Id','Tag','Address','Operation','Enabled','tagId'],
						colModel : [{name:'targetId',
										width:10},
									{name:'tag',width:50,editable:true,editoptions:{size:40}},
									{name:'address',editable:true},
									{name:'operation',width:20,editable:true},
									{name:'enabled',width:20,editable:true},
									{name:'tagId',editable:true,hidden:true}
									],
							// pager setup
						rowNum	: 3,
						rowList : [3,5,10,25],
						pager    : jQuery('#serverTargets-pager'),
						viewrecords : true,
						autowidth : true,
						height : 'auto',
						shrinkToFit : true,
						gridview : true
						
					}).navGrid('#serverTargets-pager',
						{add:true,edit:true,del:true,search:false,refresh:true},
						{closeAfterEdit:true, afterShowForm: disableNameFieldEdits, onClose:enableNameFieldEdits}, // edit options
						{addCaption: 'Add Property', savekey:[true,13],afterShowForm: insertTagSelectLink}, // add options
						{onclickSubmit:deleteRowIdent} // delete options
					);
				});
				 // Disable editing of key value. Only want to edit key when adding
				var disableNameFieldEdits = function(formid){
					jQuery('#tr_tag input').attr('readonly','readonly');
				}
				var enableNameFieldEdits = function(formid){
					jQuery('#tr_tag input').removeAttr('readonly');
				}
					// Insert link to tag select
				var insertTagSelectLink = function(formid){
					if ($('#insertedSelectLink').length == 0){
							// place the tag selector div so it can be shown after form when needed.
						var bx = $(formid);
						bx.after(
								'<div id="insertedSelectLink" style="padding:5px">\
								<a style="text-align:center" onclick="toggleSelect(event);return false;" \
								href="/BlubboUIPrelim/dataTag/showDataTagSelector">\
								<h3 style="text-align:center">Tag Selector&gt</h3></a>\
								<div id="selectDiv"></div></div>'
						);
						
					}
				}
				
				var deleteRowIdent = function(params,idv){
					var rid = jQuery('#serverTargets').getCell(idv,0)
					var postdata = {'delId':rid};
					return postdata;
				}
					// Toggles the Tag Select Table
				var toggleSelect = function(event){
					var sd = jQuery('#selectDiv');
					if (sd.children('div').length){
						sd.toggle();
					} else {
					jQuery.ajax({type:'POST', 
					url:'/BlubboUIPrelim/dataTag/showDataTagSelector',
					success:function(data,textStatus){jQuery('#selectDiv').html(data);},
					error:function(XMLHttpRequest,textStatus,errorThrown){}});

					var tgt$=jQuery(event.target);
					var bx = tgt$.closest('.ui-widget').width(600);
					}
				}

				var clickTagUpdate = function(rowid,iRow,iCol,e){
					var val = jQuery('#dataTagSelectorTbl').getCell(rowid,2);
					$('input#tag').val(val);
					
				}
            </script> 
            	
            
</div>