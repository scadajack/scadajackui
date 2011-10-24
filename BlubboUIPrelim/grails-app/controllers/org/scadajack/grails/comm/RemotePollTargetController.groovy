package org.scadajack.grails.comm

class RemotePollTargetController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [remotePollTargetInstanceList: RemotePollTarget.list(params), remotePollTargetInstanceTotal: RemotePollTarget.count()]
    }

    def create = {
        def remotePollTargetInstance = new RemotePollTarget()
        remotePollTargetInstance.properties = params
        return [remotePollTargetInstance: remotePollTargetInstance]
    }

    def save = {
        def remotePollTargetInstance = new RemotePollTarget(params)
        if (remotePollTargetInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'remotePollTarget.label', default: 'RemotePollTarget'), remotePollTargetInstance.id])}"
            redirect(action: "show", id: remotePollTargetInstance.id)
        }
        else {
            render(view: "create", model: [remotePollTargetInstance: remotePollTargetInstance])
        }
    }

    def show = {
        def remotePollTargetInstance = RemotePollTarget.get(params.id)
        if (!remotePollTargetInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'remotePollTarget.label', default: 'RemotePollTarget'), params.id])}"
            redirect(action: "list")
        }
        else {
            [remotePollTargetInstance: remotePollTargetInstance]
        }
    }

    def edit = {
        def remotePollTargetInstance = RemotePollTarget.get(params.id)
        if (!remotePollTargetInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'remotePollTarget.label', default: 'RemotePollTarget'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [remotePollTargetInstance: remotePollTargetInstance]
        }
    }

    def update = {
        def remotePollTargetInstance = RemotePollTarget.get(params.id)
        if (remotePollTargetInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (remotePollTargetInstance.version > version) {
                    
                    remotePollTargetInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'remotePollTarget.label', default: 'RemotePollTarget')] as Object[], "Another user has updated this RemotePollTarget while you were editing")
                    render(view: "edit", model: [remotePollTargetInstance: remotePollTargetInstance])
                    return
                }
            }
            remotePollTargetInstance.properties = params
            if (!remotePollTargetInstance.hasErrors() && remotePollTargetInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'remotePollTarget.label', default: 'RemotePollTarget'), remotePollTargetInstance.id])}"
                redirect(action: "show", id: remotePollTargetInstance.id)
            }
            else {
                render(view: "edit", model: [remotePollTargetInstance: remotePollTargetInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'remotePollTarget.label', default: 'RemotePollTarget'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def remotePollTargetInstance = RemotePollTarget.get(params.id)
        if (remotePollTargetInstance) {
            try {
                remotePollTargetInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'remotePollTarget.label', default: 'RemotePollTarget'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'remotePollTarget.label', default: 'RemotePollTarget'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'remotePollTarget.label', default: 'RemotePollTarget'), params.id])}"
            redirect(action: "list")
        }
    }
}
