package org.scadajack.grails.comm

class ServerTargetController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [serverTargetInstanceList: ServerTarget.list(params), serverTargetInstanceTotal: ServerTarget.count()]
    }

    def create = {
        def serverTargetInstance = new ServerTarget()
        serverTargetInstance.properties = params
        return [serverTargetInstance: serverTargetInstance]
    }

    def save = {
        def serverTargetInstance = new ServerTarget(params)
        if (serverTargetInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'serverTarget.label', default: 'ServerTarget'), serverTargetInstance.id])}"
            redirect(action: "show", id: serverTargetInstance.id)
        }
        else {
            render(view: "create", model: [serverTargetInstance: serverTargetInstance])
        }
    }

    def show = {
        def serverTargetInstance = ServerTarget.get(params.id)
        if (!serverTargetInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'serverTarget.label', default: 'ServerTarget'), params.id])}"
            redirect(action: "list")
        }
        else {
            [serverTargetInstance: serverTargetInstance]
        }
    }

    def edit = {
        def serverTargetInstance = ServerTarget.get(params.id)
        if (!serverTargetInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'serverTarget.label', default: 'ServerTarget'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [serverTargetInstance: serverTargetInstance]
        }
    }

    def update = {
        def serverTargetInstance = ServerTarget.get(params.id)
        if (serverTargetInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (serverTargetInstance.version > version) {
                    
                    serverTargetInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'serverTarget.label', default: 'ServerTarget')] as Object[], "Another user has updated this ServerTarget while you were editing")
                    render(view: "edit", model: [serverTargetInstance: serverTargetInstance])
                    return
                }
            }
            serverTargetInstance.properties = params
            if (!serverTargetInstance.hasErrors() && serverTargetInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'serverTarget.label', default: 'ServerTarget'), serverTargetInstance.id])}"
                redirect(action: "show", id: serverTargetInstance.id)
            }
            else {
                render(view: "edit", model: [serverTargetInstance: serverTargetInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'serverTarget.label', default: 'ServerTarget'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def serverTargetInstance = ServerTarget.get(params.id)
        if (serverTargetInstance) {
            try {
                serverTargetInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'serverTarget.label', default: 'ServerTarget'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'serverTarget.label', default: 'ServerTarget'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'serverTarget.label', default: 'ServerTarget'), params.id])}"
            redirect(action: "list")
        }
    }
}
