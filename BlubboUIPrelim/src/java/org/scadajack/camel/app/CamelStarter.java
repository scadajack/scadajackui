package org.scadajack.camel.app;

import java.util.List;

import org.blubbo.camel.app.AppRunner;
import org.blubbo.camel.routebuilder.CustomRoutesConfigurator;
import org.scadajack.camel.polling.PollingDriverRegistry;
import org.scadajack.camel.polling.impl.IPollDriverConfiguration;
import org.scadajack.camel.repo.RepositoryRegistry;
import org.scadajack.camel.server.CommServerRegistry;
import org.scadajack.camel.server.ICommServerConfiguration;
import org.scadajack.grails.access.GrailsAccessService;
import org.scadajack.grails.repository.DataRepository;
import org.scadajack.grails.repository.DataRepositoryService;
import org.scadajack.grails.repository.GrailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelStarter {
	private static Logger log = LoggerFactory.getLogger(CamelStarter.class);
	
	
	private static CamelStarter instance;
	
	public static CamelStarter getInstance(){
		return instance;
	}
	
	//public static void startupCamel(GrailsRepository repository){
	public static void startupCamel(){
		log.info("Starting Camel");
		//org.blubbo.camel.app.Application.startup(repository);
		org.blubbo.camel.app.Application.startup();
	}
	
	private AppRunner appRunner;
	
	public CamelStarter(){
		instance = this;
		//GrailsRepository repository = new GrailsRepository("DefaultGrailsRepository");
		//startupCamel(repository);
		startupCamel();
	}
	
	public void loadCamelRoutes(GrailsAccessService grailsService){
		
		DataRepositoryService dataRepositoryService = grailsService.getDataRepositoryService();
		dataRepositoryService.createRepositories();
		RepositoryRegistry rr = RepositoryRegistry.getInstance();
		rr.clear();
		for (String repoName : dataRepositoryService.getMemoryRepositoriesMap().keySet()){
			GrailsRepository repository = dataRepositoryService.getMemoryRepository(repoName);
			rr.addRepository(repository);
		}
		
			// Add any polling drivers to the polling driver registry.
		List<IPollDriverConfiguration> pdc = grailsService.getPollingConfigurations();
		PollingDriverRegistry pdRegistry = PollingDriverRegistry.getInstance();
		pdRegistry.clear();
		pdRegistry.addAllConfigurations(pdc);
		
			// Add any servers to the Server Registry.
		List<ICommServerConfiguration> csc = grailsService.getCommServerConfigurations();
		CommServerRegistry csRegistry = CommServerRegistry.getInstance();
		csRegistry.clear();
		csRegistry.addAllConfigurations(csc);
		
		getRouteConfigurator().initializeRoutes();
		
	}
	
	public boolean runCamel(){
		return getRouteConfigurator().startContext();
	}
	
	public boolean stopCamel(){
		return getRouteConfigurator().stopContext();
	}
	
	public AppRunner getAppRunner(){
		if (appRunner == null)
			appRunner = org.blubbo.camel.app.Application.getAppRunner();
		return appRunner;
	}
	
	public CustomRoutesConfigurator getRouteConfigurator(){
		return getAppRunner().getRouteConfigurator();
	}
}
