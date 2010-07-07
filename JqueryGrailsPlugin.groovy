/*
 * Copyright 2007-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import grails.util.GrailsUtil;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.web.taglib.*
import org.codehaus.groovy.grails.plugins.jquery.JQueryConfig
import org.codehaus.groovy.grails.plugins.jquery.JQueryProvider
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class JqueryGrailsPlugin {
	def version = "1.4.2.5"

    static SHIPPED_VERSION = '1.4.2'
    static SHIPPED_SRC_DIR = 'jquery'

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.2 > *"

	def dependsOn = [:]

	def author = "Sergey Nebolsin, Craig Jones and Finn Herpich"
	def authorEmail = "nebolsin@gmail.com, craigjones@maximsc.com and finn.herpich@marfinn-software.de"
	def title = "JQuery for Grails"
	def description = "Provides integration for the JQuery library with grails JavascriptProvider"
	def documentation = "http://grails.org/JQuery+Plugin"

	static jQueryVersion
	static jQuerySources
                     
	def doWithSpring = {
		jQueryConfig(JQueryConfig)
	}

    void loadConfig() {
    	GroovyClassLoader classLoader = new GroovyClassLoader(getClass().getClassLoader())
     	def confClass
        try {
     	    confClass = classLoader.loadClass('JQueryConfig')
 	    } catch (Exception e) {
            // <gulp>
 	    }
    	ConfigObject config = confClass ? new ConfigSlurper().parse(confClass).merge(ConfigurationHolder.config) : ConfigurationHolder.config

    	JqueryGrailsPlugin.jQueryVersion = config.jquery.version instanceof String ? config.jquery.version : JqueryGrailsPlugin.SHIPPED_VERSION
    	JqueryGrailsPlugin.jQuerySources = config.jquery.sources instanceof String ? config.jquery.sources : JqueryGrailsPlugin.SHIPPED_SRC_DIR
    }
    
	def doWithApplicationContext = { applicationContext ->
        // We need to init our own config first
        loadConfig()

		if(GrailsUtil.environment == GrailsApplication.ENV_DEVELOPMENT) {
			JavascriptTagLib.LIBRARY_MAPPINGS.jquery = ["${JqueryGrailsPlugin.jQuerySources}/jquery-${JqueryGrailsPlugin.jQueryVersion}"]
		} else {
			JavascriptTagLib.LIBRARY_MAPPINGS.jquery = ["${JqueryGrailsPlugin.jQuerySources}/jquery-${JqueryGrailsPlugin.jQueryVersion}-min"]
		}

		def jQueryConfig = applicationContext.jQueryConfig
		jQueryConfig.init()

		if(jQueryConfig.defaultPlugins) {
			jQueryConfig.defaultPlugins.each { pluginName ->
				jQueryConfig.plugins."$pluginName".each {fileName ->
					JavascriptTagLib.LIBRARY_MAPPINGS.jquery += ["${JqueryGrailsPlugin.jQuerySources}/${fileName}"[0..-4]]
				}
			}
		}

		JavascriptTagLib.PROVIDER_MAPPINGS.jquery = JQueryProvider.class
	}
}
