package org.scadajack.grails.repository;

import java.util.Iterator;
import java.util.Set;

import org.blubbo.data.IDataTag;
import org.blubbo.data.ITag;
import org.blubbo.data.repository.impl.SimpleDataRepository;
import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * This class represents the bridge between Camel and Grails with respect to the Tag values. This 
 * class wraps the Grails Persistence mechanism and presents an IDataRepository interface that is 
 * used by the Camel Routes as a message-based repository mechanism.
 * @author Ben
 *
 */
public class GrailsRepository extends SimpleDataRepository{
	private static Logger log = LoggerFactory.getLogger(GrailsRepository.class);
	
	private DataRepositoryService dataRepositoryService; // This is the grails service that provides repo methods
														 // It is obtained from Spring in the constructor. 
	/**
	 * Executor for firing events from this repository.
	 */
	//private ExecutorService eventExec = Executors.newSingleThreadExecutor();
	
	//private Map<IRepositoryEventService,Boolean> eventServices = new ConcurrentHashMap<IRepositoryEventService,Boolean>();
	
	/**
	 * Constructor for the GrailsRepository. The name of the repository needs to be the same as 
	 * the name of the DataRepository.
	 * @param name
	 */
	public GrailsRepository(String name){
		super(name);
		log.info(String.format("GrailsRepository '%s' created",name));
	}
	
	protected DataRepositoryService getRepoService(){
		if (dataRepositoryService == null){
			ApplicationContext ac = ApplicationHolder.getApplication().getMainContext();
			dataRepositoryService = ac.getBean(DataRepositoryService.class);
		}
		
		return dataRepositoryService;
	}
	
	@Override
	public boolean shutdown(boolean arg0) {
		return true;
	}

	@Override
	public void startup() {
		// do nothing
	}

	@Override
	protected <T extends ITag> T doAddTagImpl(T arg0) {
		T result = super.doAddTagImpl(arg0);
		if (result != null){
			if (result instanceof IDataTag<?>)
				getRepoService().addSjTagToRepository(name,(IDataTag<?>)result);
			else
				log.warn("GrailsRepository cannot persist tag {} to database as it is not an IDataTag.",result.getName());
		}
		return result;
	}

	@Override
	protected <T extends ITag> T doDeleteTagImpl(T arg0) {
		T result = super.doDeleteTagImpl(arg0);
		if (result != null){
			if (result instanceof IDataTag<?>)
				getRepoService().removeSjTagFromRepository(name,(IDataTag)result);
			else
				log.warn("GrailsRepository cannot remove tag {} from database as it is not an IDataTag.",result.getName());
		}
		return result;
	}

	@Override
	protected ITag doUpdateTag(ITag newTag, ITag oldTag) {
		ITag result = super.doUpdateTag(newTag, oldTag);
		if (result != null){
			getRepoService()
				.updateSjTagInRepository(name,(IDataTag<?>)newTag, (IDataTag<?>)oldTag);
			log.info("Updating database {} with new Tag, Old Tag is {}  Returned Result: {}",
					new Object[]{name,oldTag != null ? "provided." : "not provided.",result != null ? result.toString() : "NULL"});
		} else {
			log.warn("doUpdateTag is aborting since provided tag is not instance of " +
					"IDataTag or oldTag is provided and not instance of IDataTag");
		}
		return result;
	}
	
}
