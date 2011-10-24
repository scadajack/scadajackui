package org.blubbo.grails.data

class AbstractDataHolderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [abstractDataHolderInstanceList: AbstractDataHolder.list(params), abstractDataHolderInstanceTotal: AbstractDataHolder.count()]
    }

    def create = {
        def abstractDataHolderInstance = new AbstractDataHolder()
        abstractDataHolderInstance.properties = params
        return [abstractDataHolderInstance: abstractDataHolderInstance]
    }

    def save = {
        def abstractDataHolderInstance = new AbstractDataHolder(params)
        if (abstractDataHolderInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'abstractDataHolder.label', default: 'AbstractDataHolder'), abstractDataHolderInstance.id])}"
            redirect(action: "show", id: abstractDataHolderInstance.id)
        }
        else {
            render(view: "create", model: [abstractDataHolderInstance: abstractDataHolderInstance])
        }
    }

    def show = {
        def abstractDataHolderInstance = AbstractDataHolder.get(params.id)
        if (!abstractDataHolderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'abstractDataHolder.label', default: 'AbstractDataHolder'), params.id])}"
            redirect(action: "list")
        }
        else {
            [abstractDataHolderInstance: abstractDataHolderInstance]
        }
    }

    def edit = {
        def abstractDataHolderInstance = AbstractDataHolder.get(params.id)
        if (!abstractDataHolderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'abstractDataHolder.label', default: 'AbstractDataHolder'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [abstractDataHolderInstance: abstractDataHolderInstance]
        }
    }

    def update = {
        def abstractDataHolderInstance = AbstractDataHolder.get(params.id)
        if (abstractDataHolderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (abstractDataHolderInstance.version > version) {
                    
                    abstractDataHolderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'abstractDataHolder.label', default: 'AbstractDataHolder')] as Object[], "Another user has updated this AbstractDataHolder while you were editing")
                    render(view: "edit", model: [abstractDataHolderInstance: abstractDataHolderInstance])
                    return
                }
            }
            abstractDataHolderInstance.properties = params
            if (!abstractDataHolderInstance.hasErrors() && abstractDataHolderInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'abstractDataHolder.label', default: 'AbstractDataHolder'), abstractDataHolderInstance.id])}"
                redirect(action: "show", id: abstractDataHolderInstance.id)
            }
            else {
                render(view: "edit", model: [abstractDataHolderInstance: abstractDataHolderInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'abstractDataHolder.label', default: 'AbstractDataHolder'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def abstractDataHolderInstance = AbstractDataHolder.get(params.id)
        if (abstractDataHolderInstance) {
            try {
                abstractDataHolderInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'abstractDataHolder.label', default: 'AbstractDataHolder'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'abstractDataHolder.label', default: 'AbstractDataHolder'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'abstractDataHolder.label', default: 'AbstractDataHolder'), params.id])}"
            redirect(action: "list")
        }
    }
}
