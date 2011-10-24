package org.scadajack.grails.repository

import com.blubbo.grails.routing.StoredRoutes
import grails.converters.JSON
import org.blubbo.grails.data.DataTag

class DataRepositoryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def domainHelperService
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [dataRepositoryInstanceList: DataRepository.list(params), dataRepositoryInstanceTotal: DataRepository.count()]
    }

    def create = {
        def dataRepositoryInstance = new DataRepository()
		
        dataRepositoryInstance.properties = params
        return [dataRepositoryInstance: dataRepositoryInstance]
    }

	def createRouteFromName(String name){
		StoredRoutes repoRoute
		def repoRouteName = "repo:$name"
		def existingRoute = StoredRoutes.findByTargetUrl(repoRouteName)
		if (existingRoute && repoRoute != existingRoute){
			repoRoute = existingRoute
		} else {
			repoRoute = new StoredRoutes();
			repoRoute.targetUrl = repoRouteName
			repoRoute.name = "Repository-$name Route"
			repoRoute.routeDescription = "A Repository Route for $name"
			repoRoute.enable = true;
			repoRoute.save()
		}
		return repoRoute.id
	}
	
    def save = {
		boolean autoGen = params.autoGenerateRoute
		def supId = params['repoRoute.id']
		if (autoGen && supId=='null'){
			params['repoRoute.id'] = createRouteFromName(params.name)
		}
        def dataRepositoryInstance = new DataRepository(params)
		
        if (dataRepositoryInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), dataRepositoryInstance.id])}"
            redirect(action: "show", id: dataRepositoryInstance.id)
        }
        else {
            render(view: "create", model: [dataRepositoryInstance: dataRepositoryInstance])
        }
    }

    def show = {
        def dataRepositoryInstance = DataRepository.get(params.id)
        if (!dataRepositoryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), params.id])}"
            redirect(action: "list")
        }
        else {
            [dataRepositoryInstance: dataRepositoryInstance]
        }
    }

    def edit = {
        def dataRepositoryInstance = DataRepository.get(params.id)
        if (!dataRepositoryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [dataRepositoryInstance: dataRepositoryInstance]
        }
    }

    def update = {
        def dataRepositoryInstance = DataRepository.get(params.id)
        if (dataRepositoryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (dataRepositoryInstance.version > version) {
                    
                    dataRepositoryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'dataRepository.label', default: 'DataRepository')] as Object[], "Another user has updated this DataRepository while you were editing")
                    render(view: "edit", model: [dataRepositoryInstance: dataRepositoryInstance])
                    return
                }
            }
            dataRepositoryInstance.properties = params
            if (!dataRepositoryInstance.hasErrors() && dataRepositoryInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), dataRepositoryInstance.id])}"
                redirect(action: "show", id: dataRepositoryInstance.id)
            }
            else {
                render(view: "edit", model: [dataRepositoryInstance: dataRepositoryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def dataRepositoryInstance = DataRepository.get(params.id)
        if (dataRepositoryInstance) {
            try {
				removeAllTags
                dataRepositoryInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def removeAllTags = {
		def dataRepositoryInstance = DataRepository.get(params.id)
		if (dataRepositoryInstance){
			try {
				dataRepositoryInstance.clear()
			} catch (Exception e){
				
			}
			flash.message = "${message(code: 'repository.tags.cleared.message', args: [(dataRepositoryInstance?.name ?: 'null')])}"
			redirect(action:show, id: dataRepositoryInstance.id)
		}
	}
	
	def briefList = {
		try {
			def sortIndex = params.sidx ?: 'name';
			def sortOrder = params.sord ?: 'asc'
			
			def maxRows = Integer.valueOf(params.rows);
			def currentPage = Integer.valueOf(params.page) ?: 1
			def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows
		
			def repoList = DataRepository.list(max:maxRows, offset:rowOffset)
				
			def jsoncells = repoList.collect{ DataRepository pc ->
				[cell: [pc.id,pc.name ?: '',pc.repoRoute?.name ?: '']]
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
			jsoncells = minIdx <0 || maxIdx <0 ? [] : jsoncells[minIdx..maxIdx]
			
			def jsonData = [rows: jsoncells,
							page:currentPage,
							records:totalCount,
							total:numPages]
			render jsonData as JSON
		}catch (org.springframework.dao.DataIntegrityViolationException e) {
			flash.message = "${message(code: 'default.storedRoutes.retrieve.error', args: [message(code: 'StoredRoutes.label', default: 'Stored Routes'), params.id], default: 'Error Retrieving Stored Routes Properties')}"
			redirect(action: "show", id: params.id)
		}
	}
	
	def listEdit = {
		// msg format: [id:1, oper:add, pname:newname, pvalue:newvalue, action:commServerEditProps, controller:PollingConfiguration]
		def msg = "Could not edit DataRepository"
		def state = "FAIL"
		def id
		
			switch (params.oper){
				case 'add':
					
					def dataRepositoryInstance = new DataRepository() 
					dataRepositoryInstance.id = UUID.randomUUID().toString()
					bindData(dataRepositoryInstance,params,[exclude:["repoRoute","id"]]);
					if (params.repoRoute){
						StoredRoutes rr = StoredRoutes.get(params.repoRoute)
						if (rr != dataRepositoryInstance.repoRoute){
							dataRepositoryInstance.repoRoute = rr
						}
					}
					
					if (dataRepositoryInstance.save(flush: true)) {
						flash.message = "${message(code: 'default.created.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), dataRepositoryInstance.id])}"
						msg = "DataRepository id [ ${dataRepositoryInstance.id} ] added"
						id = "${dataRepositoryInstance.id}"
						state = "OK"
					} else if (dataRepositoryInstance.hasErrors()){
						msg = 'Error: '
						dataRepositoryInstance.errors.each{
							msg += it.toString() + '\n'
						}
						flash.message = msg
					}
					break;
				case 'edit':
					def dataRepositoryInstance  = DataRepository.get(params.id)
					if (dataRepositoryInstance) {
						if (params.version) {
							def version = params.version.toLong()
							if (dataRepositoryInstance.version > version) {
								
								dataRepositoryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'dataRepository.label', default: 'DataRepository')] as Object[], "Another user has updated this DataRepository while you were editing")
								render(view: "edit", model: [dataRepositoryInstance: dataRepositoryInstance])
								return
							}
						}
						
						bindData(dataRepositoryInstance,params,[exclude:["repoRoute"]]);
						if (params.repoRoute){
							StoredRoutes rr = StoredRoutes.get(params.repoRoute)
							if (rr != dataRepositoryInstance.repoRoute){
								dataRepositoryInstance.repoRoute = rr
							}
						}
						
						if (!dataRepositoryInstance.hasErrors() && dataRepositoryInstance.save(flush: true)) {
							def mc = message(code: "dataRepository.label", default: 'DataRepository')
							flash.message = "${message(code: 'default.updated.message', args: [mc, dataRepositoryInstance.id])}"
							//message = "${message(code: 'default.updated.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), dataRepositoryInstance.id])}"
							state = "OK"
							id = "${dataRepositoryInstance.id}"
						}
					}
					break;
					
				case 'del':
					def dataRepositoryInstance  = DataRepository.get(params.id)
					if (dataRepositoryInstance) {
						try {
							id = "${dataRepositoryInstance?.id}"
							dataRepositoryInstance.delete(flush: true)
							msg = "${message(code: 'default.deleted.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), params.id])}"
							state = "OK"
						}
						catch (org.springframework.dao.DataIntegrityViolationException e) {
							msg = "${message(code: 'default.not.deleted.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), params.id])}"
							
						}
					}
					else {
						msg = "${message(code: 'default.not.found.message', args: [message(code: 'dataRepository.label', default: 'DataRepository'), params.id])}"
					}
					break;
			}
		
		def response = [message:msg.toString(), state:state,id:id]
		render response as JSON
	}
	
	def dataTagList = {
		def dataRepositoryInstance = DataRepository.get(params.id)
		
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
			eq('repository',dataRepositoryInstance)
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
	
	/**
	 * Similar to dataTagList but provides list of tags that are NOT part of the 
	 * specified repository. i.e. tags that may be added to the repository.
	 */
	def addDataTagList = {
		def dataRepositoryInstance = DataRepository.get(params.id)
		
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
			ne('repository',dataRepositoryInstance)
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

	def getRepositoriesOptions = {
		def repos = DataRepository.list()
		
		def repoOptions = repos.collect{
			"""<option value=${it.id}>${it.name}</option>"""
		}
		def domResult = "<select>" + repoOptions.join('') + "</select>"
		render domResult
	}

}
