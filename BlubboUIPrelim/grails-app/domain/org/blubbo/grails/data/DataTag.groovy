package org.blubbo.grails.data

import java.beans.PropertyChangeListener

import org.blubbo.data.IDataTag
import org.blubbo.data.ITagMetadata
import org.blubbo.data.impl.DefaultDataTagBuilder
import org.grails.plugins.csv.CSVMapReader
import org.grails.plugins.csv.CSVWriter
import org.scadajack.grails.repository.DataRepository
import org.scadajack.grails.repository.DataRepositoryService
import org.scadajack.grails.util.DomainHelperService
import org.slf4j.LoggerFactory

class DataTag {
	
	//static hasOne = [dataHolder:AbstractDataHolder]
	//static hasMany = [metadata:String, propertyEntries: String]
	
	
	static constraints = {
		//dataHolder unique:true
		tagId nullable: true
		repository nullable : true
	}
	
	static mapping = {
		version false
	}

	/*
	 * updateSource and timestamp are stored in the metadata, so no need to persist the properties.
	 */
	static transients = ['sjDataTag','verifySjTag','updateSource']
	
		// Want to hold the IDataTags in a seperate map in memory so we don't lose them if the DataTag's are not kept in memory.
	static Map sjDataTagMap = [:]	
	
	/**
	 * Creates and returns a new DataHolder for a given Scadajack Tag that can be used for 
	 * the repository DataTag.
	 * @param sjTag
	 * @return
	 */
	static AbstractDataHolder getDataHolderForTag(IDataTag<?> sjTag){
		Class<?> clazz = sjTag.getValueClass();
		
		AbstractDataHolder dataHolder = null;
		
		switch (clazz){
			case [Boolean] : dataHolder = new BooleanDataHolder(sjTag); break;
			case [Float,Double] : dataHolder = new DoubleDataHolder(sjTag);
								  dataHolder.maxValue = sjTag.valueHolder?.validator?.max;
								  dataHolder.minValue = sjTag.valueHolder?.validator?.min;
								  dataHolder.defaultValue = sjTag.valueHolder?.validator?.defaultValue	;
								  break;
			case [Byte,Short,Integer,Long] : dataHolder = new LongDataHolder(sjTag); break;
									dataHolder.maxValue = sjTag.valueHolder?.validator?.max
									dataHolder.minValue = sjTag.valueHolder?.validator?.min
									dataHolder.defaultValue = sjTag.valueHolder?.validator?.defaultValue
									  break;
			case [String] : dataHolder = new StringDataHolder(sjTag); break;
									dataHolder.maxSize = sjTag.valueHolder?.validator?.size;
									dataHolder.defaultValue = sjTag.valueHolder?.validator?.defaultValue;
									break;
			default : dataHolder = null;
		}
		return dataHolder;
	}
	
	static Writer exportToWriter(Writer writer){
		def b = new CSVWriter(writer,{it ->
			NAME { it.name }
			TAG_ID { it.tagId }
			TIMESTAMP{it.timestamp}
			VERSION{it.tagVersion}
			REPOSITORY {it.repository?.name}
			METADATA {DomainHelperService.importableStringFromMap(it.metadata)}
			PROPERTIES {DomainHelperService.importableStringFromMap(it.propertyEntries)}
			CLASS {it.dataHolder?.valueClassName.toString() ?: 'java.lang.Object'}
			VALUE{it.dataHolder?.value ?: '0'}
			MIN_VALUE{try{it.dataHolder['minValue'] ?: ''} catch (Exception e){''}}
			MAX_VALUE{try{it.dataHolder['maxValue'] ?: ''} catch (Exception e){''}}
			MAX_SIZE{try{it.dataHolder['maxSize'] ?: ''} catch (Exception e){''}}
			DEFAULT_VALUE{try{it.dataHolder['defaultValue'] ?: ''} catch (Exception e){''}}
			
		})
		
		LoggerFactory.getLogger(this).info "Exporting ${DataTag.count()} tags."
		DataTag.list().each{
			LoggerFactory.getLogger(this).info "Creating CSV Output for DataTag: ${it.name}"
			b << it
		}
		return b.writer
	}
	
	static void importFromReader(Reader reader){
		def csvReader = new CSVMapReader(reader)
		csvReader.each { map ->
			def dataTag = tagFromCSVMap(map)
			//if(!dataTag?.save()){
			if (!dataTag) {LoggerFactory.getLogger(this).warn("Error Creating DataTag from CSV file");}
			if (!dataTag.dataRepositoryService.addTag(dataTag.repository,dataTag)){
				LoggerFactory.getLogger(this).warn("Error Saving DataTag from CSV file to repository. Tag Name: ${dataTag?.name}, Id: ${dataTag?.tagId?.toString()}")
			}
		}
	}
	
	static DataTag tagFromCSVMap(Map map){
		String tagName = map['NAME']
		if (!tagName){
			LoggerFactory.getLogger(this).warn "Found record with no NAME field while importing DataTags. Ignoring record."
			return null;
		}
		
		def dataTag = DataTag.findByName(tagName)
		if (dataTag == null){
			dataTag = new DataTag(name : tagName)
			LoggerFactory.getLogger(this).info "DataTag import creating new tag with name: $tagName"
		}
		
		String idString = map['TAG_ID']
		if (idString){
			dataTag.tagId = UUID.fromString(idString)
		} else {
			dataTag.tagId = UUID.randomUUID()
		}
		
		def timeString = map['TIMESTAMP']
		if (timeString){
			dataTag.timestamp = Long.valueOf(timeString) 
		} else {
			dataTag.timestamp = (new Date()).getTime()
		}
		
		def verString = map['VERSION']
		if (verString){
			dataTag.tagVersion = Long.valueOf(verString)
		} else {
			dataTag.tagVersion = 0L
		}
		
		String repository = map['REPOSITORY']
		if (repository && repository != 'null'){
			def repo = DataRepository.findByName(repository)
			if (!repo){
				repo = new DataRepository()
				repo.name = repository
				repo.save()
			}
			if (repo){
				//repo.addTag dataTag
				dataTag.repository = repo
			}
		}	
		
		String metadata = map['METADATA']
		if (metadata){
			Map mmap = Eval.me(metadata)
			if (mmap){
				dataTag.metadata = mmap
			}
		}		
		
		String propertyEntries = map['PROPERTIES']
		if (propertyEntries){
			Map mmap = Eval.me(propertyEntries)
			if (mmap){
				dataTag.propertyEntries = mmap
			}
		}
		
		
		
		def dataHolder = AbstractDataHolder.holderFromCSVMap(map)
		
		dataTag.dataHolder = dataHolder
		
		return dataTag
	}
	
	String name
	UUID tagId
	Long timestamp
	Long tagVersion
	
	DataRepository repository
	
	AbstractDataHolder dataHolder
	private ObservableMap metadata
	private ObservableMap propertyEntries
	
	private boolean verifySjTag
	//private IDataTag<?> sjDataTag
	def dataRepositoryService	// Presumably since it is a 'def' it won't get picked up by GORM
	
	DataTag(){
		tagId = UUID.randomUUID(); // May want to take this out later for performance and put responsibility 
									// for UUID generation on client.
		timestamp = (new Date()).getTime()
		tagVersion = 0L
		setupMaps()
	}
	
	DataTag(String name, UUID id, Class valueClass){
		this()
		this.name = name
		this.tagId = id
		dataHolder = AbstractDataHolder.getDataHolderForClass(valueClass)
	}
	
	DataTag(IDataTag<?> sjTag){
		name = sjTag.getName();
		tagId = sjTag.getId()
		timestamp = sjTag.getTimestamp()
		tagVersion = sjTag.getVersion()
		setupMaps()
		updateFromIDataTag(sjTag)
		verifySjTag = true
		
	}
	
	private void setupMaps(){
		this.propertyEntries = [:] as ObservableMap
		this.metadata = [:] as ObservableMap
		
		this.metadata.addPropertyChangeListener({evt ->
			notifyPossibleSjDataTagUpdate()
		} as PropertyChangeListener)
		
		this.propertyEntries.addPropertyChangeListener({evt ->
			notifyPossibleSjDataTagUpdate()
		} as PropertyChangeListener)
	}
	
	Map<String,String> getMetadata(){
		return metadata;
	}
	
	void setMetadata(Map<String,String> metadata){
		if (metadata == this.metadata){
			return;
		}
		
		this.metadata.clear()
		this.metadata.putAll(metadata)
		notifyPossibleSjDataTagUpdate()
	}
	
	
	
	String getUpdateSource(){
		return metadata['UPDATE_SOURCE']
	}
	
	void setUpdateSource(String updateSource){
		metadata['UPDATE_SOURCE'] = updateSource
	}
	
	Long getTimestamp(){
		return timestamp
	}
	
	void setTimestamp(Long timestamp){
		this.timestamp =  timestamp
		notifyPossibleSjDataTagUpdate()
	}
	
	Long getTagVersion(){
		return tagVersion
	}
	
	void setTagVersion(Long version){
		this.tagVersion = version
		notifyPossibleSjDataTagUpdate()
	}
	
	Map<String,String> getPropertyEntries(){
		return (Map<String,String>) propertyEntries
	}
	
	void setPropertyEntries(Map<String,String> propertyEntries){
		if (this.propertyEntries == propertyEntries){
			return
		}
		this.propertyEntries.clear()
		this.propertyEntries.putAll(propertyEntries)

		notifyPossibleSjDataTagUpdate()
	}
	
	void setName(String name){
		if (this.name != name){
			this.name = name
			notifyPossibleSjDataTagUpdate()
		}
	}
	
	void notifyPossibleSjDataTagUpdate(){
		if (!verifySjTag){
			sjDataTag = null
		}
	}
	
	void setTagId(UUID tagId){
		if (this.tagId != tagId){
			this.tagId = tagId
			notifyPossibleSjDataTagUpdate()
		}
	}
	
	void setDataHolder(AbstractDataHolder holder){
		if (this.dataHolder != holder){
			this.dataHolder = holder
			notifyPossibleSjDataTagUpdate()
		}
	}
	
	IDataTag<?> getSjDataTag(){
		if (!tagId){
			return null
		} else if (checkSjDataTagCurrent()){
			log.info "getSjDataTag returning cached tag."
			return DataTag.sjDataTagMap[tagId]
		}
/*		 else if (!dataHolder?.sjDataHolderStale) {
			if (!verifySjTag && DataTag.sjDataTagMap[tagId]){
					// if don't need to verify tag and we have a cached tag, then return it.
				return DataTag.sjDataTagMap[tagId]
			} else if (verifySjTag && checkSjDataTagCurrent()){
					// if we need to verify and it verifies, then return cached tag.
				verifySjTag = false
				return DataTag.sjDataTagMap[tagId]
			}
		}
*/		
			// Otherwise, go ahead and create the data tag.
		log.info "getSjDataTag creating a new tag. Cached tag: ${DataTag.sjDataTagMap[tagId]}, verifySjTag: ${verifySjTag}, dataHolder stale? : ${dataHolder?.sjDataHolderStale}"
		verifySjTag = false
		IDataTag<?> dt = newDataTag()
		dataHolder?.sjDataHolderStale = false
		DataTag.sjDataTagMap[tagId] = dt
		return dt
	}
	
	/**
	 * Method validates whether the SjTag's value matches the values set in the DataTag.
	 * @return
	 */
	boolean checkSjDataTagCurrent(){
		log.info ("checkSjDataTag")
		if (!tagId) { checkLog('tagId was null'); return false}
		IDataTag<?> sjTag = DataTag.sjDataTagMap[tagId]
		if (!sjTag) { checkLog('sjTag is null'); return false}
		if (!dataHolder.checkSjDataTagCurrent(sjTag)){ checkLog('dataHolder'); return false }
		if (sjTag.name != name) { checkLog('name', sjTag.name, name); return false }
		if (sjTag.id != tagId){ checkLog('tagId', sjTag.id, tagId); return false }
		if (sjTag.timestamp != timestamp){ checkLog('timestamp', sjTag.timestamp,timestamp)}
		if (sjTag.version != tagVersion){ checkLog('version', sjTag.version, version)}
		def repoProp = sjTag.getProperty('repository')
		if (repoProp != repository.name) { checkLog('repository', repoProp, repository.name); return false }
		if (sjTag.getMetadata() != metadata ) { checkLog('metadata', sjTag.getMetadata(), metadata); return false }
		if (sjTag.getProperties() != propertyEntries) { checkLog('propertyEntries', sjTag.getProperties(), propertyEntries); return false }
		
		return true
	}
	
	void checkLog(String str, def scadajackValue = null, def grailsValue = null){
		log.info "checkSjDataTagCurrent failed due to mismatched: $str. ScadajackTag: ${scadajackValue}, GrailsTag: ${grailsValue}"
	}
	
	void setSjDataTag(IDataTag<?> tag){
		if (tag == null && DataTag.sjDataTagMap.containsKey(tagId)){
			DataTag.sjDataTagMap.remove tagId
		} else {
			DataTag.sjDataTagMap[tagId] = tag
		}
	}
	
	/**
	 * Method allows this tag to be updated from a Scadajack tag.
	 * @param sjTag
	 */
	boolean updateFromIDataTag(IDataTag<?> sjTag){
		log.info("Updating DataTag $name with Scadajack Tag.")
		if (sjTag.name != this.name || sjTag.id != this.tagId){
			throw new IllegalArgumentException("Persistent DataTag update attempted from tag with different" + 
			+ "Id and/or name. Supplied Tag ID: ${tag.id}, Name: ${tag.name}")
		}
		
		Boolean update = false;
		
		//Class<?> clazz = sjTag.getValueClass();
		timestamp = sjTag.timestamp
		tagVersion = sjTag.version
		
		if (dataHolder == null){
			dataHolder = getDataHolderForTag(sjTag)
			update = true
		} else if (!dataHolder.checkSjDataTagCurrent(sjTag)){
			dataHolder.updateFromDataHolder sjTag.getValueHolder()
			update = true
		}
		dataHolder.sjDataHolderStale = false
		
		if (metadata != sjTag.getMetadata()){
			metadata.clear()
			sjTag.getMetadata().each{ k,v ->
				metadata.put k, v.toString()
			}
			update = true
		}
		
		if (propertyEntries != sjTag.getProperties()){
			propertyEntries.clear()
			sjTag.getProperties().each{ k,v ->
				propertyEntries.put k,v
			}
			update = true
		}
		try {
			if (update){
				DataTag.sjDataTagMap[tagId] = sjTag
				
				if (!save(flush:true)){
					def errs = this.hasErrors() ?: this.errors.allErrors.join(' && ')
					log.warn "UpdateFromIDataTag failed on SAVE with errors: $errs"
				}
			}
		} catch (Exception e){
			log.warn "Exception performing save on DataTag for Update. Error: ${e}"
		}
		return update
	}
	
	/**
	 * This method updates an IDataTag from this db tag. Note that if the supplied 
	 * tag does not have the same name and id as this DataTag instance, the method 
	 * will fail.
	 * @param tag
	 * @return
	 */
	protected IDataTag<?> updateIDataTag(IDataTag<?> tag){
		if (!tag || tag.name != name != tag.getId() != tagId){
			log.warn "updateIDataTag method can only be called on instance of the same tag. This tag name: ${name}, id: ${tagId}. Supplied" + 
				" tag name: ${tag?.name}, id: ${tag?.getId()}.";
			return null
		}
		
		def tagBuilder = tag.toMutableDataTag();
		tagBuilder.getMetadata().updateFrom(metadata);
		tagBuilder.setMetadatum(ITagMetadata.UPDATE_SOURCE,repository?.getName())
		tagBuilder.getProperties().updateFrom(propertyEntries);
		tagBuilder.setValue(tag.getValue());
		IDataTag<?> newTag = tagBuilder.toImmutable();
		
		setTimestamp(newTag.getTimestamp());
		
		return newTag;
	}
	
	
	
	/**
	 * Creates a new tag from the Database.
	 * @return
	 */
	protected IDataTag<?> newDataTag(){
		
		
		DefaultDataTagBuilder dtb = new DefaultDataTagBuilder(name,tagId,dataHolder.valueClass);
		
		
		Map<String,Object> propMap = dtb.getProperties().getMap();
		Map<String,Object> mdMap = dtb.getMetadata().getMap();
		
		if (repository){
			propertyEntries['repository'] = repository.name // Make sure repository name is in properties
			if (propMap['repository'] != repository.name){
				propMap['repository'] = repository.name
			}
		}
		
		propertyEntries.each{
			if (propMap[it.key] != it.value){
				propMap[it.key] = it.value
			}
		}
		
		metadata.each{
			if (mdMap[it.key] != it.value){
				mdMap[it.key] = it.value
			}
		}
		
		if (!dataHolder){
			log.warn "Attempt to create new Scadajack Tag from DataTag but DataTag is not fully initialized... DataHolder is not configured."
		}
		
		dtb.setValue(dataHolder.value);
		
		//IAgedHolder<?> holder = dataHolder.newDataHolder()
		
		//IDataTag<?> dt = new DefaultDataTag(tagId, name, new DefaultMetadata(mdMap), new DefaultProperties(propMap),
		//	holder)
		
		IDataTag<?> dt = dtb.toImmutable();
		
		log.info("Created new Scadajack Tag from DataTag. DataTag: name: ${name}, tagId: ${tagId}, repository: ${repository.name}, metadata: ${metadata}, properties: ${propertyEntries}. \n" + 
					"Scadajack Tag: ${dt.toString()}, metadata: ${dt.getMetadata()}, properties: ${dt.getProperties()}")
			// Note that creating the Scadajack DataTag may cause some property changes. Need to synchronize these.
			// So we update the DataTag from the generated Scadajack DataTag
		updateFromIDataTag(dt)
		
		return dt
		
		
	}

}
