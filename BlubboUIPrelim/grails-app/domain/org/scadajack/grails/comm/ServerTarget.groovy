package org.scadajack.grails.comm

import java.io.Reader
import java.io.Writer
import java.util.Map

import org.blubbo.grails.data.DataTag
import org.grails.plugins.csv.CSVMapReader
import org.grails.plugins.csv.CSVWriter
import org.scadajack.camel.comm.DefaultRemoteTarget
import org.scadajack.camel.comm.IRemoteTarget
import org.slf4j.LoggerFactory

class ServerTarget {

    static constraints = {
		operation (nullable:true)
    }
	
	static transients = ['serverTargetImpl']
	
	static Writer exportToWriter(Writer writer){
		def b = new CSVWriter(writer,{it ->
			TAG_ID { it.tagId }
			ADDRESS { it.address }
			SERVER_CONFIG_NAME { it.serverSetup?.name }
			OPERATION {it.operation }
			ENABLED { it.enabled }
			
		})
		
		LoggerFactory.getLogger(this).info "Exporting ${ServerTarget.count()} RemoteTargets."
		ServerTarget.list().each{
			LoggerFactory.getLogger(this).info "Creating CSV Output for RemoteTarget for tag id: ${it.tagId}"
			b << it
		}
		return b.writer
	}
	
	static void importFromReader(Reader reader){
		def csvReader = new CSVMapReader(reader)
		csvReader.eachWithIndex { map, i ->
			def remoteTarget = targetFromCSVMap(map)
			if(!remoteTarget?.save()){
				if (!remoteTarget){
					LoggerFactory.getLogger(this).info("Error Creating ServerTarget #${i} from CSV file likely due to bad format or missing entry.")
				} else {
					LoggerFactory.getLogger(this).info("Error Creating ServerTarget #${i} from CSV file: ${ServerTarget.errors.allErrors.join('\n')}")
				}
			}
		}
	}
	
	static ServerTarget targetFromCSVMap(Map map){
			// First check for parent object and create one if necessary.
		String serverConfigName = map['SERVER_CONFIG_NAME']
		if (!serverConfigName){
			LoggerFactory.getLogger(this).warn 'Invalid ServerTarget entry. No Server Configuration Name found.'
			return null
		}
		def commConfig = CommServerSetup.findByName(serverConfigName)
		if (commConfig == null){
			LoggerFactory.getLogger(this).info "ServerTarget Import creating new CommServerSetup to hold ServerTargets with name: $serverConfigName"
			commConfig = new CommServerSetup(name: serverConfigName)	
			if (!commConfig.save()){
				LoggerFactory.getLogger(this).warn "Errors instantiating and persiting CommServerConfig: ${commConfig.errors.allErrors.join()}"
			}
		}
		if (!commConfig){
			
			LoggerFactory.getLogger(this).warn "Could not find or create server config for target.}"
			return null
		}
		
		def target = new ServerTarget()
		
		target.serverSetup = commConfig
		
		String idString = map['TAG_ID']
		if (idString){
			target.tagId = UUID.fromString(idString)
		} else {
			target.tagId = UUID.randomUUID()
		}
		
		LoggerFactory.getLogger(this).info "ServerTarget Import creating new ServerTarget for tag id: ${target.tagId}"
		target.serverSetup = commConfig
		target.address = map['ADDRESS']
		target.operation = map['OPERATION']
		target.enabled = map['ENABLED']
		
		return target
	}
	
	UUID tagId;
	String address
	String operation;
	boolean enabled;
	IRemoteTarget serverTargetImpl
	CommServerSetup serverSetup
	
	String toString(){
		if (tagId != null){
			DataTag dataTag = DataTag.findByTagId(tagId);
			return "${dataTag?.name}: ${(operation && operation != "null") ? operation : ''} (${enabled ? 'enabled' : 'disabled'})"
		} else
			super.toString();
	}
	
	IRemoteTarget getServerTargetImpl(){
		if (serverTargetImpl == null && tagId && address){
			serverTargetImpl = new DefaultRemoteTarget(tagId,address,operation);
			serverTargetImpl.enabled = enabled;
		}
		return serverTargetImpl;
	}
	
	/**
	 * Set Operation and force regeneration on next access.
	 * @param operation
	 */
	void setOperation(String operation){
		this.operation = operation;
		serverTargetImpl = null;
	}
	
	void setAddress(String address){
		this.address = address
		serverTargetImpl = null
	}
	
	/**
	 * Set enabled. Note that remoteTargetImpl does not need to be 
	 * regenerated for changes in enabled state.
	 * @param enabled
	 */
	void setEnabled(boolean enabled){
		this.enabled = enabled;
		getServerTargetImpl()?.enabled = true;
	}
	
	def getDataTagForTarget(){
		if (tagId){
			DataTag dataTag = DataTag.findByTagId(tagId)
			return dataTag
		}
	}
	
}
