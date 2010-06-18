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
	def version = "1.4.2.2"
	def dependsOn = [:]
	def author = "Sergey Nebolsin, Craig Jones and Finn Herpich"
	def authorEmail = "nebolsin@gmail.com, craigjones@maximsc.com and finn.herpich@marfinn-software.de"
	def title = "JQuery for Grails"
	def description = "Provides integration for the JQuery library with grails JavascriptProvider"
	def documentation = "http://grails.org/JQuery+Plugin"

	GroovyClassLoader classLoader = new GroovyClassLoader(getClass().getClassLoader())

	// Applied GRAILSPLUGINS-2056
	ConfigObject config = new ConfigSlurper().parse(classLoader.loadClass('JQueryConfig'))
							.merge(new ConfigSlurper().parse(classLoader.loadClass('Config')))

	def jQueryVersion = config.jquery.version
	def jQuerySources = config.jquery.sources

	def doWithSpring = {
		jQueryConfig(JQueryConfig)
	}

	def doWithApplicationContext = {applicationContext ->
		if(GrailsUtil.environment == GrailsApplication.ENV_DEVELOPMENT) {
			JavascriptTagLib.LIBRARY_MAPPINGS.jquery = ["${jQuerySources}/jquery-${jQueryVersion}"]
		} else {
			JavascriptTagLib.LIBRARY_MAPPINGS.jquery = ["${jQuerySources}/jquery-${jQueryVersion}.min"]
		}

		def jQueryConfig = applicationContext.jQueryConfig
		jQueryConfig.init()

		if(jQueryConfig.defaultPlugins) {
			jQueryConfig.defaultPlugins.each { pluginName ->
				jQueryConfig.plugins."$pluginName".each {fileName ->
					JavascriptTagLib.LIBRARY_MAPPINGS.jquery += ["${jQuerySources}/${fileName}"[0..-4]]
				}
			}
		}

		JavascriptTagLib.PROVIDER_MAPPINGS.jquery = JQueryProvider.class
	}
}
