package org.scadajack.grails.repository

import java.util.List

import org.blubbo.data.IDataHolderBuilder
import org.blubbo.data.IDataTag
import org.blubbo.data.impl.DefaultDataTagBuilder
import org.blubbo.data.repository.IDataRepository
import org.blubbo.grails.data.DataTag
import org.scadajack.data.validator.impl.ComparableValidator
import org.scadajack.data.validator.impl.SizedValidator


/**
 * DataRepositoryService provides the interface between the in-memory tag repository used by 
 * Scadajack and the persistent storage mechanism provided by grails.
 * @author Ben
 *
 */
class DataRepositoryService {

	public static final String DEFAULT_REPOSITORY_NAME = "default-repository"
	
    static transactional = true

    def serviceMethod() {

    }
	
	/**
	 * MemoryRepositoriesMap keeps a map of the GrailsRepositories objects keyed by name. Note that these 
	 * are the in-memory repositories as opposed to the database domain repositories. The database domain 
	 * repositories can be retreived by {@link #getRepositories()}.
	 */
	Map<String,GrailsRepository> memoryRepositoriesMap = [:]
	
	/**
	 * This method queries the repositories from the database and creates the in-memory repositories 
	 * for them, stores the reference in the memoryRepositoriesMap, and stuffs the tags configured in 
	 * the database into them.
	 * 
	 * If the memory repositories already exist, this method will skip the repository creation 
	 * and just load the tags into the existing repository using the {@link #loadTagsFromDbIntoMemoryRepository(String)}
	 * method.
	 * 
	 */
	void createRepositories(){
		List<DataRepository> repos = getDbRepositories();
		for (DataRepository repo : repos){
			initializeMemoryRepository(repo.name)
		}
	}
	
	void initializeMemoryRepository(String repoName){
		if (memoryRepositoriesMap[repoName] == null && getDbRepository(repoName) != null){
			GrailsRepository repository = new GrailsRepository(repoName);
			memoryRepositoriesMap[repoName] = repository;
		}
		loadTagsFromDbIntoMemoryRepository(repoName)
	}
	
	/**
	 * Returns a memory repository for the given repository name. If 
	 * one does not exist but there is a repository in the database by 
	 * that name, it will create and inialize the memory repository.
	 * @param repoName
	 * @param initialize will create and initialize if true (default)
	 * @return
	 */
	GrailsRepository getMemoryRepository(String repoName, boolean initialize=true){
		if (!repoName){
			log.warn 'getMemoryRepository called without a Repository Name to reference. Returning null.'
			return null;
		}
		GrailsRepository gr = memoryRepositoriesMap[repoName]
		if (repoName != null && gr == null && initialize){
			initializeMemoryRepository(repoName)
		}
		gr = memoryRepositoriesMap[repoName]
		return gr
	}
	
	List<DataRepository> getDbRepositories(){
		DataRepository.listOrderByName()
	}
	
	DataRepository getDbRepository(String name){
		DataRepository.findByName(name)
	}

	
	/**
	 * Method will, given a repository name, load the tags from the database for that 
	 * repository, convert them to IDataTag instances, and add or update them in the 
	 * memoryDatabase.
	 * 
	 * Note that any tags that already exist in the memory database will be overwritten 
	 * by the Database tags.
	 * @param name
	 */
	void loadTagsFromDbIntoMemoryRepository(String repositoryName){
		def repository = getDbRepository(repositoryName)
		def memoryRepository = getMemoryRepository(repositoryName,false)
		if (repository != null && memoryRepository != null){
			def tags = DataTag.findAllByRepository(repository)
			def tagsAdded = 0;
			tags.each{
				IDataTag<?> newTag = getSjTagForDataTag(repositoryName, it)
				IDataTag<?> currentTag = memoryRepository.getElement(it.name)
				if (!currentTag){
					if (memoryRepository.addTag(newTag)){
						tagsAdded++
					}
				} else {
					if (memoryRepository.updateTag(newTag,null)){
						tagsAdded++
					}
				}	
			}
			log.info("${tagsAdded} tags added or updated to repository ${repositoryName}.")
		} else {
			log.info("Repository ${repositoryName} could not be found and no tags were loaded.")
		}
	}
	
/**
 * =======================================================================================================	
 * New Methods
 * 
 */

/*
 * Repository Update Methods:
 * These methods are generalized methods for updating the repository with either Database DataTag's 
 * or memory IDataTag's. These methods will update both the Database repository and the in-memory 
 * repository.
 */
	
/**
 * 	
 * @param repository
 * @param tag
 * @return
 */
	DataTag addTag(DataRepository repository, DataTag tag){
		String repoName = repository.name;
		if (repoName){
			return addTag(repoName,tag)
		} else {
			return null
		}
	}
	
	DataTag addTag(String repositoryName, DataTag tag){
		IDataTag dt = getSjTagForDataTag(repositoryName, tag)
		IDataTag dtResult = addTag(repositoryName,dt)
		if (dtResult){
			return DataTag.findByTagId(dtResult.id)
		} else {
			return null
		}
	}
	
	IDataTag addTag(GrailsRepository repository, IDataTag tag){
		return repository.addTag(tag);
	}
	
	IDataTag addTag(String repositoryName, IDataTag tag){
		GrailsRepository gr = getMemoryRepository(repositoryName);
		if (gr){
			return addTag(gr,tag);
		} else {
			return null;
		}
	}
	
	DataTag deleteTag(DataRepository repository, DataTag tag){
		String repoName = repository.name;
		if (repoName){
			return deleteTag(repoName,tag)
		} else {
			return null
		}
	}
	
	DataTag deleteTag(String repositoryName, DataTag tag){
		GrailsRepository gr = getMemoryRepository(repositoryName)
		def tagId = tag.tagId
		
		if (gr && tagId && gr.deleteTag(tagId)){
			return tag
		} else {
			return none
		}
	}
	
	IDataTag deleteTag(GrailsRepository repository, IDataTag tag){
		return repository.deleteTag(tag);
	}
	
	IDataTag deleteTag(String repositoryName, IDataTag tag){
		GrailsRepository gr = getMemoryRepository(repositoryName);
		if (gr){
			return deleteTag(gr,tag);
		} else {
			return null;
		}
	}
	
	DataTag updateTag(DataRepository repository, DataTag newTag){
		String repoName = repository?.name
		if (repoName){
			return updateTag(repoName,newTag)
		} else {
			return null
		}
	}
	
	DataTag updateTag(String repositoryName, DataTag newTag){
		IDataTag dt = getSjTagForDataTag(repositoryName, tag)
		IDataTag dtResult = updateTag(repositoryName,dt,null)
		return DataTag.findByTagId(dtResult.id)
	}
	
	IDataTag updateTag(GrailsRepository repository, IDataTag newTag, IDataTag oldTag){
		return repository.updateTag(newTag, oldTag)
	}
	
	IDataTag updateTag(String repositoryName, IDataTag newTag, IDataTag oldTag){
		GrailsRepository gr = getMemoryRepository(repositoryName)
		if (gr){
			return updateTag(gr,newTag,oldTag)
		} else {
			return null
		}
	}
	
	
	/**
	* Method creates a Scadajack tag from a database dataTag. Note it will
	* retrieve the repository name and place in the tag properties.
	* @param memoryRepository
	* @param dataTag
	* @return
	*/
   IDataTag<?> getSjTagForDataTag(String repositoryName, def dataTag){
	   def memoryRepository = getMemoryRepository(repositoryName)
	   if (memoryRepository == null || !dataTag){
		   return null
	   }
	   def oldTag = memoryRepository.getElement(dataTag.tagId)
	   DefaultDataTagBuilder newTagBuilder =
		   oldTag ? new DefaultDataTagBuilder(oldTag) : new DefaultDataTagBuilder(
			   dataTag.name, dataTag.tagId, dataTag.dataHolder?.valueClass)
		   
	   newTagBuilder.metadata.updateFrom dataTag.metadata
	   newTagBuilder.properties.updateFrom dataTag.propertyEntries
	   
	   newTagBuilder.setProperty(IDataRepository.REPOSITORY_KEY, dataTag.repository?.name)
	   
	   newTagBuilder.timestamp = dataTag.timestamp
	   newTagBuilder.version = dataTag.tagVersion
	   newTagBuilder.setValue(dataTag.dataHolder.value)
	   
	   IDataHolderBuilder dhBuilder = newTagBuilder.valueHolder;
	   def dataHolderProps = dhBuilder.properties.keySet()
	   def dbHolderProps = dataTag.dataHolder.properties.keySet()
	   if ('max' in dataHolderProps && 'maxValue' in dhHolderProps){
		   dhBuilder.max = dataTag.dataHolder.maxValue
	   }
	   if ('min' in dataHolderProps && 'minValue' in dhHolderProps){
		   dhBuilder.min = dataTag.dataHolder.minValue
	   }
	   if ('defaultValue' in dataHolderProps && 'defaultValue' in dhHolderProps){
		   dhBuilder.defaultValue = dataTag.dataHolder.defaultValue
	   }
	   
	   IDataTag<?> tag = newTagBuilder.toImmutable()
	   return tag
   }
   
   /**
   * Method will update or create a data tag from an SjTag and, by default, save to
   * the database. The tag properties are searched for a repository name
   * and, if found, the repository is found and set on the DataTag.
   * @param tag
   * @param persist flag to save to database. True by default.
   */
  DataTag createOrUpdateDataTagFromSjTag(IDataTag<?> tag, boolean persist = true){
	  def dbTag = DataTag.findByTagId(tag?.id)
	  if (!dbTag){
		  dbTag = new DataTag(tag.name, tag.id, tag.valueClass)
	  }
	  if (!dbTag || tag.name != dbTag?.name
		  || tag.id != dbTag?.tagId
		  || tag.valueClass != dbTag?.dataHolder?.valueClass){return}
	  
	  dbTag.dataHolder.value = tag.getValue()
	  dbTag.metadata.clear()
	  dbTag.metadata.putAll(tag.metadata)
	  dbTag.propertyEntries.clear()
	  dbTag.propertyEntries.putAll(tag.properties)
	  dbTag.tagVersion = tag.version
	  dbTag.timestamp = tag.timestamp
	  
	  def validator = tag.valueHolder.validator;
	  
	  if (validator.defaultValue){
		  dbTag.dataHolder.defaultValue = validator.defaultValue
	  }
	  
	  switch (validator){
		  case [ComparableValidator]:if (validator.max){
										  dbTag.dataHolder.maxValue = validator.max
									  }
									  if (validator.min){
										  dbTag.dataHolder.minValue = validator.min
									  }
		  case [SizedValidator]:		if (validator.size){
										  dbTag.dataHolder.maxSize = validator.size
									  }
	  }
	  
	  String repoName = tag.getProperty(IDataRepository.REPOSITORY_KEY)
	  if (repoName){
		  def repository = getDbRepository(repoName)
		  dbTag.repository = repository
	  }
	  
	  if (persist && !dbTag.save(flush:true)){
		  if (dbTag.hasErrors()){
			  log.warn("Attempt to save IDataTag with name ${tag.name} failed with errors. Errors: ${dbTag.errors.join(' , ')}")
		  } else {
			  log.warn("Attempt to save IDataTag with name ${tag.name} failed. Unknown Errors.")
		  }
	  }
	  return dbTag
  }
	

	/**
	 * Method retrieves a tag by reference to its id, its name, or by supplying 
	 * another tag (typically a previous version) whose name or id is used to do the 
	 * lookup.
	 * 
	 * The tag is retrieved from the in-memory repository.
	 * @param repoName
	 * @param name
	 * @return
	 */
	IDataTag<?> getSjTag(String repoName, Object tag_id_name){
		if (!repoName || !tag_id_name){
			return null
		}
		
		GrailsRepository repo = getMemoryRepository(repoName);
		
		if (!repo){
			log.warn("getTag(${repoName},${tag_id_name}) could not find repository with the given name.")
			return null
		}
		
		return repo.getElement(tag_id_name)
		
		/*
		DataTag tag = null;
		switch (tag_id_name){
			case String.class: tag = retrieveRepoTagByName(repoName,tag_id_name); break;
			case IDataTag.class : tag_id_name = ((IDataTag<?>)tag_id_name).getId(); // fall through to get by id
			default: tag = retrieveRepoTagByTagId(repoName,tag_id_name); break;
		}

		return tag?.sjDataTag
		*/
	}
	
	private DataTag retrieveDbTagByTagId(def repoName, def tagId){
		if (!repoName || !tagId){
			return null
		}
		def dataRepository = DataRepository.findByName(repoName)
		if (!dataRepository){
			log.warn("Attempt to retrieve tag from repository failed because repository was not found. Repository Requested: $repoName")
			return null
		}
		def c = DataTag.createCriteria()
		def result = c.get {
			eq('tagId',tagId)
			eq('repository',dataRepository)
		}
		return result
	}
	
	private DataTag retrieveDbTagByName(def repoName, def tagName){
		if (!repoName || !tagName){
			return null
		}
		def dataRepository = DataRepository.findByName(repoName)
		def c = DataTag.createCriteria()
		def result = c.get {
			eq('name',tagName)
			eq('repository',dataRepository)
		}
		return result
	}
	
	List<DataTag> getDbTagsForRepository(String repoName){
		DataRepository repository = getDbRepository(repoName)
		if (repository != null){
			return DataTag.findAllByRepository(repoName);
		}
		return null
	}
	
	/**
	 * Multiple tag lookup by reference to a collection of tags, ids, or names.
	 * Note that collection is expected to hold only one type of identifier, not mixed. 
	 * If identifiers are mixed, results will likely be incorrect.
	 * @param repoName
	 * @param tags_ids_names
	 * @return
	 */
	List<IDataTag<?>> getSjTags(String repoName, Collection<?> tags_ids_names){
		Class collClass = null;
		if (tags_ids_names.size < 1){
			return []	// empty list.
		}
		List<IDataTag<?>> tags = [];
		
		tags = tags_ids_names.collect{
			getTag(repoName,it)
		}
		
		return tags;
		
	}

	IDataTag<?> addSjTagToRepository(String repoName, IDataTag tag){
		createOrUpdateDataTagFromSjTag(tag)
		return tag
	}
	
	IDataTag<?> removeSjTagFromRepository(String repoName, IDataTag<?> tag){
		DataTag dbTag = DataTag.findByTagId(tag.id);
		dbTag.delete(flush:true)
		return tag
	}
	
	IDataTag<?> updateSjTagInRepository(String repoName, IDataTag newTag, IDataTag oldTag){
		log.info "updateSjTagInRepository on Repository: $repoName, Tag: ${newTag}, OldTag: ${oldTag ?: 'NULL'}"
		createOrUpdateDataTagFromSjTag(newTag)
		return newTag
		
	}
	
}
