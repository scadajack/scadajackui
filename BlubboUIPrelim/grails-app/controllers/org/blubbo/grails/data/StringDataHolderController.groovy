package org.blubbo.grails.data

class StringDataHolderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [stringDataHolderInstanceList: StringDataHolder.list(params), stringDataHolderInstanceTotal: StringDataHolder.count()]
    }

    def create = {
        def stringDataHolderInstance = new StringDataHolder()
        stringDataHolderInstance.properties = params
        return [stringDataHolderInstance: stringDataHolderInstance]
    }

    def save = {
        def stringDataHolderInstance = new StringDataHolder(params)
        if (stringDataHolderInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'stringDataHolder.label', default: 'StringDataHolder'), stringDataHolderInstance.id])}"
            redirect(action: "show", id: stringDataHolderInstance.id)
        }
        else {
            render(view: "create", model: [stringDataHolderInstance: stringDataHolderInstance])
        }
    }

    def show = {
        def stringDataHolderInstance = StringDataHolder.get(params.id)
        if (!stringDataHolderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'stringDataHolder.label', default: 'StringDataHolder'), params.id])}"
            redirect(action: "list")
        }
        else {
            [stringDataHolderInstance: stringDataHolderInstance]
        }
    }

    def edit = {
        def stringDataHolderInstance = StringDataHolder.get(params.id)
        if (!stringDataHolderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'stringDataHolder.label', default: 'StringDataHolder'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [stringDataHolderInstance: stringDataHolderInstance]
        }
    }

    def update = {
        def stringDataHolderInstance = StringDataHolder.get(params.id)
        if (stringDataHolderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (stringDataHolderInstance.version > version) {
                    
                    stringDataHolderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'stringDataHolder.label', default: 'StringDataHolder')] as Object[], "Another user has updated this StringDataHolder while you were editing")
                    render(view: "edit", model: [stringDataHolderInstance: stringDataHolderInstance])
                    return
                }
            }
            stringDataHolderInstance.properties = params
            if (!stringDataHolderInstance.hasErrors() && stringDataHolderInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'stringDataHolder.label', default: 'StringDataHolder'), stringDataHolderInstance.id])}"
                redirect(action: "show", id: stringDataHolderInstance.id)
            }
            else {
                render(view: "edit", model: [stringDataHolderInstance: stringDataHolderInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'stringDataHolder.label', default: 'StringDataHolder'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def stringDataHolderInstance = StringDataHolder.get(params.id)
        if (stringDataHolderInstance) {
            try {
                stringDataHolderInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'stringDataHolder.label', default: 'StringDataHolder'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'stringDataHolder.label', default: 'StringDataHolder'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'stringDataHolder.label', default: 'StringDataHolder'), params.id])}"
            redirect(action: "list")
        }
    }
}
