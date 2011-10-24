package org.scadajack.grails.repository

import grails.test.*

import org.blubbo.grails.data.DataTag
import org.blubbo.grails.data.LongDataHolder

class DataRepositoryIntegTests extends GrailsUnitTestCase {
	
	DataTag dataTag;
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	DataTag createIndexedTag(int index){
		def dataHolder = new LongDataHolder();
		dataHolder.valueClass = 'java.lang.Integer';
		dataHolder.value = index;
		dataHolder.timestamp = (new Date()).time;
		
		dataTag = new DataTag();
		dataTag.updateSource = "test${index}"
		dataTag.dataHolder = dataHolder;
		dataTag.name = "testTagInt$index"
		dataTag.tagId = UUID.randomUUID();
		dataTag.metadata['testMetadata1'] = 'testMetadata1Value'
		dataTag.metadata['testMetadata2'] = 'testMetadata2Value'
		dataTag.propertyEntries['testpropertyEntries1'] = 'testpropertyEntries1Value'
		dataTag.propertyEntries['testpropertyEntries2'] = 'testpropertyEntries2Value'
		return dataTag
		
	}
	
    void testRepositoryImportExport() {
		
		def dataTag0 = createIndexedTag(0)
		if (!dataTag0.save()){
			dataTag0.errors.allErrors.each{
				println it
			}
		} 
		
		DataRepository dr = new DataRepository();
		dr.name = "testrepository"
		
		if (!dr?.save()){
			println 'Error Saving Repository'
			dr.errors.allErrors.each{
				println it
			}
		}
		
		(1..20).each{
			def dataTag = createIndexedTag(it)
			if (!dataTag.save()){
				dataTag.errors.allErrors.each{
					println it
				}
			} else {
				dr.addTag dataTag
			}
		}
		
		
		
		if (!dr?.save()){
			println 'Error Saving Repository'
			dr.errors.allErrors.each{
				println it
			}
		}
		
		StringWriter swRepo = new StringWriter();
		DataRepository.exportToWriter(swRepo)
		String swResult = swRepo.toString();
		println 'Printing Export of DataRepository:' + swResult 
		StringWriter swTags = new StringWriter();
		DataTag.exportToWriter swTags
		println 'Exporting data tags'
		
		
			// Now Import Repository from String
		println 'Now trying to import exported Repository and tags'
		StringReader srRepo = new StringReader(swResult)
		StringReader srTags = new StringReader(swTags.toString())
		
			// remove existing tags and repository so we can recreate them with import
		def tag = [] + dr.dataTags
		tag.each{
			dr.removeTag it
		}
		dr.save(flush:true)
		
		dr.dataTags.each{
			assert(it.repository == null)
		}
		
		dr.delete(flush:true)
		
		
		DataTag.importFromReader srTags
		DataRepository.importFromReader srRepo
		
		assertEquals(DataRepository.count(),1);
		def importRepository = DataRepository.findByName('testrepository')
		assertNotNull(importRepository?.dataTags);
		assertEquals(20,importRepository.dataTags?.size())
		importRepository.dataTags.each{
			assert(it.repository == importRepository)
		}
		
		println 'Repository Import succeeded'
    }
}
