<div id="editingControl">
	<script type="text/javascript">
		var editMode=false;
		$(function(){
			$('#editBtnTitle, #editBtnStatus').click(function(){
					var body$=$(this.parentNode,'.btnArea');
					if (body$.is(':hidden')){
						body$.show();
					} else {
						body$.hide();
					}
			});

			$('#editBtn,#browseBtn').click(function(){
					editMode= !editMode;
					var text$=$('#editBtnTitle');
					var textStr = text$.text();
					if (editMode){
						$('#editBtn').addClass('editBtnSelected');
						$('#browseBtn').removeClass('editBtnSelected');
						text$.text('EDITING');
						
					} else {
						$('#editBtn').removeClass('editBtnSelected');
						$('#browseBtn').addClass('editBtnSelected');
						text$.text('BROWSING');
					}
					
			});
		});
			
	
	</script>
	<div class="title" id="editBtnTitle">BROWSING</div>
	<div class="btnArea">
		<div id="editBtn" class="editBtn">EDIT</div>
		<div id="browseBtn" class="editBtn editBtnSelected">BROWSE</div>
	</div>
</div>