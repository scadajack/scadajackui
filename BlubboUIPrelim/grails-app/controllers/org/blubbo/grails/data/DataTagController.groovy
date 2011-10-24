package org.blubbo.grails.data

import org.blubbo.data.IDataTag
import org.blubbo.data.ITagMetadata;
import org.scadajack.grails.repository.GrailsRepository
import org.scadajack.grails.repository.DataRepository

import grails.converters.JSON


class DataTagController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def dataRepositoryService
	def domainHelperService


	def index = {
		redirect(action: "list", params: params)
	}

	def list = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		params.fetch = [dataHolder:"eager"]

		def errMessage;
		def returnResult;
		def res = (1..10).find{
			try {
				returnResult = [dataTagInstanceList: DataTag.list(params), dataTagInstanceTotal: DataTag.count()]
				true
			} catch (Exception e){
				errMessage = "${e}"
				false
			}
		}

		if (!res){
			flash.message = "Error listing data tags. Error: ${errMessage}"
			render(url:"index")
		} else {
			return returnResult
		}



	}

	def create = {
		def dataTagInstance = new DataTag()
		dataTagInstance.properties = params
		return [dataTagInstance: dataTagInstance]
	}

	def save = {
		def newDataTag = new DataTag()
		def dataHolderClass = params.dataHolder.valueClass
		AbstractDataHolder dataHolder = AbstractDataHolder.getDataHolderForClass(dataHolderClass)
		dataHolder.valueClassName = dataHolderClass
		newDataTag.dataHolder = dataHolder
		bindData(newDataTag,params,[exclude:['metadata','dataHolder.valueClass']])
		params.metadata.key.eachWithIndex{k,i ->
			if (k)
				newDataTag.metadata[k] = params.metadata.value[i]
		}

		newDataTag.metadata[ITagMetadata.UPDATE_SOURCE] = 'UI'

		params.propertyEntries.key.eachWithIndex{k,i ->
			if (k)
				newDataTag.propertyEntries[k] = params.propertyEntries.value[i]
		}

		if (newDataTag.repository){
			IDataTag<?> sjTag = dataRepositoryService.getSjTagForDataTag(newDataTag.repository.name,newDataTag)
			GrailsRepository repo = dataRepositoryService.getMemoryRepository(newDataTag.repository.name)
			repo.addTag(sjTag)
		}
		def dataTag = DataTag.findByTagId(newDataTag.tagId)
		redirect(action: "show", id: dataTag.id)
	}

	def show = {
		def dataTagInstance = DataTag.get(params.id)
		if (!dataTagInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataTag.label', default: 'DataTag'), params.id])}"
			redirect(action: "list")
		}
		else {
			[dataTagInstance: dataTagInstance]
		}
	}

	def edit = {
		def dataTagEditInstance = getWorkingTag(session, params.id)
		if (!dataTagEditInstance || !dataTagEditInstance.dt) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataTag.label', default: 'DataTag'), params.id])}"
			redirect(action: "list")
		}
		else {
			if (!session.editInstance){
				session.editInstance = [:]
			}
			session.editInstance.put("${dataTagEditInstance.id}".toString(), dataTagEditInstance);

			return [dataTagInstance: dataTagEditInstance.dt, dataTagInstanceId:dataTagEditInstance.id]
		}
	}

	class DataTagEditCommand {

		def id
		DataTag dt
		def changed

		DataTagEditCommand(DataTag dt){
			if (dt){
				this.dt = getDataTagCopy(dt)
				id = dt.id
			}
		}
	}

	/**
	 * Method to create a working copy tag for editing. A DataTagEditCommand instance is 
	 * instantiated to hold this temporary tag and the session stores this tag in a map 
	 * referenced by id. When the edits are committed, this working copy is used to update 
	 * the current tag value in the database.
	 * @param session
	 * @param id
	 * @return
	 */
	DataTagEditCommand getWorkingTag(def session, def id){
		if (!session.editInstance){
			session.editInstance = [:]
		}
		if (!session.editInstance[id]){
			def dataTagInstance = DataTag.get(id)
			def editCmd = new DataTagEditCommand(dataTagInstance)
			session.editInstance.put("${editCmd.id}".toString(), editCmd);
		}
		session.editInstance[id]
	}
	
	DataTagEditCommand removeWorkingTag(def session, def id){
		if (!session.editInstance){
			session.editInstance = [:]
		}
		DataTagEditCommand dtec
		if (session.editInstance[id]){
			dtec = session.editInstance[id]
			session.editInstance.remove(id)
		}
		return dtec
	}
	

	def update = {
		/* def dataTagInstance = DataTag.get(params.id)
		 if (dataTagInstance) {
		 if (params.version) {
		 def version = params.version.toLong()
		 if (dataTagInstance.version > version) {
		 dataTagInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'dataTag.label', default: 'DataTag')] as Object[], "Another user has updated this DataTag while you were editing")
		 render(view: "edit", model: [dataTagInstance: dataTagInstance])
		 return
		 }
		 }
		 //dataTagInstance.properties = params
		 * 
		 */
		/*
		 * We create a DataTag from the update parameters, then create an SjTag for it. And 
		 * finally, we submit it to the memoryRepository. The memoryRepository will then update 
		 * the Database with the new value.
		 */
		def o2 = params
		def ss = session
		def dataTagEditInstance = getWorkingTag(ss,o2.id)
		session.editInstance.remove(o2.id)
		def newDataTag = new DataTag()
		def dataHolderClass = params.dataHolder.valueClass
		AbstractDataHolder dataHolder = AbstractDataHolder.getDataHolderForClass(dataHolderClass)
		dataHolder.valueClassName = dataHolderClass
		newDataTag.dataHolder = dataHolder
		bindData(newDataTag,params,[exclude:['metadata','propertyEntries','dataHolder.valueClass']])

		dataTagEditInstance.dt.metadata.each{k,v ->
			newDataTag.metadata[k] = v
		}
		//params.metadata.key.eachWithIndex{k,i ->
		//	if (k)
		//		newDataTag.metadata[k] = params.metadata.value[i]
		//}

		newDataTag.metadata[ITagMetadata.UPDATE_SOURCE] = 'UI'

		dataTagEditInstance.dt.propertyEntries.each{k,v ->
			newDataTag.propertyEntries[k]=v
		}

		//def value = params.dataHolder.value
		//dataHolder.value=value

		if (newDataTag.repository){
			IDataTag<?> sjTag = dataRepositoryService.getSjTagForDataTag(newDataTag.repository.name,newDataTag)
			GrailsRepository repo = dataRepositoryService.getMemoryRepository(newDataTag.repository.name)
			repo.updateTag(sjTag,null)
		}
		def dataTag = DataTag.findByTagId(newDataTag.tagId)
		redirect(action: "edit", id: dataTag.id)

		/*			
		 if (!dataTagInstance.hasErrors() && dataTagInstance.save(flush: true)) {
		 flash.message = "${message(code: 'default.updated.message', args: [message(code: 'dataTag.label', default: 'DataTag'), dataTagInstance.id])}"
		 redirect(action: "show", id: dataTagInstance.id)
		 }
		 else {
		 render(view: "edit", model: [dataTagInstance: dataTagInstance])
		 }
		 }
		 else {
		 flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataTag.label', default: 'DataTag'), params.id])}"
		 redirect(action: "list")
		 }
		 */        
	}

	def delete = {
		def dataTagInstance = DataTag.get(params.id)
		if (dataTagInstance) {
			try {
				dataTagInstance.repository?.removeTag dataTagInstance
				dataTagInstance.delete(flush: true)
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'dataTag.label', default: 'DataTag'), params.id])}"
				redirect(action: "list")
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'dataTag.label', default: 'DataTag'), params.id])}"
				redirect(action: "show", id: params.id)
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataTag.label', default: 'DataTag'), params.id])}"
			redirect(action: "list")
		}
	}

	def dataTagList = {
		def sortIndex = params.sidx ?: 'name';
		def sortOrder = params.sord ?: 'asc'
		def sortProp = sortIndex;
		switch (sortIndex){
			case 'name' : sortProp = 'name'; break;
			case 'repository'	: sortProp = 'repository'; break;
			case 'value'	: sortProp = 'd.value'; break;
			case 'type'		: sortProp = 'd.valueClassName';break;
		}


		def maxRows = Integer.valueOf(params.rows);
		def currentPage = Integer.valueOf(params.page) ?: 1
		def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows
		def c = DataTag.createCriteria();
		def dtList = c.list(max:maxRows, offset:rowOffset){
			createAlias ('dataHolder','d')

			if (params.name)
				ilike('name',params.name+'%')

			if (params.type)
				ilike('d.valueClassName','%'+params.type+'%')

			order(sortProp, sortOrder).ignoreCase()
		}


		def jsoncells = dtList.collect{ DataTag tgt ->

			[cell: [tgt.id,
					tgt.tagId.toString(),
					domainHelperService.uuidShortner(tgt.tagId),
					tgt.name,
					tgt.dataHolder.value,
					tgt.dataHolder.valueClass.simpleName,
					formatDate(date:new Date(tgt.timestamp), type:'datetime'),
					tgt.repository.name]]
		};

		def totalCount = DataTag.count();
		def numPages = Math.ceil(totalCount/maxRows);

		def jsonData = [rows: jsoncells,
					page:currentPage,
					records:totalCount,
					total:numPages];
		render jsonData as JSON;
	}

	def dataTagEdit = {
		// msg format: [id:1, oper:add, pname:newname, pvalue:newvalue, action:commServerEditProps, controller:commServerSetup]
		def message = ''
		def state = "FAIL"
		def id


		switch (params.oper){

			case 'add':
				def newDataTag = new DataTag()
				try{
					def dataHolderClass = params.type
					AbstractDataHolder dataHolder = AbstractDataHolder.getDataHolderForClass(dataHolderClass)
					dataHolder.valueClassName = dataHolderClass
					bindData(dataHolder,params,[include:'value'])
					//dataHolder.value= params.value
					newDataTag.dataHolder = dataHolder

					def drepo = params.repository ? DataRepository.get(params.repository) : null
					newDataTag.repository = drepo ?: null
					newDataTag.name = params.name

					/*					
				 params.metadata.key.eachWithIndex{k,i ->
				 if (k)
				 newDataTag.metadata[k] = params.metadata.value[i]
				 }
				 newDataTag.metadata[ITagMetadata.UPDATE_SOURCE] = 'UI'
				 params.propertyEntries.key.eachWithIndex{k,i ->
				 if (k)
				 newDataTag.propertyEntries[k] = params.propertyEntries.value[i]
				 }
				 */					
					if (newDataTag.repository){
						IDataTag<?> sjTag = dataRepositoryService.getSjTagForDataTag(newDataTag.repository.name,newDataTag)
						GrailsRepository repo = dataRepositoryService.getMemoryRepository(newDataTag.repository.name)
						repo.addTag(sjTag)
					}
					state = "OK"
					message = "DataTag ${newDataTag?.name} added to repository: ${newDataTag?.repository?.name ?: 'no repository specified'}"
				} catch (Exception e){
					message = "DataTag ${newDataTag?.name} not added due to error: ${e}"
					flash.message = message;
				}

				break;
			case 'del':
				String paramsList = params.id
				def dids
				if (paramsList instanceof String){
					dids = paramsList.split(/\,/)
				} else {
					dids = paramsList
				}
				dids.each{
					try {
						def dataTagInstance = DataTag.get(it)
						if (dataTagInstance){
							dataTagInstance.repository?.removeTag dataTagInstance
							dataTagInstance.delete(flush: true)
							state = "OK"
							message += "DataTag ${dataTagInstance.name} removed from repository and deleted."
						}
					} catch (Exception e){
						message += "DataTag ${dataTagInstance?.name} not removed due to error: ${e}"
					}
				}

			/*
			 def dataTag = CommServerSetup.get(params.id)
			 if (commServerSetupInstance){
			 def targetId = params.delId ? Long.valueOf(params.delId) : null;
			 if (targetId){
			 def targets = commServerSetupInstance.targets
			 def target = ServerTarget.get(targetId)
			 commServerSetupInstance.removeFromTargets(target);
			 target.delete(flush:true)
			 if (!commServerSetupInstance.hasErrors() && commServerSetupInstance.save()){
			 message = "Target ${targetId} deleted"
			 state = "OK"
			 } else {
			 message = "Target delete error ${targetId}"
			 }
			 }
			 }
			 */	
				break;
			default: message = "Unkown Command: ${params.oper} specified for DataTagController.dataTagEdit"
		}
		def response = [message:message.toString(), state:state,id:id]
		render response as JSON
	}

	def briefList = {
		def sortIndex = params.sidx ?: 'name';
		def sortOrder = params.sord ?: 'asc'
		def sortProp = sortIndex;
		switch (sortIndex){
			case 'name' : sortProp = 'name'; break;
			case 'repository'	: sortProp = 'repository'; break;
			case 'value'	: sortProp = 'd.value'; break;
			case 'type'		: sortProp = 'd.valueClassName';break;
		}


		def maxRows = Integer.valueOf(params.rows);
		def currentPage = Integer.valueOf(params.page) ?: 1
		def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows
		def c = DataTag.createCriteria();
		def dtList = c.list(max:maxRows, offset:rowOffset){
			createAlias ('dataHolder','d')

			if (params.name)
				ilike('name',params.name+'%')

			if (params.type)
				ilike('d.valueClassName','%'+params.type+'%')

			order(sortProp, sortOrder).ignoreCase()
		}


		def jsoncells = dtList.collect{ DataTag tgt ->

			[cell: [tgt.id,
					tgt.name,
					tgt.dataHolder.value,
					tgt.dataHolder.valueClass.simpleName,
					tgt.tagId.toString(),
					tgt.repository.name]]
		};

		def totalCount = DataTag.count();
		def numPages = Math.ceil(totalCount/maxRows);

		def jsonData = [rows: jsoncells,
					page:currentPage,
					records:totalCount,
					total:numPages];
		render jsonData as JSON;
	}

	def metadataList = {
		def dataTagInstance = getWorkingTag(session,params.id)
		if (dataTagInstance){
			try {
				def sortIndex = params.sidx ?: 'name';
				def sortOrder = params.sord ?: 'asc'

				def maxRows = Integer.valueOf(params.rows);
				def currentPage = Integer.valueOf(params.page) ?: 1
				def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows

				def jsoncells = dataTagInstance.dt.metadata.collect{ k,v ->
					[cell: [k,v]]
				}

				jsoncells = jsoncells.sort{ x1,x2 ->
					def c1 = x1.cell[sortIndex != 'value' ? 0 : 1] ?: ''
					def c2 = x2.cell[sortIndex != 'value' ? 0 : 1] ?: ''

					return sortOrder != 'desc' ? c1.compareTo(c2) : c2.compareTo(c1)
				}

				def totalCount = jsoncells.size
				def numPages = Math.ceil(totalCount/maxRows)
				def minIdx = Math.min(rowOffset,totalCount-1)
				def maxIdx = Math.min(totalCount-1, rowOffset+maxRows-1)
				jsoncells = minIdx < 0 || maxIdx < 0 ? [] : jsoncells[minIdx..maxIdx]

				def jsonData = [rows: jsoncells,
							page:currentPage,
							records:totalCount,
							total:numPages]
				render jsonData as JSON
			}catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.dataTagMetadata.retrieve.error', args: [message(code: 'DataTag.label', default: 'Data Tag'), params.id], default: 'Error Retrieving DataTag Metadata')}"
				redirect(action: "show", id: params.id)
			}
		}


	}

	def metadataEdit = {
		// msg format: [id:1, oper:add, pname:newname, pvalue:newvalue, action:commServerEditProps, controller:commServerSetup]
		def message = "Could not save property with name: ${params.name}, value: ${params.value}"
		def state = "FAIL"
		def id
		//def dataTagInstance = DataTag.get(params.id)
		def dataTagInstance = getWorkingTag(session,params.id)
		if (dataTagInstance){
			switch (params.oper){
				case 'add':
					dataTagInstance?.dt?.metadata."${params.name}" = "${params.value}".toString()
					dataTagInstance.changed = true;
					break;
				case 'edit':
					def mp = dataTagInstance?.dt?.metadata
					if (mp.containsKey("${params.name}".toString())){
						mp."${params.name}" = "${params.value}".toString()
						message = "DataTag Metadata Property ${params.name} value changed to ${params.value} "
						id = "${params.name}"
						state = "OK"
					}
					break;
				case 'del':
					def mp = dataTagInstance?.dt?.metadata
					if (mp.containsKey("${params.delId}".toString())){
						mp.remove("${params.delId}".toString())
						message = "DataTag Metadata Property [ ${params.delId} : ${params.value} ] removed"
						id = "${params.delId}"
						state = "OK"
					}
					break;
			}
		}
		def response = [message:message.toString(), state:state,id:id]
		render response as JSON
	}

	def propertyList = {
		def dataTagInstance = getWorkingTag(session,params.id)
		if (dataTagInstance){
			try {
				def sortIndex = params.sidx ?: 'name';
				def sortOrder = params.sord ?: 'asc'

				def maxRows = Integer.valueOf(params.rows);
				def currentPage = Integer.valueOf(params.page) ?: 1
				def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows

				def jsoncells = dataTagInstance.dt.propertyEntries.collect{ k,v ->
					[cell: [k,v]]
				}

				jsoncells = jsoncells.sort{ x1,x2 ->
					def c1 = x1.cell[sortIndex != 'value' ? 0 : 1] ?: ''
					def c2 = x2.cell[sortIndex != 'value' ? 0 : 1] ?: ''

					return sortOrder != 'desc' ? c1.compareTo(c2) : c2.compareTo(c1)
				}

				def totalCount = jsoncells.size
				def numPages = Math.ceil(totalCount/maxRows)
				def minIdx = Math.min(rowOffset,totalCount-1)
				def maxIdx = Math.min(totalCount-1, rowOffset+maxRows-1)
				jsoncells = minIdx < 0 || maxIdx < 0 ? [] : jsoncells[minIdx..maxIdx]

				def jsonData = [rows: jsoncells,
							page:currentPage,
							records:totalCount,
							total:numPages]
				render jsonData as JSON
			}catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.dataTagPropertyEntries.retrieve.error', args: [message(code: 'DataTag.label', default: 'Data Tag'), params.id], default: 'Error Retrieving DataTag PropertyEntries')}"
				redirect(action: "show", id: params.id)
			}
		}


	}

	def propertyEdit = {
		// msg format: [id:1, oper:add, pname:newname, pvalue:newvalue, action:commServerEditProps, controller:commServerSetup]
		def message = "Could not save property with name: ${params.name}, value: ${params.value}"
		def state = "FAIL"
		def id
		//def dataTagInstance = DataTag.get(params.id)
		def dataTagInstance = getWorkingTag(session,params.id)
		if (dataTagInstance){
			switch (params.oper){
				case 'add':
					dataTagInstance?.dt?.propertyEntries."${params.name}" = "${params.value}".toString()
					dataTagInstance.changed = true;
					break;
				case 'edit':
					def mp = dataTagInstance?.dt?.propertyEntries
					if (mp.containsKey("${params.name}".toString())){
						mp."${params.name}" = "${params.value}".toString()
						message = "DataTag Property Entry ${params.name} value changed to ${params.value} "
						id = "${params.name}"
						state = "OK"
					}
					break;
				case 'del':
					def mp = dataTagInstance?.dt?.propertyEntries
					if (mp.containsKey("${params.delId}".toString())){
						mp.remove("${params.delId}".toString())
						message = "DataTag Property Entry [ ${params.delId} : ${params.value} ] removed"
						id = "${params.delId}"
						state = "OK"
					}
					break;
			}
		}
		def response = [message:message.toString(), state:state,id:id]
		render response as JSON
	}

	def knownDataHolderClasses = {
		def availClasses = org.blubbo.grails.data.AbstractDataHolder.knownDataHolderClassesClasses

		def classOptions = availClasses.collect{
			"""<option value=${it.getName()}>${it.getSimpleName()}</option>"""
		}
		def domResult = "<select>" + classOptions.join('') + "</select>"
		render domResult
	}

	DataTag getDataTagCopy(DataTag tag){
		def copyTag = new DataTag(
				name : tag.name,
				tagId: tag.tagId,
				tagVersion: tag.tagVersion,
				timestamp: tag.timestamp,
				)
		copyTag.metadata << tag.metadata
		copyTag.propertyEntries << tag.propertyEntries
		copyTag.dataHolder = tag.dataHolder.copy()
		copyTag.repository = tag.repository
		return copyTag
	}
}
