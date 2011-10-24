package org.scadajack.grails.ui

import grails.converters.JSON
import static org.codehaus.groovy.grails.commons.ConfigurationHolder.config as Config
import org.scadajack.grails.util.DomainHelperService
import org.springframework.http.HttpStatus
import uk.co.desirableobjects.ajaxuploader.AjaxUploaderService
import uk.co.desirableobjects.ajaxuploader.exception.FileUploadException
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

class FileUploadController {

    AjaxUploaderService ajaxUploaderService
	DomainHelperService domainHelperService

    def upload = {
		def result = true
        try {
			def uploadFileName = params.qqfile
            File uploadedFile = createTemporaryFile(uploadFileName)
            InputStream inputStream = selectInputStream(request)
			
            ajaxUploaderService.upload(inputStream, uploadedFile)
			
				// Use original filename to look for domain class
			def domain = domainHelperService.domainFromFile(uploadFileName)
				// Use created file name for import
			result = domainHelperService.importFileIntoDomain(uploadedFile, domain)
			
			if (result)
            	return render(text: [success:true] as JSON, contentType:'text/json')
			else
				log.error("Failed to import file.")

        } catch (FileUploadException e) {
            log.error("Failed to upload file.", e)
        }
		return render(text: [success:false] as JSON, contentType:'text/json')
    }

    private InputStream selectInputStream(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('qqfile')
            return uploadedFile.inputStream
        }
        return request.inputStream
    }

    private File createTemporaryFile(def uploadFileName) {
        File uploaded
		
        if (Config.ajaxFileUpload.containsKey('temporaryFilePrefix') && uploadFileName) {
			def dotPosition = uploadFileName.lastIndexOf('.')
			def uploadFilePrefix = dotPosition > 0 ? uploadFileName.substring(0,dotPosition) : uploadFileName
			def uploadFileSuffix = dotPosition > 0 ? uploadFileName.substring(dotPosition) : null
            uploaded = File.createTempFile("${Config.ajaxFileUpload.temporaryFilePrefix + uploadFilePrefix}",uploadFileSuffix)
        } else {
            uploaded = File.createTempFile('grails', 'ajaxupload')
        }
        return uploaded
    }

}
