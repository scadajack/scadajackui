package org.scadajack.grails.util

import grails.test.*

import java.util.UUID

import org.blubbo.grails.data.DataTag
import org.blubbo.grails.data.LongDataHolder
import org.scadajack.grails.comm.CommServerSetup
import org.scadajack.grails.comm.PollingConfiguration
import org.scadajack.grails.comm.RemotePollTarget
import org.scadajack.grails.comm.ServerTarget
import org.scadajack.grails.repository.DataRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.blubbo.grails.routing.StoredRoutes

class DomainHelperServiceIntegrationTests extends GroovyTestCase {
	static Logger log = LoggerFactory.getLogger(DomainHelperServiceIntegrationTests.class)
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	DomainHelperService domainHelperService
	
    void testExport() {
		log.info 'Starting Export Test.'
		createDataForImportExportTests()
		//domainHelperService.exportData (new File('C:/GrailsTests/'), ['*'])
		domainHelperService.exportDomains (new File('C:/GrailsTests/'), ['*'])
    }
	
	void testImport(){
		log.info 'Starting Import Test.'
		domainHelperService.importDomains (new File('C:/GrailsTests/'), ['*'])
	}
	
	
	void createDataForImportExportTests(){
			// Create a repository
		DataRepository repo1 = new DataRepository(name: 'test-repository')
		repo1.save()
		
			// Create a bunch of dataTags
		(0..19).each {
			def dataHolder = new LongDataHolder();
			dataHolder.valueClass = 'java.lang.Integer';
			dataHolder.value = it;
			dataHolder.timestamp = (new Date()).time;
			def dataTag = new DataTag();
			dataTag.updateSource = "test${it}"
			dataTag.dataHolder = dataHolder;
			dataTag.name = "testTagInt$it"
			dataTag.tagId = UUID.randomUUID();
			dataTag.metadata['testMetadata1'] = 'testMetadata1Value'
			dataTag.metadata['testMetadata2'] = 'testMetadata2Value'
			dataTag.propertyEntries['testpropertyEntries1'] = 'testpropertyEntries1Value'
			dataTag.propertyEntries['testpropertyEntries2'] = 'testpropertyEntries2Value'
			dataTag.repository = repo1
			dataTag.save()
		}
		
			// Create some stored routes
		(0..5).each {
			def rt = new StoredRoutes(name : "Route${it}",
										enable : false,
										targetUrl : "mbtcpTest:route${it}",
										routeDescription : "StoredRoute Test ${it}")
			rt.save()
		}
		
			// Create Polling Configuration
		def pollConfig = new PollingConfiguration( name : 'TestPollConfig',
													commRoute : StoredRoutes.findByName('Route0'),
													repoRoute : StoredRoutes.findByName('Route1'),
													refreshTagsBeforePoll : true,
													forceWriteDelta : true,
													period : 5000,
													configProperties : ['firstProp':'firstValue','secondProp':'secondValue'])
		
		pollConfig.save()
		
		def commServer = new CommServerSetup(name : 'TestPollConfig',
													commRoute : StoredRoutes.findByName('Route0'),
													repoRoute : StoredRoutes.findByName('Route1'),
													configProperties : ['firstProp':'firstValue','secondProp':'secondValue'])
		
		commServer.save()
		
		(0..10).each{
			def remPollTarget = new RemotePollTarget(tagId : UUID.randomUUID(),
														remoteAddress : "${it}",
														operation : 'READ',
														enabled : true,
														pollConfiguration : pollConfig)
			remPollTarget.save()
		}
		
		(0..10).each{
			def serverTarget = new ServerTarget(tagId : UUID.randomUUID(),
														operation : 'READ',
														enabled : true,
														serverSetup : commServer)
			serverTarget.save()
		}
		
	}
}
