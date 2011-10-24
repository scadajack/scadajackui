package com.blubbo.grails.routing

import grails.converters.JSON
import org.scadajack.grails.util.DomainHelperService

class StoredRoutesController {

	
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	DomainHelperService domainHelperService
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [storedRoutesInstanceList: StoredRoutes.list(params), storedRoutesInstanceTotal: StoredRoutes.count()]
    }

    def create = {
        def storedRoutesInstance = new StoredRoutes()
        storedRoutesInstance.properties = params
        return [storedRoutesInstance: storedRoutesInstance]
    }

    def save = {
        def storedRoutesInstance = new StoredRoutes(params)
        if (storedRoutesInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), storedRoutesInstance.id])}"
            redirect(action: "show", id: storedRoutesInstance.id)
        }
        else {
            render(view: "create", model: [storedRoutesInstance: storedRoutesInstance])
        }
    }

    def show = {
        def storedRoutesInstance = StoredRoutes.get(params.id)
        if (!storedRoutesInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), params.id])}"
            redirect(action: "list")
        }
        else {
            [storedRoutesInstance: storedRoutesInstance]
        }
    }

    def edit = {
        def storedRoutesInstance = StoredRoutes.get(params.id)
        if (!storedRoutesInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [storedRoutesInstance: storedRoutesInstance]
        }
    }

    def update = {
        def storedRoutesInstance = StoredRoutes.get(params.id)
        if (storedRoutesInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (storedRoutesInstance.version > version) {
                    
                    storedRoutesInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'storedRoutes.label', default: 'StoredRoutes')] as Object[], "Another user has updated this StoredRoutes while you were editing")
                    render(view: "edit", model: [storedRoutesInstance: storedRoutesInstance])
                    return
                }
            }
            storedRoutesInstance.properties = params
            if (!storedRoutesInstance.hasErrors() && storedRoutesInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), storedRoutesInstance.id])}"
                redirect(action: "show", id: storedRoutesInstance.id)
            }
            else {
                render(view: "edit", model: [storedRoutesInstance: storedRoutesInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def storedRoutesInstance = StoredRoutes.get(params.id)
        if (storedRoutesInstance) {
            try {
					// First delete references to the stored route
				domainHelperService.removeRouteReferences(storedRoutesInstance)
					// Then can delete the route.
                storedRoutesInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def briefList = {
		try {
			def sortIndex = params.sidx ?: 'name';
			def sortOrder = params.sord ?: 'asc'
			
			def maxRows = Integer.valueOf(params.rows);
			def currentPage = Integer.valueOf(params.page) ?: 1
			def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows
		
			def routeList = StoredRoutes.list(max:maxRows, offset:rowOffset)
				
			def jsoncells = routeList.collect{ StoredRoutes pc ->
				[cell: [pc.id,pc.name,pc.targetUrl,pc.enable,pc.routeDescription]]
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
		def msg = "Could not edit Stored Route"
		def state = "FAIL"
		def id
		
			switch (params.oper){
				case 'add':
					def storedRouteInstance = new StoredRoutes(params)
					if (storedRouteInstance.save(flush: true)) {
						flash.message = "${message(code: 'default.created.message', args: [message(code: 'storedRoute.label', default: 'storedRoute'), storedRouteInstance.id])}"
						msg = "Stored Route id [ ${storedRouteInstance.id} ] added"
						id = "${storedRouteInstance.id}"
						state = "OK"
					} else {
						def estr = storedRouteInstance.hasErrors() ? storedRouteInstance.errors.allErrors.toString() : 'Unknown save error'
						flash.message = estr
						log.warn(estr)
						msg = estr
						
					}
					break;
				case 'edit':
					def storedRouteInstance  = StoredRoutes.get(params.id)
					if (storedRouteInstance) {
						if (params.version) {
							def version = params.version.toLong()
							if (storedRouteInstance.version > version) {
								
								storedRouteInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'storedRoutes.label', default: 'StoredRoutes')] as Object[], "Another user has updated this Stored Route while you were editing")
								render(view: "list")
								return
							}
						}
						
						storedRouteInstance.properties = params
						bindData(storedRouteInstance,params,[exclude:'enabled'])
						storedRouteInstance.enable = params.enabled == 'true'
						
						if (!storedRouteInstance.hasErrors() && storedRouteInstance.save(flush: true)) {
							def mc = message(code: "storedRoutes.label", default: 'StoredRoutes')
							flash.message = "${message(code: 'default.updated.message', args: [mc, storedRouteInstance.id])}"
							//message = "${message(code: 'default.updated.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), storedRoutesInstance.id])}"
							state = "OK"
							id = "${storedRouteInstance.id}"
						}
					}
					break;
					
				case 'del':
					def storedRouteInstance  = StoredRoutes.get(params.id)
					if (storedRouteInstance) {
						try {
							id = "${storedRouteInstance?.id}"
							storedRouteInstance.delete(flush: true)
							msg = "${message(code: 'default.deleted.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), params.id])}"
							state = "OK"
						}
						catch (org.springframework.dao.DataIntegrityViolationException e) {
							msg = "${message(code: 'default.not.deleted.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), params.id])}"
							
						}
					}
					else {
						msg = "${message(code: 'default.not.found.message', args: [message(code: 'storedRoutes.label', default: 'StoredRoutes'), params.id])}"
					}
					break;
			}
		
		def response = [message:msg.toString(), state:state,id:id]
		render response as JSON
	}
	
	def storedRoutesList = {
		def list = StoredRoutes.list();
		def listOptions = list.collect{
			"""<option value=${it.id}>${it.name}</option>"""
		}
		def domResult = "<select>" + listOptions.join('') + "</select>"
		render domResult
	}
}
