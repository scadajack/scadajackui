package org.blubbo.grails.data

import grails.test.*

import org.scadajack.grails.repository.DataRepository

class DataTagIntegTests extends GrailsUnitTestCase {
	
	DataTag dataTag;
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testStringTag() {
		
		println ''
		println 'starting test'
		def dataRepository = new DataRepository()
		dataRepository.name = 'test-repository'
		dataRepository.save()
		
		def dataHolder = new StringDataHolder();
		dataHolder.valueClass = 'java.lang.String';
		dataHolder.value = 'stringValue';
		dataHolder.timestamp = (new Date()).time;
		
		dataTag = new DataTag();
		dataTag.updateSource = "test"
		dataTag.dataHolder = dataHolder;
		dataTag.name = 'testTagString'
		dataTag.tagId = UUID.randomUUID();
		dataTag.timestamp = (new Date()).getTime()
		dataTag.tagVersion = 0
		dataTag.repository = dataRepository
		dataTag.metadata['testMetadata1'] = 'testMetadata1Value'
		dataTag.metadata['testMetadata2'] = 'testMetadata2Value'
		dataTag.propertyEntries['testpropertyEntries1'] = 'testpropertyEntries1Value'
		dataTag.propertyEntries['testpropertyEntries2'] = 'testpropertyEntries2Value'
		
		if (!dataTag.save(flush:true)){
			println 'Error Saving Data Tag'
			dataTag.errors.allErrors.each{
				println it
			}
		}
		
		StringWriter sw = new StringWriter();
		
		println 'Printing Export of DataTag:' + DataTag.exportToWriter(sw)
		
			// Now Import Tag from String
		println 'Now trying to import exported tag'
		StringReader sr = new StringReader(sw.toString())
		
			// remove existing tag and repository so we verify that they're recreated
		dataTag.delete()
		dataRepository.delete(flush:true)
		
		DataTag.importFromReader sr
		
		dataTag = DataTag.findByName('testTagString')
		
		assertNotNull('Could not import tag', dataTag)
		def createdRepository = DataRepository.findByName('test-repository')
		assertNotNull('Repository not created for tag', dataRepository)
		assertTrue('Repository does not contain tag',createdRepository.dataTags.contains(dataTag))
		
		println 'Import succeeded'
    }
	
	void testLongTag() {
		
		println ''
		println 'starting Long import export test'
		def dataHolder = new LongDataHolder();
		dataHolder.valueClass = 'java.lang.Integer';
		dataHolder.value = 20;
		dataHolder.timestamp = (new Date()).time;
		
		dataTag = new DataTag();
		dataTag.updateSource = "test"
		dataTag.dataHolder = dataHolder;
		dataTag.name = 'testTagInt'
		dataTag.tagId = UUID.randomUUID();
		dataTag.timestamp = (new Date()).getTime()
		dataTag.tagVersion = 0
		dataTag.metadata['testMetadata1'] = 'testMetadata1Value'
		dataTag.metadata['testMetadata2'] = 'testMetadata2Value'
		dataTag.propertyEntries['testpropertyEntries1'] = 'testpropertyEntries1Value'
		dataTag.propertyEntries['testpropertyEntries2'] = 'testpropertyEntries2Value'
		
		if (!dataTag.save(flush:true)){
			println 'Error Saving Long Data Tag'
			dataTag.errors.allErrors.each{
				println it
			}
		}
		
		StringWriter sw = new StringWriter();
		
		println 'Printing Export of Long DataTag:' + DataTag.exportToWriter(sw)
		
		println 'Now trying to import exported Long tag'
			// Now Import Tag from String
		StringReader sr = new StringReader(sw.toString())
		
			// remove existing tag so we don't get a uniqueness error
		dataTag.delete()
		
		DataTag.importFromReader sr
		
		dataTag = DataTag.findByName('testTagInt')
		
		assertNotNull('Could not import Long tag', dataTag)
		
		println 'Import Long Tag succeeded'
		
	}
	
	void testDoubleTag() {
		
		println ''
		println 'starting Double Tag import export test'
		def dataHolder = new DoubleDataHolder();
		dataHolder.valueClass = 'java.lang.Float';
		dataHolder.value = 100.9;
		dataHolder.timestamp = (new Date()).time;
		
		dataTag = new DataTag();
		dataTag.updateSource = "test"
		dataTag.dataHolder = dataHolder;
		dataTag.name = 'testTagFloat'
		dataTag.tagId = UUID.randomUUID();
		dataTag.metadata['testMetadata1'] = 'testMetadata1Value'
		dataTag.metadata['testMetadata2'] = 'testMetadata2Value'
		dataTag.propertyEntries['testpropertyEntries1'] = 'testpropertyEntries1Value'
		dataTag.propertyEntries['testpropertyEntries2'] = 'testpropertyEntries2Value'
		
		
		if (!dataTag.save(flush:true)){
			println 'Error Saving Double Data Tag'
			dataTag.errors.allErrors.each{
				println it
			}
		}
		
		StringWriter sw = new StringWriter();
		//dataTag.exportToWriter sw
		println 'Printing Export of Double DataTag:' + DataTag.exportToWriter(sw)
		
		println 'Now trying to import exported Double tag'
			// Now Import Tag from String
		StringReader sr = new StringReader(sw.toString())
		
			// remove existing tag so we don't get a uniqueness error
		dataTag.delete()
		
		DataTag.importFromReader sr
		
		dataTag = DataTag.findByName('testTagFloat')
		
		assertNotNull('Could not import Double tag', dataTag)
		
		println 'Import succeeded'
		
	}
	
	void testBooleanTag() {
		
		println ''
		println 'starting Boolean Tag import export test'
		def dataHolder = new BooleanDataHolder();
		dataHolder.valueClass = 'java.lang.Boolean';
		dataHolder.value = true;
		dataHolder.timestamp = (new Date()).time;
		
		dataTag = new DataTag();
		dataTag.updateSource = "test"
		dataTag.dataHolder = dataHolder;
		dataTag.name = 'testTagBoolean'
		dataTag.tagId = UUID.randomUUID();
		dataTag.metadata['test_Metadata1'] = 'testMetadata1Value'
		dataTag.metadata['testMetadata2'] = 'testMetadata2Value'
		dataTag.propertyEntries['testpropertyEntries1'] = 'testpropertyEntries1Value'
		dataTag.propertyEntries['testpropertyEntries2'] = 'testpropertyEntries2Value'
		
		if (!dataTag.save(flush:true)){
			println 'Error Saving Boolean Data Tag'
			dataTag.errors.allErrors.each{
				println it
			}
		}
		
		StringWriter sw = new StringWriter();
		
		println 'Printing Export of Boolean DataTag:' + DataTag.exportToWriter(sw)
		
		println 'Now trying to import exported Boolean tag'
			// Now Import Tag from String
		StringReader sr = new StringReader(sw.toString())
		
			// remove existing tag so we don't get a uniqueness error
		dataTag.delete()
		
		DataTag.importFromReader sr
		
		dataTag = DataTag.findByName('testTagBoolean')
		
		assertNotNull('Could not import Boolean tag', dataTag)
		
		println 'Import  Boolean Tag succeeded'
		
	}
}
