package org.scadajack.grails.util

import grails.converters.JSON

class ApplicationPropsController {

	ApplicationPropertiesService applicationPropertiesService
	
    def index = {redirect(action: "list", params: params) }
	
	def showProps = {}
	
	def list = {}
	
	def listSysProps = {
		Map props = applicationPropertiesService.sysProps()
		try {
			def sortIndex = params.sidx ?: 'name';
			def sortOrder = params.sord ?: 'asc'
			
			def maxRows = Integer.valueOf(params.rows);
			def currentPage = Integer.valueOf(params.page) ?: 1
			def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows
			
			def jsoncells = props.collect{ k,v ->
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
			flash.message = "${message(code: 'default.applicationProperties.retrieve.error', args: [message(code: 'ApplicationProperties.label', default: 'Application Properties'), params.id], default: 'Error Retrieving Application Properties')}"
			redirect(action: "show", id: params.id)
		}
		
	}
	
	def listAppUIProps = {
		Map props = applicationPropertiesService.grailsConfigProps()
		try {
			def sortIndex = params.sidx ?: 'name';
			def sortOrder = params.sord ?: 'asc'
			
			def maxRows = Integer.valueOf(params.rows);
			def currentPage = Integer.valueOf(params.page) ?: 1
			def rowOffset = currentPage <= 1 ? 0 : (currentPage - 1) * maxRows
			
			def jsoncells = props.collect{ k,v ->
				[cell: [k,v.toString()]]
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
			flash.message = "${message(code: 'default.applicationProperties.retrieve.error', args: [message(code: 'ApplicationProperties.label', default: 'Application Properties'), params.id], default: 'Error Retrieving Application Properties')}"
			redirect(action: "show", id: params.id)
		}
		
	}
	
}
