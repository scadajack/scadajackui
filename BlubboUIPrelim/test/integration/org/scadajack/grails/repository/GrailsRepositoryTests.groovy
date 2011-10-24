package org.scadajack.grails.repository

import grails.test.*

import org.blubbo.grails.data.AbstractDataHolder
import org.blubbo.grails.data.DataTag
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GrailsRepositoryTests extends GroovyTestCase {
	static Logger log = LoggerFactory.getLogger(GrailsRepositoryTests.class)
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	GrailsRepository gr
	List tagNames = ['firstTag','secondTag']
	
	void setupData(){
		DataTag.sjDataTagMap.clear()
		def repoName = "testRepository"
		gr = new GrailsRepository(repoName)
		
		DataRepository dr = new DataRepository(name : repoName)
		if (!dr.save()){
			log.warn "Error saving data repository ${repoName}."
			if (dr.hasErrors()){
				log.warn "Errors are: ${dr.errors.allErrors}"
			}
		}
		
		if (!dr){
			log.warn "Could not retrieve data repository instance: $repoName"
		}
		
		def dh = AbstractDataHolder.getDataHolderForClass('Long')
		Class clazz = java.lang.Long
		dh.valueClass = clazz
		dh.value = 20L
		DataTag dt = new DataTag(name : tagNames[0],
									repository : dr,
									dataHolder : dh)
		if (!dt.save()){
			log.warn "Error saving dataTag ${tagNames[0]}."
			if (dt.hasErrors()){
				log.warn "Errors are: ${dt.errors.allErrors}"
			}
		}
		
		dh = AbstractDataHolder.getDataHolderForClass('Long')
		dh.valueClass = java.lang.Long
		dh.value = 30L
		dt = new DataTag(name : tagNames[1],
									repository : dr,
									dataHolder : dh)
		if (!dt.save()){
			log.warn "Error saving dataTag ${tagNames[1]}."
			if (dt.hasErrors()){
				log.warn "Errors are: ${dt.errors.allErrors}"
			}
		}
		
	
	}
	
    void testRetrieveSameTagObject() {
		setupData()
		
			// First get DataTag from Grails
		DataTag dt = DataTag.findByName(tagNames[0])
		assertNotNull(dt)
		UUID tagId = dt.tagId
		String name = dt.name
		
		def tagById = gr.getElement(tagId)
		def tagByName = gr.getElement(name)
		
		assertTrue('Tag retrieved by name and by id are not the same!',tagById.is(tagByName))
    }
	
	void testUpdateTagObjectDirectAndRetrieve(){
		setupData()
		
		DataTag dt = DataTag.findByName(tagNames[0])
		assertNotNull(dt)
		UUID tagId = dt.tagId
			// Get an IDataTag for the DataTag
		def tagById = gr.getElement(tagId)
		def originalSjTag = tagById				// Want to hold on to this for later
		
		Long newValue = 12345l
			// Now we want to update it.
		def updatedTag = tagById.withNewValue(newValue,'testUpdateTagObjectAndRetrieve')
		
		gr.updateTag(updatedTag,tagById)
		
		def updatedTagById = gr.getElement(tagId)
		
		assertNotNull('Updated Tag could not be retrieved',updatedTagById)
		assertTrue('Updated Tag retrieved was not different than original', !updatedTagById.is(tagById))
		def val = updatedTagById.getValue()
		assertTrue('New Tags Value didnt update',val==newValue)
		
			// Now try to update but use the original tag. This should fail since we're using the 
			// protected update (by supplying the old tag)
		newValue = 54321l
		updatedTag = updatedTag.withNewValue(newValue,'testUpdateTagObjectAndRetrieve')
		
		def result = gr.updateTag(updatedTag,originalSjTag)
		
		assertTrue("Should have received null result on bad update but got $result",result == null)
		assertTrue('Retrieved Tag should not have updated, but appears to have the new value', gr.getElement(tagId).value != newValue)
		
		
	}

	void testUpdateTagObjectViaSjTagAndRetrieve(){
		setupData()
		
		DataTag dt = DataTag.findByName(tagNames[0])
		assertNotNull(dt)
		UUID tagId = dt.tagId
			// Get an IDataTag for the DataTag
		def tagById = gr.getElement(tagId)
		def originalSjTag = tagById				// Want to hold on to this for later
		
		Long newValue = 12345l
			// Now we want to update it.
		def updatedTag = tagById.withNewValue(newValue,'testUpdateTagObjectAndRetrieve')
		
		gr.updateTag(updatedTag,tagById)
		
		def updatedTagById = gr.getElement(tagId)
		
		assertNotNull('Updated Tag could not be retrieved',updatedTagById)
		assertTrue('Updated Tag retrieved was not different than original', !updatedTagById.is(tagById))

		def val = updatedTagById.getValue()
		assertTrue('New Tags Value didnt update',val==newValue)
		
		
			// Now try to update but use the original tag. This should fail since we're using the
			// protected update (by supplying the old tag)
		newValue = 54321l
		updatedTag = updatedTag.withNewValue(newValue,'testUpdateTagObjectAndRetrieve')
		
		def result = gr.updateTag(updatedTag,originalSjTag)
		
		assertTrue("Should have received null result on bad update but got $result",result == null)
		assertTrue('Retrieved Tag should not have updated, but appears to have the new value', gr.getElement(tagId).value != newValue)
		
		
	}

	
	
}
