package org.blubbo.grails.data

class MetadataEntry {

    static constraints = {
    }
	
	String key
	String value
	
	String toString(){
		"$key:$value"
	}
}
