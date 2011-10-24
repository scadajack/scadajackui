


function pageEnable(){
	jQuery('#page-busy-overlay').hide();
}
function pageDisable(){
	jQuery('#page-busy-overlay').show();
/*	    var fc = jQuery('#spinner'); 
		fc.before(
				'<div id="page-disable-overlay" style="height: 100%;' + 
				'width: 100%; position: fixed; left: 0px; top: 0px; z-index: 949;' +
				'opacity: 0.3;" class="ui-widget-overlay">' +
				'</div>'
			)
*/		
}



function camelStartEffects(){
	pageEnable();
	jQuery("#menu-start-btn a").addClass("menu-btn-disabled");
	jQuery("#menu-stop-btn a").removeClass("menu-btn-disabled");
}

function camelStopEffects(){
	pageEnable();
	jQuery("#menu-stop-btn a").addClass("menu-btn-disabled");
	jQuery("#menu-start-btn a").removeClass("menu-btn-disabled");
}
