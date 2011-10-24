package blubbouiprelim

import org.scadajack.grails.access.GrailsAccessService
import org.scadajack.grails.camel.CamelController

class ScadajackTagLib {

	static namespace = 'sj'
	
	GrailsAccessService grailsAccessService
	
	def camelState = {attrs, body ->
		out << grailsAccessService.getCamelStateStr()
	}
	
	def menuRightArrow = {attrs, body ->
		out << "<span class='menu-right-arrow' style='background: -32px -17px url(${resource(dir:'css',file:'/images/ui-icons_222222_256x240.png')})  no-repeat;" +
				"width:16px;height:16px;position:absolute;right:0'></span>"	
	}
}
