package org.blubbo.grails.data

class DoubleDataHolderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [doubleDataHolderInstanceList: DoubleDataHolder.list(params), doubleDataHolderInstanceTotal: DoubleDataHolder.count()]
    }

    def create = {
        def doubleDataHolderInstance = new DoubleDataHolder()
        doubleDataHolderInstance.properties = params
        return [doubleDataHolderInstance: doubleDataHolderInstance]
    }

    def save = {
        def doubleDataHolderInstance = new DoubleDataHolder(params)
        if (doubleDataHolderInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder'), doubleDataHolderInstance.id])}"
            redirect(action: "show", id: doubleDataHolderInstance.id)
        }
        else {
            render(view: "create", model: [doubleDataHolderInstance: doubleDataHolderInstance])
        }
    }

    def show = {
        def doubleDataHolderInstance = DoubleDataHolder.get(params.id)
        if (!doubleDataHolderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder'), params.id])}"
            redirect(action: "list")
        }
        else {
            [doubleDataHolderInstance: doubleDataHolderInstance]
        }
    }

    def edit = {
        def doubleDataHolderInstance = DoubleDataHolder.get(params.id)
        if (!doubleDataHolderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [doubleDataHolderInstance: doubleDataHolderInstance]
        }
    }

    def update = {
        def doubleDataHolderInstance = DoubleDataHolder.get(params.id)
        if (doubleDataHolderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (doubleDataHolderInstance.version > version) {
                    
                    doubleDataHolderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder')] as Object[], "Another user has updated this DoubleDataHolder while you were editing")
                    render(view: "edit", model: [doubleDataHolderInstance: doubleDataHolderInstance])
                    return
                }
            }
            doubleDataHolderInstance.properties = params
            if (!doubleDataHolderInstance.hasErrors() && doubleDataHolderInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder'), doubleDataHolderInstance.id])}"
                redirect(action: "show", id: doubleDataHolderInstance.id)
            }
            else {
                render(view: "edit", model: [doubleDataHolderInstance: doubleDataHolderInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def doubleDataHolderInstance = DoubleDataHolder.get(params.id)
        if (doubleDataHolderInstance) {
            try {
                doubleDataHolderInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'doubleDataHolder.label', default: 'DoubleDataHolder'), params.id])}"
            redirect(action: "list")
        }
    }
}
