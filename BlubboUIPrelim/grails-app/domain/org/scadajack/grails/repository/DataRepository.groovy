package org.scadajack.grails.repository

import java.io.Reader
import java.io.Writer

import org.blubbo.grails.data.DataTag
import org.blubbo.grails.data.DataTagController
import org.grails.plugins.csv.CSVMapReader
import org.grails.plugins.csv.CSVWriter
import org.slf4j.LoggerFactory

import com.blubbo.grails.routing.StoredRoutes


/**
 * This class is the heart of the repository mechanism for the Grails version of Scadajack. It will 
 * store data in the Grails Database. The DataRepositoryService uses this domain class to do the 
 * actual storage etc. (The GrailsRepository class in turn uses the DataRepositoryService.)
 * 
 * Note that with the current implementation, applications can only have one Grails repository.
 * @author Ben
 *
 */
class DataRepository {

    static constraints = {
		repoRoute(nullable:true)
    }
	
	static hasMany = [dataTags : DataTag]
	
	static Writer exportToWriter(Writer writer){
		def b = new CSVWriter(writer,{it ->
			UID({it?.uid})
			NAME({ it?.name })
			
		})
		
		LoggerFactory.getLogger(this).info "Exporting ${DataRepository.count()} repositories."
		DataRepository.list().each{
			LoggerFactory.getLogger(this).info "Creating CSV Output for Repository: ${it.name}"
			b << it
			
		}
		return b.writer
	}
	
	static void importFromReader(Reader reader){
		def csvReader = new CSVMapReader(reader)
		csvReader.each { map ->
			def repoId = map['UID']
			def repoName = map['NAME']
			if (!repoName){
				return;
			}
			def repository = DataRepository.findByUid(repoId)
			if (!repository){
				repository = DataRepository.findByName(repoName)
				if (!repository){
					LoggerFactory.getLogger(this).info "DataRepository Import creating new Repository with name: $repoName"
					repository = new DataRepository();
				}
			}
			repository.name = repoName
			repository.uid = !repository.uid && repoId ? repoId : UUID.randomUUID().toString()
			repository?.save(insert:true)
		}
	}
	
	String uid
	String name
	StoredRoutes repoRoute
	
	DataTag addTag(DataTag tag){
		if (!(tag in dataTags)){
			//dataTags.add tag
			addToDataTags(tag)
			if (tag.repository != this){
				tag.repository = this
			}
			save() // repository saves should cascade to tag
			return tag
		}
		return null
	}
	
	DataTag removeTag(DataTag tag){
		if (tag in dataTags){
			//dataTags.remove tag
			removeFromDataTags(tag)
			tag.repository = null
			save()
			return tag
		}
		return null
	}
	
	DataTag updateTag(DataTag oldTag, DataTag newTag){
		
		DataTag dt = DataTagController.fromName(sjTag.name)
		if (dt){
			if (oldTag){
				if (dt.sjDataTag.is( oldTag.sjDataTag)){
					dt.updateFromIDataTag sjTag
				}
			} else {
				dt.updateFromIDataTag sjTag
			}
		}
		dt?.sjDataTag
	}
	
	void clear(){
		dataTags.each{ DataTag it ->
			it.sjDataTag = null
			it.repository = null
		}
		dataTags.clear()
		save()
	}
}
