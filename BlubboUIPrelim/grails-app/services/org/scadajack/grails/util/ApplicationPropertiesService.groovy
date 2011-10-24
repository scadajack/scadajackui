package org.scadajack.grails.util

import org.codehaus.groovy.grails.commons.GrailsApplication;

class ApplicationPropertiesService {

    static transactional = true
	
	GrailsApplication grailsApplication

    def sysProps() {
		System.getProperties()
    }
	
	def grailsConfigProps(){
		grailsApplication.getConfig().toProperties()
	}
}
