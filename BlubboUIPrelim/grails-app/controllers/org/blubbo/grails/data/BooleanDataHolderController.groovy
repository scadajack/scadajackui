package org.blubbo.grails.data

class BooleanDataHolderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [booleanDataHolderInstanceList: BooleanDataHolder.list(params), booleanDataHolderInstanceTotal: BooleanDataHolder.count()]
    }

    def create = {
        def booleanDataHolderInstance = new BooleanDataHolder()
        booleanDataHolderInstance.properties = params
        return [booleanDataHolderInstance: booleanDataHolderInstance]
    }

    def save = {
        def booleanDataHolderInstance = new BooleanDataHolder(params)
        if (booleanDataHolderInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'booleanDataHolder.label', default: 'BooleanDataHolder'), booleanDataHolderInstance.id])}"
            redirect(action: "show", id: booleanDataHolderInstance.id)
        }
        else {
            render(view: "create", model: [booleanDataHolderInstance: booleanDataHolderInstance])
        }
    }

    def show = {
        def booleanDataHolderInstance = BooleanDataHolder.get(params.id)
        if (!booleanDataHolderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'booleanDataHolder.label', default: 'BooleanDataHolder'), params.id])}"
            redirect(action: "list")
        }
        else {
            [booleanDataHolderInstance: booleanDataHolderInstance]
        }
    }

    def edit = {
        def booleanDataHolderInstance = BooleanDataHolder.get(params.id)
        if (!booleanDataHolderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'booleanDataHolder.label', default: 'BooleanDataHolder'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [booleanDataHolderInstance: booleanDataHolderInstance]
        }
    }

    def update = {
        def booleanDataHolderInstance = BooleanDataHolder.get(params.id)
        if (booleanDataHolderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (booleanDataHolderInstance.version > version) {
                    
                    booleanDataHolderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'booleanDataHolder.label', default: 'BooleanDataHolder')] as Object[], "Another user has updated this BooleanDataHolder while you were editing")
                    render(view: "edit", model: [booleanDataHolderInstance: booleanDataHolderInstance])
                    return
                }
            }
            booleanDataHolderInstance.properties = params
            if (!booleanDataHolderInstance.hasErrors() && booleanDataHolderInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'booleanDataHolder.label', default: 'BooleanDataHolder'), booleanDataHolderInstance.id])}"
                redirect(action: "show", id: booleanDataHolderInstance.id)
            }
            else {
                render(view: "edit", model: [booleanDataHolderInstance: booleanDataHolderInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'booleanDataHolder.label', default: 'BooleanDataHolder'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def booleanDataHolderInstance = BooleanDataHolder.get(params.id)
        if (booleanDataHolderInstance) {
            try {
                booleanDataHolderInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'booleanDataHolder.label', default: 'BooleanDataHolder'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'booleanDataHolder.label', default: 'BooleanDataHolder'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'booleanDataHolder.label', default: 'BooleanDataHolder'), params.id])}"
            redirect(action: "list")
        }
    }
}
