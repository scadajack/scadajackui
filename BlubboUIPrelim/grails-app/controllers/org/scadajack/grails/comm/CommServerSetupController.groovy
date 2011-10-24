package org.scadajack.grails.comm

import org.scadajack.camel.comm.IRemoteTarget;

import com.blubbo.grails.routing.StoredRoutes
import grails.converters.JSON
import org.blubbo.grails.data.DataTag



class CommServerSetupController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [commServerSetupInstanceList: CommServerSetup.list(params), commServerSetupInstanceTotal: CommServerSetup.count()]
    }

    def create = {
        def commServerSetupInstance = new CommServerSetup()
        commServerSetupInstance.properties = params
        return [commServerSetupInstance: commServerSetupInstance]
    }

    def save = {
        def commServerSetupInstance = new CommServerSetup(params)
        if (commServerSetupInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), commServerSetupInstance.id])}"
            redirect(action: "show", id: commServerSetupInstance.id)
        }
        else {
            render(view: "create", model: [commServerSetupInstance: commServerSetupInstance])
        }
    }

    def show = {
        def commServerSetupInstance = CommServerSetup.get(params.id)
        if (!commServerSetupInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), params.id])}"
            redirect(action: "list")
        }
        else {
			def serverTargetInstanceList = ServerTarget.findAllByServerSetup(commServerSetupInstance)
            [commServerSetupInstance: commServerSetupInstance, 
				serverTargetInstanceList: serverTargetInstanceList, serverTargetInstanceTotal: serverTargetInstanceList.size()]
        }
    }

    def edit = {
        def commServerSetupInstance = CommServerSetup.get(params.id)
        if (!commServerSetupInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [commServerSetupInstance: commServerSetupInstance]
        }
    }

    def update = {
        def commServerSetupInstance = CommServerSetup.get(params.id)
        if (commServerSetupInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (commServerSetupInstance.version > version) {
                    
                    commServerSetupInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'commServerSetup.label', default: 'CommServerSetup')] as Object[], "Another user has updated this CommServerSetup while you were editing")
                    render(view: "edit", model: [commServerSetupInstance: commServerSetupInstance])
                    return
                }
            }
            commServerSetupInstance.properties = params
            if (!commServerSetupInstance.hasErrors() && commServerSetupInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), commServerSetupInstance.id])}"
                redirect(action: "show", id: commServerSetupInstance.id)
            }
            else {
                render(view: "edit", model: [commServerSetupInstance: commServerSetupInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def commServerSetupInstance = CommServerSetup.get(params.id)
        if (commServerSetupInstance) {
            try {
                commServerSetupInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), params.id])}"
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
		
			def serverList = CommServerSetup.list(max:maxRows, offset:rowOffset)
				
			def jsoncells = serverList.collect{ CommServerSetup pc ->
				[cell: [pc.id,pc.name,pc.commRoute.name,pc.repoRoute.name]]
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
			flash.message = "${message(code: 'default.commServerConfig.retrieve.error', args: [message(code: 'ServerConfiguration.label', default: 'Server Configuration'), params.id], default: 'Error Retrieving Server Configuration Properties')}"
			redirect(action: "show", id: params.id)
		}
		
	}
	
	def listEdit = {
		// msg format: [id:1, oper:add, pname:newname, pvalue:newvalue, action:commServerEditProps, controller:PollingConfiguration]
		def msg = "Could not edit Comm Server Setup"
		def state = "FAIL"
		def id
		
			switch (params.oper){
				case 'add':
					
					def commServerSetupInstance = new CommServerSetup()
					bindData(commServerSetupInstance,params,[exclude:["repoRoute","commRoute"]]);
					if (params.repoRoute){
						StoredRoutes rr = StoredRoutes.get(params.repoRoute)
						if (rr != commServerSetupInstance.repoRoute){
							commServerSetupInstance.repoRoute = rr
						}
					}
					if (params.commRoute){
						StoredRoutes cr = StoredRoutes.get(params.commRoute)
						if (cr != commServerSetupInstance.commRoute){
							commServerSetupInstance.commRoute = cr
						}
					}
					if (commServerSetupInstance.save(flush: true)) {
						flash.message = "${message(code: 'default.created.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), commServerSetupInstance.id])}"
						msg = "CommServerSetup id [ ${commServerSetupInstance.id} ] added"
						id = "${commServerSetupInstance.id}"
						state = "OK"
					}
					break;
				case 'edit':
					def commServerSetupInstance  = CommServerSetup.get(params.id)
					if (commServerSetupInstance) {
						if (params.version) {
							def version = params.version.toLong()
							if (commServerSetupInstance.version > version) {
								
								commServerSetupInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'commServerSetup.label', default: 'CommServerSetup')] as Object[], "Another user has updated this CommServerSetup while you were editing")
								render(view: "edit", model: [commServerSetupInstance: commServerSetupInstance])
								return
							}
						}
						
						bindData(commServerSetupInstance,params,[exclude:["repoRoute","commRoute"]]);
						if (params.repoRoute){
							StoredRoutes rr = StoredRoutes.get(params.repoRoute)
							if (rr != commServerSetupInstance.repoRoute){
								commServerSetupInstance.repoRoute = rr
							}
						}
						if (params.commRoute){
							StoredRoutes cr = StoredRoutes.get(params.commRoute)
							if (cr != commServerSetupInstance.commRoute){
								commServerSetupInstance.commRoute = cr
							}
						}
						
						if (!commServerSetupInstance.hasErrors() && commServerSetupInstance.save(flush: true)) {
							def mc = message(code: "commServerSetup.label", default: 'CommServerSetup')
							flash.message = "${message(code: 'default.updated.message', args: [mc, commServerSetupInstance.id])}"
							//message = "${message(code: 'default.updated.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), commServerSetupInstance.id])}"
							state = "OK"
							id = "${commServerSetupInstance.id}"
						}
					}
					break;
					
				case 'del':
					def commServerSetupInstance  = CommServerSetup.get(params.id)
					if (commServerSetupInstance) {
						try {
							id = "${commServerSetupInstance?.id}"
							commServerSetupInstance.delete(flush: true)
							msg = "${message(code: 'default.deleted.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), params.id])}"
							state = "OK"
						}
						catch (org.springframework.dao.DataIntegrityViolationException e) {
							msg = "${message(code: 'default.not.deleted.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), params.id])}"
							
						}
					}
					else {
						msg = "${message(code: 'default.not.found.message', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), params.id])}"
					}
					break;
			}
		
		def response = [message:msg.toString(), state:state,id:id]
		render response as JSON
	}
	
	
	def commServerPropsList = {
		def commServerSetupInstance = CommServerSetup.get(params.id)
		if (commServerSetupInstance){
			try {
				def sortIndex = params.sidx ?: 'name';
				def sortOrder = params.sord ?: 'asc'
				
				def maxRows = Integer.valueOf(params.rows);
				def currentPage = Integer.valueOf(params.page) ?: 1
				def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows
				
				def jsoncells = commServerSetupInstance.configProperties.collect{ k,v ->
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
                flash.message = "${message(code: 'default.commServer.retrieve.error', args: [message(code: 'commServerSetup.label', default: 'CommServerSetup'), params.id], default: 'Error Retrieving CommServerProperties')}"
                redirect(action: "show", id: params.id)
            }
		}	
	}
	
	def commServerEditProps = {
		// msg format: [id:1, oper:add, pname:newname, pvalue:newvalue, action:commServerEditProps, controller:commServerSetup]
		def message = "Could not save property with name: ${params.name}, value: ${params.value}"
		def state = "FAIL"
		def id
		def commServerSetupInstance = CommServerSetup.get(params.id)
		if (commServerSetupInstance){
			switch (params.oper){
				case 'add':
					commServerSetupInstance.configProperties."${params.name}" = "${params.value}".toString()
					if (!commServerSetupInstance.hasErrors() && commServerSetupInstance.save(flush:true)){
						message = "Comm Server Property [ ${params.name} : ${params.value} ] added"
						id = "${params.name}"
						state = "OK"
					}
					break;
				case 'edit':
					def mp = commServerSetupInstance.configProperties
					if (mp.containsKey("${params.name}".toString())){
						commServerSetupInstance.configProperties."${params.name}" = "${params.value}".toString()
						message = "Comm Server Property ${params.name} value changed to ${params.value} "
						id = "${params.name}"
						state = "OK"
					}
					break;
				case 'del':
					def mp = commServerSetupInstance.configProperties
					if (mp.containsKey("${params.delId}".toString())){
						commServerSetupInstance.configProperties.remove("${params.delId}".toString())
						message = "Comm Server Property [ ${params.delId} : ${params.value} ] removed"
						id = "${params.delId}"
						state = "OK"
					}
					break;
			}
		}
		def response = [message:message.toString(), state:state,id:id]
		render response as JSON
	}
	
	def commServerTargetList = {
		def listErr = false
		try{
			def commServerSetupInstance = CommServerSetup.get(params.id)
			if (commServerSetupInstance){
				def serverTargetInstanceList = ServerTarget.findAllByServerSetup(commServerSetupInstance)
				
				def sortIndex = params.sidx ?: 'targetId';
				def sortOrder = params.sord ?: 'asc'
				
				def maxRows = Integer.valueOf(params.rows);
				def currentPage = Integer.valueOf(params.page) ?: 1
				def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows
				
				
				def jsoncells = serverTargetInstanceList.collect{ ServerTarget tgt ->
					[cell: [tgt.id,
							tgt?.dataTagForTarget?.name,
							tgt.address,(tgt.operation == 'null' ? 'Any' : tgt.operation) ?: 'Any',
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
			flash.message = "${message(code: 'default.commServer.retrieve.error', args: [message(code: 'commServerSetup.label', default: 'Server Target Tags Error'), params.id], default: 'Error Retrieving Server Target Tags')}"                
	}
	
	def commServerEditTargets = {
		// msg format: [id:1, oper:add, pname:newname, pvalue:newvalue, action:commServerEditProps, controller:commServerSetup]
		def message = "Could not save property with name: ${params.name}, value: ${params.value}"
		def state = "FAIL"
		def id
		def commServerSetupInstance = CommServerSetup.get(params.id)
		if (commServerSetupInstance){
			switch (params.oper){
				case 'add':
					ServerTarget st = new ServerTarget();
					
					def tid = params.tagId ? UUID.fromString(params.tagId) : 
								params.tag ? DataTag.findByName(params.tag).tagId : null;
					
					st.tagId = tid
					st.address = params.address ?: 'HR1/MB_UINT'
					st.operation = params.operation ?: 'Any'
					st.enabled = params.enabled ?: false
					st.serverSetup = commServerSetupInstance  // add operation must be for this serverSetup
					
					
					
					if (!st.hasErrors() && st.save()){
						message = "Comm Server target added"
						id = "${params.tag}"
						state = "OK"
					}
					
					commServerSetupInstance.addTarget(st)
					
					if (!commServerSetupInstance.hasErrors() && commServerSetupInstance.save(flush:true)){
						message = "Comm Server target added"
						id = "${params.name}"
						state = "OK"
					}
					break;
				case 'edit':
					def targets = commServerSetupInstance.targets
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
							target.address = params.address
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
					break;
			}
		}
		def response = [message:message.toString(), state:state,id:id]
		render response as JSON
	}
}
