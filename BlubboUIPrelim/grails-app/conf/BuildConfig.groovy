
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
	useOrigin true // Fix from Web purported to fix Maven Snapshot issues with ivy
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
		excludes 'hsqldb'	// Want to exclude included version of hsqldb as it is v1.8 and we want to use 2.0+
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
		mavenRepo "http://repo1.maven.org/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.13'
		
		runtime 'org.hsqldb:hsqldb:2.0.0'
		
		compile 'com.google.guava:guava:r07', 'org.simpleframework:simple-xml:2.4.1', 
				'org.apache.camel:camel-mina:2.6.0',
				
				'org.scadajack:scadajack-camel-mbtcp:0.0.2-SNAPSHOT',
				'org.scadajack:scadajack-camel-modbus-server:0.0.2-SNAPSHOT',
				'org.scadajack:scadajack-camel-adstcp:0.0.2-SNAPSHOT',
				'org.blubbo:org.blubbo.camel:0.0.2-SNAPSHOT',
				'org.scadajack:scadajack-camel-polling:0.0.2-SNAPSHOT',
				//'org.blubbo:org.blubbo.data.impl:0.0.1-SNAPSHOT'
				// Note this line below is equivalent to above commented out line but with changing set to 
				// true. This is used to try to force Ivy to reload from the maven repository rather than 
				// just using from its cache. Don't think it works but haven't verified yet.
				[group:'org.blubbo', name:'org.blubbo.data.impl', version:'0.0.2-SNAPSHOT', changing:'true'],
				//'org.blubbo:org.blubbo.data:0.0.1-SNAPSHOT',
				[group:'org.blubbo', name:'org.blubbo.data', version:'0.0.2-SNAPSHOT', changing:'true']
				 
		test 'org.apache.camel:camel-test:2.6.0'
    }
}
