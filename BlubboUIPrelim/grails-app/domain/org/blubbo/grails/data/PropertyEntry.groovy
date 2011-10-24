package org.blubbo.grails.data


class PropertyEntry {

    static constraints = {
    }
	
	String key
	String value
	
	String toString(){
		"$key:$value"
	}
}
