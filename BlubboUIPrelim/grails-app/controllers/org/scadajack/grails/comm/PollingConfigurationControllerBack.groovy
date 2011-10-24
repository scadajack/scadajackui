package org.scadajack.grails.comm

class PollingConfigurationControllerBack {

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
}
