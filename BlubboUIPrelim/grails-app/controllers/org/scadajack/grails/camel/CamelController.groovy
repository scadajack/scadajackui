package org.scadajack.grails.camel

import java.io.File

import org.apache.camel.ServiceStatus
import org.scadajack.grails.access.GrailsAccessService
import org.scadajack.grails.util.DomainHelperService

import blubbouiprelim.ScadajackTagLib

class CamelController {

	GrailsAccessService grailsAccessService
	DomainHelperService domainHelperService
	
	static long startTO = 5000
	
    def index = { }
	
	def startCamel = {
		def canStart = !(grailsAccessService.getCamelState() in [ServiceStatus.Started, ServiceStatus.Starting])
		if (canStart){
			grailsAccessService.runCamel()
		}
		//render(template: "/templates/camelControlFrame")
		long startTime = (new Date()).getTime() + startTO;
		while (grailsAccessService.getCamelStateStr() != ServiceStatus.Started){
			sleep(100);
			if ((new Date()).getTime() > startTime){
				break;
			}
		}

		render "${grailsAccessService.getCamelStateStr()}"
		
	}
	
	def stopCamel = {
		if (grailsAccessService.getCamelState() in [ServiceStatus.Started,ServiceStatus.Starting]){
			grailsAccessService.stopCamel();
		}
		long startTime = (new Date()).getTime() + startTO
		while (grailsAccessService.getCamelState() != ServiceStatus.Stopped){
			sleep(100);
			if ((new Date()).getTime() > startTime){
				break;
			}
		}
		render "${grailsAccessService.getCamelStateStr()}"
	}
	
	def reloadCamel = {
		grailsAccessService.reloadRoutes()
		render "${grailsAccessService.getCamelStateStr()}"
	}
	
	def exportData = {
		String fileDir = grailsApplication.config.scadajack.export.directory.defaultDir
		File directory = new File(fileDir)
		domainHelperService.exportDomains(directory, ["*"])
		render "${grailsAccessService.getCamelStateStr()}"
	}
	
	def importData = {
		String fileDir = grailsApplication.config.scadajack.export.directory.defaultDir
		File directory = new File(fileDir)
		domainHelperService.importDomains(directory, ["*"])
		render "${grailsAccessService.getCamelStateStr()}"
	}
	
	def retrieveRunState = {
		def state = grailsAccessService.getCamelStateStr();
		render state
	}
	
}
