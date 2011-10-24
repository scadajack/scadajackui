package org.scadajack.grails.comm

import grails.converters.JSON
import org.blubbo.grails.data.DataTag
import com.blubbo.grails.routing.StoredRoutes

class PollingConfigurationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [pollingConfigurationInstanceList: PollingConfiguration.list(params), pollingConfigurationInstanceTotal: PollingConfiguration.count()]
    }

    def create = {
        def pollingConfigurationInstance = new PollingConfiguration()
        pollingConfigurationInstance.properties = params
        return [pollingConfigurationInstance: pollingConfigurationInstance]
    }

    def save = {
        def pollingConfigurationInstance = new PollingConfiguration(params)
        if (pollingConfigurationInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), pollingConfigurationInstance.id])}"
            redirect(action: "show", id: pollingConfigurationInstance.id)
        }
        else {
            render(view: "create", model: [pollingConfigurationInstance: pollingConfigurationInstance])
        }
    }
	
	def ajaxSave = {
		def message = "Could not create new PollingConfiguration"
		def state = "FAIL"
		def id
		def result
		def pollingConfigurationInstance = new PollingConfiguration(params)
		if (pollingConfigurationInstance.save(flush: true)) {
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), pollingConfigurationInstance.id])}"
			
		}
		else {
			id = pollingConfigurationInstance.id
			message = "PollingConfiguration saved with id=${id}"
			state = "SUCCESS"
		}
		return [message:message,state:state,id:id] as JSON
	}

    def show = {
        def pollingConfigurationInstance = PollingConfiguration.get(params.id)
        if (!pollingConfigurationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), params.id])}"
            redirect(action: "list")
        }
        else {
            [pollingConfigurationInstance: pollingConfigurationInstance]
        }
    }

    def edit = {
        def pollingConfigurationInstance = PollingConfiguration.get(params.id)
        if (!pollingConfigurationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [pollingConfigurationInstance: pollingConfigurationInstance]
        }
    }

    def update = {
        def pollingConfigurationInstance = PollingConfiguration.get(params.id)
        if (pollingConfigurationInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (pollingConfigurationInstance.version > version) {
                    
                    pollingConfigurationInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration')] as Object[], "Another user has updated this PollingConfiguration while you were editing")
                    render(view: "edit", model: [pollingConfigurationInstance: pollingConfigurationInstance])
                    return
                }
            }
            pollingConfigurationInstance.properties = params
            if (!pollingConfigurationInstance.hasErrors() && pollingConfigurationInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), pollingConfigurationInstance.id])}"
                redirect(action: "show", id: pollingConfigurationInstance.id)
            }
            else {
                render(view: "edit", model: [pollingConfigurationInstance: pollingConfigurationInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def pollingConfigurationInstance = PollingConfiguration.get(params.id)
        if (pollingConfigurationInstance) {
            try {
                pollingConfigurationInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), params.id])}"
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
		
			def pollList = PollingConfiguration.list(max:maxRows, offset:rowOffset)
				
			def jsoncells = pollList.collect{ PollingConfiguration pc ->
				[cell: [pc.id,pc.name,pc.commRoute.name,pc.repoRoute.name,pc.enabled,
					pc.period,pc.forceWriteDelta,pc.refreshTagsBeforePoll ]]
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
			flash.message = "${message(code: 'default.pollConfig.retrieve.error', args: [message(code: 'PollingConfiguration.label', default: 'PollingConfiguration'), params.id], default: 'Error Retrieving PollingConfiguration Properties')}"
			redirect(action: "show", id: params.id)
		}
	}
	
	def pollConfigEdit = {
		// msg format: [id:1, oper:add, pname:newname, pvalue:newvalue, action:commServerEditProps, controller:PollingConfiguration]
		def msg = "Could not edit polling configuration"
		def state = "FAIL"
		def id
		
			switch (params.oper){
				case 'add':
					
					def pollingConfigurationInstance = new PollingConfiguration()
					bindData(pollingConfigurationInstance,params,[exclude:["repoRoute","commRoute"]]);
					if (params.repoRoute){
						StoredRoutes rr = StoredRoutes.get(params.repoRoute)
						if (rr != pollingConfigurationInstance.repoRoute){
							pollingConfigurationInstance.repoRoute = rr
						}
					}
					if (params.commRoute){
						StoredRoutes cr = StoredRoutes.get(params.commRoute)
						if (cr != pollingConfigurationInstance.commRoute){
							pollingConfigurationInstance.commRoute = cr
						}
					}
					if (pollingConfigurationInstance.save(flush: true)) {
						flash.message = "${message(code: 'default.created.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), pollingConfigurationInstance.id])}"
						msg = "Polling Configuration id [ ${pollingConfigurationInstance.id} ] added"
						id = "${pollingConfigurationInstance.id}"
						state = "OK"
					}
					break;
				case 'edit':
					def pollingConfigurationInstance  = PollingConfiguration.get(params.id)
					if (pollingConfigurationInstance) {
						if (params.version) {
							def version = params.version.toLong()
							if (pollingConfigurationInstance.version > version) {
								
								pollingConfigurationInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration')] as Object[], "Another user has updated this PollingConfiguration while you were editing")
								render(view: "edit", model: [pollingConfigurationInstance: pollingConfigurationInstance])
								return
							}
						}
						
						//pollingConfigurationInstance.properties = params
						bindData(pollingConfigurationInstance,params,[exclude:["repoRoute","commRoute"]]);
						if (params.repoRoute){
							StoredRoutes rr = StoredRoutes.get(params.repoRoute)
							if (rr != pollingConfigurationInstance.repoRoute){
								pollingConfigurationInstance.repoRoute = rr
							}
						}
						if (params.commRoute){
							StoredRoutes cr = StoredRoutes.get(params.commRoute)
							if (cr != pollingConfigurationInstance.commRoute){
								pollingConfigurationInstance.commRoute = cr
							}
						}
						
						if (!pollingConfigurationInstance.hasErrors() && pollingConfigurationInstance.save(flush: true)) {
							def mc = message(code: "pollingConfiguration.label", default: 'PollingConfiguration')
							flash.message = "${message(code: 'default.updated.message', args: [mc, pollingConfigurationInstance.id])}"
							//message = "${message(code: 'default.updated.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), pollingConfigurationInstance.id])}"
							state = "OK"
							id = "${pollingConfigurationInstance.id}"
						}
					}
					break;
					
				case 'del':
					def pollingConfigurationInstance  = PollingConfiguration.get(params.id)
					if (pollingConfigurationInstance) {
						try {
							id = "${pollingConfigurationInstance?.id}"
							pollingConfigurationInstance.delete(flush: true)
							msg = "${message(code: 'default.deleted.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), params.id])}"
							state = "OK"
						}
						catch (org.springframework.dao.DataIntegrityViolationException e) {
							msg = "${message(code: 'default.not.deleted.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), params.id])}"
							
						}
					}
					else {
						msg = "${message(code: 'default.not.found.message', args: [message(code: 'pollingConfiguration.label', default: 'PollingConfiguration'), params.id])}"
					}
					break;
			}
		
		def response = [message:msg.toString(), state:state,id:id]
		render response as JSON
	}
	
	def pollConfigPropsList = {
		def pollConfigInstance = PollingConfiguration.get(params.id)
		if (pollConfigInstance){
			try {
				def sortIndex = params.sidx ?: 'name';
				def sortOrder = params.sord ?: 'asc'
				
				def maxRows = Integer.valueOf(params.rows);
				def currentPage = Integer.valueOf(params.page) ?: 1
				def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows
				
				def jsoncells = pollConfigInstance.configProperties.collect{ k,v ->
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
				jsoncells = minIdx <0 || maxIdx <0 ? [] : jsoncells[minIdx..maxIdx]
				
				def jsonData = [rows: jsoncells,
								page:currentPage,
								records:totalCount,
								total:numPages]
				render jsonData as JSON
			}catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.pollConfig.retrieve.error', args: [message(code: 'PollingConfiguration.label', default: 'PollingConfiguration'), params.id], default: 'Error Retrieving PollingConfiguration Properties')}"
				redirect(action: "show", id: params.id)
			}
		}
	}
	
	def pollConfigEditProps = {
		// msg format: [id:1, oper:add, pname:newname, pvalue:newvalue, action:commServerEditProps, controller:PollingConfiguration]
		def message = "Could not save property with name: ${params.name}, value: ${params.value}"
		def state = "FAIL"
		def id
		def pollConfigInstance = PollingConfiguration.get(params.id)
		if (pollConfigInstance){
			switch (params.oper){
				case 'add':
					pollConfigInstance.configProperties."${params.name}" = "${params.value}".toString()
					if (!pollConfigInstance.hasErrors() && pollConfigInstance.save(flush:true)){
						message = "Polling Configuration Property [ ${params.name} : ${params.value} ] added"
						id = "${params.name}"
						state = "OK"
					}
					break;
				case 'edit':
					def mp = pollConfigInstance.configProperties
					if (mp.containsKey("${params.name}".toString())){
						pollConfigInstance.configProperties."${params.name}" = "${params.value}".toString()
						message = "Polling Configuration Property ${params.name} value changed to ${params.value} "
						id = "${params.name}"
						state = "OK"
					}
					break;
				case 'del':
					def mp = pollConfigInstance.configProperties
					if (mp.containsKey("${params.delId}".toString())){
						pollConfigInstance.configProperties.remove("${params.delId}".toString())
						message = "Polling Configuration Property [ ${params.delId} : ${params.value} ] removed"
						id = "${params.delId}"
						state = "OK"
					}
					break;
			}
		}
		def response = [message:message.toString(), state:state,id:id]
		render response as JSON
	}
	
	def pollConfigTargetList = {
		def listErr = false
		try{
			def pollConfigInstance = PollingConfiguration.get(params.id)
			if (pollConfigInstance){
				def RemotePollTargetInstanceList = RemotePollTarget.findAllByPollConfiguration(pollConfigInstance)
				
				def sortIndex = params.sidx ?: 'targetId';
				def sortOrder = params.sord ?: 'asc'
				
				def maxRows = Integer.valueOf(params.rows);
				def currentPage = Integer.valueOf(params.page) ?: 1
				def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows
				
				
				def jsoncells = RemotePollTargetInstanceList.collect{ RemotePollTarget tgt ->
					[cell: [tgt.id,
							tgt?.dataTagForTarget?.name,
							tgt.remoteAddress,(tgt.operation == 'null' ? 'Any' : tgt.operation) ?: 'Any',
							tgt.enabled,
							tgt.tagId]]
				}
				
				def idx = 0;
				switch (sortIndex){
					case 'targetId' : idx = 0; break;
					case 'tag'		: idx = 1; break;
					case 'address'	: idx = 2; break;
					case 'operation': idx = 3; break;
					case 'enabled'	: idx = 4; break;
					case 'tagId'	: idx = 5; break;
				}
				
				jsoncells = jsoncells.sort{ x1,x2 ->
					def c1 = x1.cell[idx] ?: ''
					def c2 = x2.cell[idx] ?: ''
					
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
			} else {
				listErr = true;
			}
		} catch (Exception e){
			listErr = true
		}
		if (listErr)
			flash.message = "${message(code: 'default.pollingConfiguration.retrieve.error', args: [message(code: 'PollingConfiguration.label', default: 'Polling Configuration Target Tags Error'), params.id], default: 'Error Retrieving Polling Configuration Target Tags')}"
	}
	
	def pollConfigEditTargets = {
		// msg format: [id:1, oper:add, pname:newname, pvalue:newvalue, action:commServerEditProps, controller:PollingConfiguration]
		def message = "Could not save property with name: ${params.name}, value: ${params.value}"
		def state = "FAIL"
		def id
		def pollConfigInstance = PollingConfiguration.get(params.id)
		if (pollConfigInstance){
			switch (params.oper){
				case 'add':
					RemotePollTarget st = new RemotePollTarget();
					
					def tid = params.tagId ? UUID.fromString(params.tagId) :
								params.tag ? DataTag.findByName(params.tag).tagId : null;
					
					st.tagId = tid
					st.remoteAddress = params.address ?: 'HR1/MB_UINT'
					st.operation = params.operation ?: 'Any'
					st.enabled = params.enabled ?: false
					st.pollConfiguration = pollConfigInstance  // add operation must be for this serverSetup
					
					
					
					if (!st.hasErrors() && st.save()){
						message = "Polling Configuration target added"
						id = "${params.tag}"
						state = "OK"
					}
					
					pollConfigInstance.addTarget(st)
					
					if (!pollConfigInstance.hasErrors() && pollConfigInstance.save(flush:true)){
						message = "Polling Configuration target added"
						id = "${params.name}"
						state = "OK"
					}
					break;
				case 'edit':
					def targets = pollConfigInstance.targets
					def tid
					try {
						tid = params.tagId ? UUID.fromString(params.tagId) : null
					} catch (Exception e){}
					
					if (!tid){
						tid = params.tag ? DataTag.findByName(params.tag).tagId : null;
					}
						
					id = tid?.toString()
					
					def target = targets.find{
						it.tagId == tid
					}
					
					if (target){
						if (params.address)
							target.remoteAddress = params.address
						if (params.operation)
							target.operation = params.operation
						if (params.enabled)
							target.enabled = Boolean.valueOf(params.enabled)
						
						if (!target.hasErrors() && target.save(flush:true)){
							message = "Target ${target.tagId} Edited"
							state = "OK"
							break;
						}
					}
					message = "Target ${target.tagId} Edit failed"
					state = "FAIL"
					break;
				case 'del':
					def targetId = params.delId ? Long.valueOf(params.delId) : null;
					if (targetId){
						def targets = pollConfigInstance.targets
						def target = RemotePollTarget.get(targetId)
						pollConfigInstance.removeFromTargets(target);
						target.delete(flush:true)
						if (!pollConfigInstance.hasErrors() && pollConfigInstance.save()){
							message = "Target ${targetId} deleted"
							state = "OK"
						} else {
							message = "Target delete error ${targetId}"
						}
					}
					break;
			}
		}
		def response = [message:message.toString(), state:state,id:id]
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
