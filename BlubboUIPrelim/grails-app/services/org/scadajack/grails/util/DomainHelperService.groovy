package org.scadajack.grails.util

import java.util.Map

import org.blubbo.grails.data.DataTag
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.scadajack.grails.comm.CommServerSetup
import org.scadajack.grails.comm.PollingConfiguration
import org.scadajack.grails.comm.RemotePollTarget
import org.scadajack.grails.comm.ServerTarget
import org.scadajack.grails.repository.DataRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.blubbo.grails.routing.StoredRoutes
import com.google.common.base.CharMatcher
import com.google.common.base.Splitter

class DomainHelperService {
	static Logger log = LoggerFactory.getLogger(DomainHelperService.class)
    static transactional = true

	GrailsApplication grailsApplication
	
    def serviceMethod() {

    }
	
	/**
	 * Utility method to find route references in the domains below and 
	 * remove them from the domain instances. Used to allow routes to be 
	 * removed without having to manually go to each domain that uses them.
	 * Eventually will likely want to remove the RI constraints for these 
	 * items so this method won't be necessary.
	 * @param route
	 * @return
	 */
	def removeRouteReferences(StoredRoutes route){
		// do it brute force for now.
		CommServerSetup.findAllWhere(commRoute:route).each { 
			it.commRoute = null;	
		}
		
		CommServerSetup.findAllWhere(repoRoute:route).each {
			it.repoRoute = null;
		}
		
		PollingConfiguration.findAllWhere(targetRoute:route).each{
			it.targetRoute = null;
		}
		
		PollingConfiguration.findAllWhere(repoRoute:route).each{
			it.repoRoute = null;
		}
		
		DataRepository.findAllWhere(repoRoute:route).each{
			it.repoRoute = null;
		}
	}
	
	static Map qualifiedClassNameMap = ['Boolean':'java.lang.Boolean',
										'Byte':'java.lang.Byte', 
										'Short':'java.lang.Short',
										'Integer':'java.lang.Integer',
										'Long':'java.lang.Long', 
										'Float':'java.lang.Float', 
										'Double':'java.lang.Double',
										'String':'java.lang.String'];
	
	static Map sNameMap
	static String simpleClassName = { String qName ->
		if (sNameMap == null){
			sNameMap = [:]
			qualifiedClassNameMap.each{ k,v ->
				sNameMap[v]=k
			}
		}
		sNameMap[qName]
	}
	
	CharMatcher charMatcher = CharMatcher.anyOf('[]:,')
	Splitter mapSplitter = Splitter.on(charMatcher).trimResults()
	/**
	 * Provides a means to create a string from a map that can be then reimported to create a map.
	 * When a string is created from a map by java, it does not quote the values for string keys and values. 
	 * This then causes a problem for Groovy to reconvert back to a map because it interprets the unquoted values 
	 * to be labels instead of strings. So we must force the values to be quoted when converting. Also 
	 * have some trouble with field properties that end in 'properties'. Groovy or Grails interprets those 
	 * as not strictly maps, but Properties and uses a different 
	 * @param map
	 * @return
	 */
	static String importableStringFromMap(Map map){
		if (map.isEmpty()){
			return '[:]' // if grails thinks map is a properties object it returns [] instead of [:] for empty map
		}
		String mapString = map.toString()
			// Force values to be quoted.
		def quoted = mapString.replaceAll(/\w+/){ "'${it.trim()}'" }
			// Have to replace = in some cases as groovy uses = instead of : and curly brackets instead
			// of straight when it thinks 
			// map instance is a properties object (appears to be if instance name ends in 'properties')
		
		quoted = quoted.replaceAll(/[\{=\}]/){
			switch (it){
				case '=' : ':'; break;
				case '{' : '['; break;
				case '}' : ']'; break;
			}
		}

		return quoted
		
	}
	
	static List exportableDomainsList = [DataRepository, DataTag, StoredRoutes, CommServerSetup, PollingConfiguration, 
									RemotePollTarget, ServerTarget]
	
	
	boolean exportDomains(File directory, List domains){
		if (!domains){ return false }
		def first = domains[0]
		if (first == '*'){
			domains = exportableDomainsList
		}
			// First make sure directory exists and create if not
		if (!directory.exists()){
			directory.mkdirs()
		}
		log.info('Cycling through domains with count: ' + domains.size())
		log.info "Domains are: $domains"
			// Changing name of logger for 'each' closure as the name 'log' 
			// doesn't seem to work in the closure (may revert back to Grails default logger.)
		Logger ll = log
		domains.each { domain ->
			if (domain.class in [String]){
				ll.info "Exporting $domain from a String name specification."
					// Get the exportableDomains string map (may ha
				def domainInstance = exportableDomainsList.find{
					it.simpleName == domain
				}
				if (domainInstance == null){
					ll.warn "Domain Instance $domain was not found."
					return
				}
				def fileName = "${domain}.csv"
				File file = new File(directory,fileName)
				if (!file.exists()){
					file.createNewFile()
				}
				def filePath = file.getAbsolutePath()
				FileWriter fw = new FileWriter(file)
				domainInstance.exportToWriter(fw)
				fw.close()
				return true
			} else if (domain in exportableDomainsList){
				ll.info "Exporting $domain from a Class parameter specification."
				String domainName = domain.simpleName
				def fileName = "${domainName}.csv"
				File file = new File(directory,fileName)
				if (!file.exists()){
					file.createNewFile()
				}
				def filePath = file.getAbsolutePath()
				FileWriter fw = new FileWriter(file)
				domain.exportToWriter(fw)
				fw.close()
				return true
			} else {
				log.info "Domain not matching if else"
			}
			return false
		}
	}
	
	boolean importDomains(File directory, List domains){
		if (!domains){ return false }
		def first = domains[0]
		if (first == '*'){
			domains = exportableDomainsList
		}
		
		if (!directory.exists()){
			log.warn "Cannot import from directory: $directory. Directory does not exist."
			return false
		}
		
			// Create map of filenames and domains for import
		Map fileDomainMap = [:]
		Logger ll = log
		domains.each{ domain ->
			if (domain.class in [String]){
				def domainInstance = exportableDomainsList.find{
					it.simpleName == domain
				}
				if (domainInstance == null){
					ll.warn "Domain Instance $domain could not be parsed into a class instance."
					return
				}
			}
			String domainName = domain.simpleName
			def fileName = "${domainName}.csv"
			def file = new File(directory,fileName)
			
			fileDomainMap[file] = domain
		}
		
		importFilesIntoDomains(fileDomainMap)
	}
	
	/**
	 * Searches the exportableDomainsList for a Domain Class that matches the file prefix 
	 * and returns the Class.
	 * 
	 * @param file
	 * @return
	 */
	Class domainFromFile(def file){
		if (file in [File]){
			file = file.getName()
		}
		def filePrefix = file.contains(".") ? file?.substring(0,file.lastIndexOf('.')) : file
		def domainInstance = exportableDomainsList.find{
			it.simpleName == filePrefix
		}
		if (domainInstance == null){
			ll.warn "Domain Instance $domain could not be parsed into a class instance."
			return
		}
		return domainInstance
	}
	
	/**
	 * Given a map of files and domains, will import each file into the corresponding domain.
	 * @param fileDomainMap
	 * @return
	 */
	boolean importFilesIntoDomains( Map fileDomainMap){
		boolean result = true;
		fileDomainMap.each{file,domain ->
			result |= importFileIntoDomain(file,domain)
		}
		return result;
	}
	
	/**
	 * Given a file and a domain, imports the file into the domain.
	 * @param file
	 * @param domain
	 * @return
	 */
	boolean importFileIntoDomain(def file, def domain){
		Logger ll = log
		try {
			if (!file.exists()){
				log.warn "File ${file.getAbsolutePath()} does not exist. Skipping."
				return false
			}
			def fileReader = new FileReader(file)
			log.info "Data import from ${file.getAbsolutePath()} to $domain instance begun."
			domain.importFromReader(fileReader)
			fileReader.close()
			
			
			log.info "Data import from ${file.getAbsolutePath()} to $domain instance complete."
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	String uuidShortner(UUID id){
		def idstr = id.toString()
		idstr = '...' + idstr[-4..-1]
		
	}
	
}
