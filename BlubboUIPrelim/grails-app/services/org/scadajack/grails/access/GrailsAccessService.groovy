package org.scadajack.grails.access

import org.apache.camel.CamelContext
import org.apache.camel.ServiceStatus
import org.scadajack.camel.app.CamelStarter
import org.scadajack.camel.polling.impl.IPollDriverConfiguration
import org.scadajack.camel.server.ICommServerConfiguration
import org.scadajack.grails.comm.CommServerSetup
import org.scadajack.grails.comm.PollingConfiguration
import org.scadajack.grails.repository.DataRepository
import org.scadajack.grails.repository.DataRepositoryService

class GrailsAccessService {

    static transactional = true

	CamelStarter camelStarter
	CamelContext camelContext
	DataRepositoryService dataRepositoryService
	
    def serviceMethod() {

    }
	
	private CamelStarter getCamelStarter(){
		if (camelStarter == null){
			camelStarter = CamelStarter.getInstance();
		}
		return camelStarter;
	}
	
	List<DataRepository> getDataRepositories(){
		DataRepository.list()
	}
	
	List<ICommServerConfiguration> getCommServerConfigurations(){
			CommServerSetup.list().collect { 
				it.getServerConfigImpl()	
			}
	}
	
	List<IPollDriverConfiguration> getPollingConfigurations(){
		def pl = PollingConfiguration.list()
		pl.collect {
			it.getPollDriverConfiguration()
		}
	}
	
	void loadRoutes(){
		getCamelStarter().loadCamelRoutes this
		
	}
	
	boolean runCamel(){
		getCamelStarter().runCamel();
	}
	
	boolean stopCamel(){
		getCamelStarter().stopCamel();
	}
	
	void reloadRoutes(){
		getCamelStarter().loadCamelRoutes this
	}
	
	CamelContext getCamelContext(){
		if (camelContext == null){
			camelContext = getCamelStarter().getAppRunner().getCamelContext()
		}
		return camelContext
	}
	
	ServiceStatus getCamelState(){
		getCamelContext().getStatus();
	}
	
	String getCamelStateStr(){
		String state = getCamelState().toString()
		//return "State: $state"
		if (state == 'Started'){
			state = 'Running'
		}
		state = state.toUpperCase()
	}
}
