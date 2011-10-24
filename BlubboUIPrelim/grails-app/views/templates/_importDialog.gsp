
<script type='text/javascript'>
	var $importDialog;
	$(document).ready(function(){
		$importDialog = $('#file-uploader-box')
			.dialog({
				autoOpen:false,
				title: "Import Dialog"
			})
	});

	function openImportDialog(){
		$importDialog.dialog('open');
		return false;
	}		

</script>