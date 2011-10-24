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

class RemotePollTarget {

    static constraints = {
    }
	
	static transients = ['remoteTargetImpl']
	
	static Writer exportToWriter(Writer writer){
		def b = new CSVWriter(writer,{it ->
			TAG_ID { it.tagId }
			POLL_CONFIG_NAME { it.pollConfiguration?.name }
			REMOTE_ADDRESS {it.remoteAddress}
			OPERATION {it.operation }
			ENABLED { it.enabled }
			
		})
		
		LoggerFactory.getLogger(this).info "Exporting ${RemotePollTarget.count()} RemoteTargets."
		RemotePollTarget.list().each{
			LoggerFactory.getLogger(this).info "Creating CSV Output for RemoteTarget for tag id: ${it.tagId}"
			b << it
		}
		return b.writer
	}
	
	static void importFromReader(Reader reader){
		def csvReader = new CSVMapReader(reader)
		csvReader.each { map ->
			def remoteTarget = targetFromCSVMap(map)
			if(!remoteTarget?.save()){
				LoggerFactory.getLogger(this).info("Error Creating RemoteTarget from CSV file: ${RemotePollTarget.errors.allErrors.join('\n')}")
			}
		}
	}
	
	static RemotePollTarget targetFromCSVMap(Map map){
		def pollConfigName = map['POLL_CONFIG_NAME']
		if (!pollConfigName){
			LoggerFactory.getLogger(this).warn 'Invalid ServerTarget entry. No Server Configuration Name found.'
			return null
		}
		def pollConfig = PollingConfiguration.findByName(pollConfigName)
		if (pollConfig  == null){
			LoggerFactory.getLogger(this).info "RemotePollTarget Import creating new PollingConfiguration to hold ServerTargets with name: $pollConfigName"
			pollConfig  = new PollingConfiguration(name: pollConfigName)
			pollConfig .save()
		}
		if (pollConfig == null){
			if (PollingConfiguration.hasErrors()){
				LoggerFactory.getLogger(this).warn "Could not find or create PollConfiguration for target. Errors: ${PollingConfiguration.errors?.allErrors?.join('\n')}"
			}
			return null
		}

		def target = new RemotePollTarget()
		
		target.pollConfiguration = pollConfig 
		
		String idString = map['TAG_ID']
		
		if (idString){
			target.tagId = UUID.fromString(idString)
		} else {
			target.tagId = UUID.randomUUID()
		}
		LoggerFactory.getLogger(this).info "RemotePollTarget Import creating new RemotePollTarget for tag id: ${target.tagId}"
		target.remoteAddress = map['REMOTE_ADDRESS']
		target.operation = map['OPERATION']
		target.enabled = map['ENABLED']
		
		return target
	}
	
	UUID tagId;
	String remoteAddress;
	String operation;
	boolean enabled;
	IRemoteTarget remoteTargetImpl
	PollingConfiguration pollConfiguration
	
	
	IRemoteTarget getRemoteTargetImpl(){
		if (remoteTargetImpl == null && tagId && remoteAddress && operation){
			remoteTargetImpl = new DefaultRemoteTarget(tagId,remoteAddress,operation);
			remoteTargetImpl.enabled = enabled;
		}
		return remoteTargetImpl;
	}
	
	/**
	 * Set Remote Address and force regeneration on next access.
	 * @param remoteAddress
	 */
	void setRemoteAddress(String remoteAddress){
		this.remoteAddress = remoteAddress;
		remoteTargetImpl = null;
	}
	
	/**
	 * Set Operation and force regeneration on next access.
	 * @param operation
	 */
	void setOperation(String operation){
		this.operation = operation;
		remoteTargetImpl = null;
	}
	
	/**
	 * Set enabled. Note that remoteTargetImpl does not need to be 
	 * regenerated for changes in enabled state.
	 * @param enabled
	 */
	void setEnabled(boolean enabled){
		this.enabled = enabled;
		getRemoteTargetImpl()?.enabled = true;
	}
	
	def getDataTagForTarget(){
		if (tagId){
			DataTag dataTag = DataTag.findByTagId(tagId)
			return dataTag
		}
	}
	
}
