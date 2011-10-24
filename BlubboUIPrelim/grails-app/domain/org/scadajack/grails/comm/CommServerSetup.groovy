package org.scadajack.grails.comm

import java.io.Reader
import java.io.Writer
import java.util.Map

import org.blubbo.grails.data.AbstractDataHolder
import org.blubbo.grails.data.DataTag
import org.grails.plugins.csv.CSVMapReader
import org.grails.plugins.csv.CSVWriter
import org.scadajack.camel.comm.IRemoteTarget
import org.scadajack.camel.server.CommServerConfiguration
import org.scadajack.camel.server.ICommServerConfiguration
import org.scadajack.grails.repository.DataRepository
import org.scadajack.grails.util.DomainHelperService
import org.slf4j.LoggerFactory

import com.blubbo.grails.routing.StoredRoutes

class CommServerSetup {

    static constraints = {
		commRoute nullable:true
		repoRoute nullable:true
    }
	
	static transients = ['serverConfigImpl'];
	
	static hasMany = [targets:ServerTarget]
	
	static Writer exportToWriter(Writer writer){
		def b = new CSVWriter(writer,{it ->
			NAME({ it.name })
			COMM_ROUTE { it.commRoute.name }
			REPO_ROUTE { it.repoRoute.name }
			PROPERTIES {DomainHelperService.importableStringFromMap(it.configProperties)}
		})
		
		LoggerFactory.getLogger(this).info "Exporting ${CommServerSetup.count()} CommServerSetups."
		CommServerSetup.list().each{
			LoggerFactory.getLogger(this).info "Creating CSV Output for CommServerSetup: ${it.name}"
			b << it
		}
		return b.writer
	}
	
	static void importFromReader(Reader reader){
		def csvReader = new CSVMapReader(reader)
		csvReader.each { map ->
			def commServerSetup = setupFromCSVMap(map)
			if(!commServerSetup?.save()){
				LoggerFactory.getLogger(this).info("Error Creating CommServerSetup from CSV file: ${CommServerSetup.errors.allErrors.join('\n')}")
			}
		}
	}
	
	static CommServerSetup setupFromCSVMap(Map map){
			// First look to see if server setup is already created and use it
		String serverSetupName = map['NAME']
		def serverSetup = CommServerSetup.findByName(serverSetupName) ?: null;
		if (!serverSetup){
			LoggerFactory.getLogger(this).info "CommServerSetup Import creating new CommServerSetup with name: $serverSetupName"
			serverSetup = new CommServerSetup()
			serverSetup.name = serverSetupName
		}
			// Should have serverSetup now, so proceed to configure
		def commRouteName = map['COMM_ROUTE']
		if (commRouteName){
			StoredRoutes sr = StoredRoutes.findByName(commRouteName);
			if (!sr){
				sr = new StoredRoutes(name:commRouteName, enable: false, targetUrl: 'temp', routeDescription:'temp');
				sr.save()
			} 
			serverSetup.commRoute = sr
		}
		
		def repoRouteName = map['REPO_ROUTE']
		if (repoRouteName){
			StoredRoutes sr = StoredRoutes.findByName(repoRouteName);
			if (!sr){
				sr = new StoredRoutes(name:repoRouteName, enable: false, targetUrl: 'temp', routeDescription:'temp');
				sr.save()
			}
			serverSetup.repoRoute = sr
		}
		
		String props = map['PROPERTIES']
		if (props){
			Map mmap = Eval.me(props)
			if (mmap){
				serverSetup.configProperties = mmap
			}
		}
		
		return serverSetup
		
	}
	
	
	String name
	StoredRoutes commRoute
	StoredRoutes repoRoute
	Map configProperties = [:]
	
	ICommServerConfiguration serverConfigImpl
	
	ICommServerConfiguration getServerConfigImpl(){
		if (!serverConfigImpl && name){
			serverConfigImpl = new CommServerConfiguration(name);
			if (commRoute){
				serverConfigImpl.commUrl = commRoute.targetUrl;
			}
			populateTargets(serverConfigImpl);
			configProperties.each { k,v ->
				serverConfigImpl.setProp(k,v);
			}
		}
		return serverConfigImpl;
	}
	
	private void populateTargets(ICommServerConfiguration serverConfigImpl){
		serverConfigImpl.clearTargets();
		for (ServerTarget target : targets){
			IRemoteTarget serverTargetImpl = target.serverTargetImpl;
			serverConfigImpl.add serverTargetImpl
		}
	}
	
	void addTarget(ServerTarget target){
		IRemoteTarget serverTargetImpl = target.serverTargetImpl;
		getServerConfigImpl().add(serverTargetImpl)
		this.addToTargets(target)
	}
	
	void setCommRoute(StoredRoutes commRoute){
		this.commRoute = commRoute
		if (serverConfigImpl){
			serverConfigImpl?.commUrl = commRoute.targetUrl;
		}
	}
	
}
