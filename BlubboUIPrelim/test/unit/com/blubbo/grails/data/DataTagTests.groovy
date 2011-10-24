package com.blubbo.grails.data

import grails.test.*

import org.blubbo.grails.data.DataTag
import org.blubbo.grails.data.StringDataHolder

class DataTagTests extends GrailsUnitTestCase {
	
	DataTag dataTag;
	
    protected void setUp() {
        super.setUp()
		def dataHolder = new StringDataHolder();
		dataHolder.valueClass = java.lang.String;
		dataHolder.value = 'stringValue';
		dataHolder.timestamp = (new Date()).time;
		dataTag = new DataTag();
		dataTag.dataHolder = dataHolder;
		dataTag.name = 'testTag'
		dataTag.tagId = UUID.randomUUID();
		
		
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {
		println ''
		println 'starting test'
		StringWriter sw = new StringWriter();
		//dataTag.exportToWriter sw
		println 'Printing Export of DataTag:' + dataTag.exportToWriter(sw)
    }
}
