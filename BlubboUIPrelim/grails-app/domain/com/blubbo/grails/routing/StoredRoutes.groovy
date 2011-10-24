package com.blubbo.grails.routing

import java.io.Reader
import java.io.Writer
import java.util.Map

import org.grails.plugins.csv.CSVMapReader
import org.grails.plugins.csv.CSVWriter
import org.slf4j.LoggerFactory


class StoredRoutes {

    static constraints = {
    }
	
	
	static Writer exportToWriter(Writer writer){
		def b = new CSVWriter(writer,{it ->
			NAME { it.name }
			ENABLE { it.enable }
			TARGET_URL {it.targetUrl }
			DESCRIPTION { it.routeDescription }
			
		})
		
		LoggerFactory.getLogger(this).info "Exporting ${StoredRoutes.count()} StoredRoutes."
		StoredRoutes.list().each{
			LoggerFactory.getLogger(this).info "Creating CSV Output for StoredRoutes for : ${it.name}"
			b << it
		}
		return b.writer
	}
	
	static void importFromReader(Reader reader){
		def csvReader = new CSVMapReader(reader)
		csvReader.eachWithIndex { map, i ->
			def storedRoute = routeFromCSVMap(map)
			if(!storedRoute?.save()){
				if (!storedRoute){
					LoggerFactory.getLogger(this).info("Error Creating storedRoute #${i} from CSV file likely due to bad format or missing entry.")
				} else {
					LoggerFactory.getLogger(this).info("Error Creating storedRoute #${i} from CSV file: ${StoredRoutes.errors.allErrors.join('\n')}")
				}
			}
		}
	}
	
	static StoredRoutes routeFromCSVMap(Map map){
			// First check if route exists (by name) and only create one if necessary.
		String routeName = map['NAME']
		if (!routeName){
			LoggerFactory.getLogger(this).warn 'Invalid StoredRoute entry. No Name found.'
			return null
		}
		def route = StoredRoutes.findByName(routeName)
		if (route == null){
			route = new StoredRoutes(name: routeName)
		}
		if (!route){
			LoggerFactory.getLogger(this).warn("Could not find or create route for target. Errors: ${StoredRoutes.errors.allErrors.join('\n')}")
			return null
		}
		
		def enable = map['ENABLE']
		if (enable){
			route.enable = enable;
		}
		
		def targetUrl = map['TARGET_URL']
		if (targetUrl){
			route.targetUrl = targetUrl
		}
		
		def desc = map['DESCRIPTION']
		if (desc){
			route.routeDescription = desc
		}
			
		return route
	}
	
	
	String name
	boolean enable
	String targetUrl
	String routeDescription
	
	String toString(){
		"$name($targetUrl)"
	}
	
}
