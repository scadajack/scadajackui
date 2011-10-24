package org.blubbo.grails.data

class LongDataHolderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [longDataHolderInstanceList: LongDataHolder.list(params), longDataHolderInstanceTotal: LongDataHolder.count()]
    }

    def create = {
        def longDataHolderInstance = new LongDataHolder()
        longDataHolderInstance.properties = params
        return [longDataHolderInstance: longDataHolderInstance]
    }

    def save = {
        def longDataHolderInstance = new LongDataHolder(params)
        if (longDataHolderInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'longDataHolder.label', default: 'LongDataHolder'), longDataHolderInstance.id])}"
            redirect(action: "show", id: longDataHolderInstance.id)
        }
        else {
            render(view: "create", model: [longDataHolderInstance: longDataHolderInstance])
        }
    }

    def show = {
        def longDataHolderInstance = LongDataHolder.get(params.id)
        if (!longDataHolderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'longDataHolder.label', default: 'LongDataHolder'), params.id])}"
            redirect(action: "list")
        }
        else {
            [longDataHolderInstance: longDataHolderInstance]
        }
    }

    def edit = {
        def longDataHolderInstance = LongDataHolder.get(params.id)
        if (!longDataHolderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'longDataHolder.label', default: 'LongDataHolder'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [longDataHolderInstance: longDataHolderInstance]
        }
    }

    def update = {
        def longDataHolderInstance = LongDataHolder.get(params.id)
        if (longDataHolderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (longDataHolderInstance.version > version) {
                    
                    longDataHolderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'longDataHolder.label', default: 'LongDataHolder')] as Object[], "Another user has updated this LongDataHolder while you were editing")
                    render(view: "edit", model: [longDataHolderInstance: longDataHolderInstance])
                    return
                }
            }
            longDataHolderInstance.properties = params
            if (!longDataHolderInstance.hasErrors() && longDataHolderInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'longDataHolder.label', default: 'LongDataHolder'), longDataHolderInstance.id])}"
                redirect(action: "show", id: longDataHolderInstance.id)
            }
            else {
                render(view: "edit", model: [longDataHolderInstance: longDataHolderInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'longDataHolder.label', default: 'LongDataHolder'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def longDataHolderInstance = LongDataHolder.get(params.id)
        if (longDataHolderInstance) {
            try {
                longDataHolderInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'longDataHolder.label', default: 'LongDataHolder'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'longDataHolder.label', default: 'LongDataHolder'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'longDataHolder.label', default: 'LongDataHolder'), params.id])}"
            redirect(action: "list")
        }
    }
}
