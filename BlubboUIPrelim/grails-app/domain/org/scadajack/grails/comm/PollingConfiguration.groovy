package org.scadajack.grails.comm

import java.io.Reader
import java.io.Writer
import java.util.Map

import org.blubbo.grails.data.DataTag
import org.grails.plugins.csv.CSVMapReader
import org.grails.plugins.csv.CSVWriter
import org.scadajack.camel.comm.IRemoteTarget
import org.scadajack.camel.polling.impl.DefaultPollDriverConfiguration
import org.scadajack.camel.polling.impl.IPollDriverConfiguration
import org.scadajack.grails.util.DomainHelperService
import org.slf4j.LoggerFactory

import com.blubbo.grails.routing.StoredRoutes

class PollingConfiguration {

    static constraints = {
		commRoute(nullable:true)
		repoRoute(nullable:true)
    }
	
	static transients = ['pollDriverConfiguration']
	static hasMany = [targets:RemotePollTarget]
	
	static Writer exportToWriter(Writer writer){
		def b = new CSVWriter(writer,{it ->
			NAME { it.name }
			ENABLED {it.enabled}
			PERIOD { it.period }
			COMM_ROUTE { it.commRoute.name }
			REPO_ROUTE { it.repoRoute.name }
			REFRESH_TAGS_BEFORE_POLL { it.refreshTagsBeforePoll }
			FORCE_WRITE_DELTA { it.forceWriteDelta }
			PROPERTIES {DomainHelperService.importableStringFromMap(it.configProperties)}
		})
		
		LoggerFactory.getLogger(this).info "Exporting ${PollingConfiguration.count()} PollingConfigurations."
		if (PollingConfiguration.count() < 1){
			return b.writer
		}
		PollingConfiguration.list()?.each{
			LoggerFactory.getLogger(this).info "Creating CSV Output for PollingConfiguration: ${it.name}"
			b << it
		}
		return b.writer
	}
	
	static void importFromReader(Reader reader){
		def csvReader = new CSVMapReader(reader)
		csvReader.eachWithIndex { map,i ->
			def pollConfiguration = pollConfigFromCSVMap(map)
			if (!pollConfiguration){
				LoggerFactory.getLogger(this).info("Error Creating PollingConfiguration #${i} from CSV file likely due to bad format")
			} else if(!pollConfiguration?.save()){
				LoggerFactory.getLogger(this).info("Error Creating PollingConfiguration #{i} from CSV file: ${pollConfiguration.errors.allErrors.join('\n')}")
			}
		}
	}
	
	static PollingConfiguration pollConfigFromCSVMap(Map map){
			// First check and see if the configuration has already been created and use it
		def configSetupName = map['NAME']
		if (!configSetupName){
			LoggerFactory.getLogger(this).warn 'Error importing a PollingConfiguration, no name found. Ignoring.'
			return null
		}
		
		
		def configSetup = PollingConfiguration.count() > 0 ? PollingConfiguration.findByName(configSetupName) : null
		if (configSetup == null){
			LoggerFactory.getLogger(this).info "PollingConfiguration Import creating new PollingConfiguration with name: $configSetupName"
			configSetup = new PollingConfiguration(name: configSetupName)
		}
	
		def commRouteName = map['COMM_ROUTE']
		
		if (commRouteName){
			StoredRoutes sr = StoredRoutes.findByName(commRouteName);
			if (!sr){
				sr = new StoredRoutes(name:commRouteName, enable: false, targetUrl: 'temp', routeDescription:'temp');
				sr.save()
			}
			configSetup.commRoute = sr
		}
		
		def repoRouteName = map['REPO_ROUTE']
		
		if (repoRouteName){
			StoredRoutes sr = StoredRoutes.findByName(repoRouteName);
			if (!sr){
				sr = new StoredRoutes(name:repoRouteName, enable: false, targetUrl: 'temp', routeDescription:'temp');
				sr.save()
			}
			configSetup.repoRoute = sr
		}
		
		String props = map['PROPERTIES']
		if (props){
			Map mmap = Eval.me(props)
			if (mmap){
				configSetup.configProperties = mmap
			}
		}
		
		def enabled = map['ENABLED']
		if (enabled != null){
			configSetup.enabled = enabled
		}
		
		def period = map['PERIOD']
		if (period != null){
			configSetup.period = Long.valueOf(period)
		}
		
		return configSetup
		
	}
	
	
	String name;
	
	StoredRoutes commRoute;
	StoredRoutes repoRoute
	boolean refreshTagsBeforePoll = true;
	boolean forceWriteDelta;
	boolean enabled;
	long period;
	IPollDriverConfiguration pollDriverConfiguration
	Map configProperties
	
	IPollDriverConfiguration getPollDriverConfiguration(){
		if (pollDriverConfiguration == null){
			pollDriverConfiguration = new DefaultPollDriverConfiguration(name);
			pollDriverConfiguration.targetUrl = commRoute.targetUrl;
			pollDriverConfiguration.refreshTagsBeforePoll = refreshTagsBeforePoll;
			pollDriverConfiguration.forceWriteDelta = forceWriteDelta;
			pollDriverConfiguration.setEnabled(enabled);
			pollDriverConfiguration.setPeriod(period);
			
			configProperties?.each{ k,v ->
				pollDriverConfiguration.setProp(k,v)
			}
			
			populateTargets(pollDriverConfiguration);
		}
		return pollDriverConfiguration;
	}
	
	private void populateTargets(IPollDriverConfiguration pollDriverConfiguration){
		pollDriverConfiguration.clearTargets();
		for (RemotePollTarget target : targets){
			IRemoteTarget remoteTargetImpl = target.remoteTargetImpl;
			pollDriverConfiguration.add remoteTargetImpl
		}
	}
	
	void addTarget(RemotePollTarget target){
		IRemoteTarget remoteTargetImpl = target.remoteTargetImpl;
		getPollDriverConfiguration().add(remoteTargetImpl)
		this.addToTargets(target)
	}
	
	void setForceWriteDelta(boolean forceWriteDelta){
		this.forceWriteDelta = forceWriteDelta;
		pollDriverConfiguration?.forceWriteDelta = forceWriteDelta;
	}
	
	void setRefreshTagsBeforePoll(boolean refreshTagsBeforePoll){
		this.refreshTagsBeforePoll = refreshTagsBeforePoll;
		pollDriverConfiguration?.refreshTagsBeforePoll = refreshTagsBeforePoll;
	}
	
	
	
	
	
}
