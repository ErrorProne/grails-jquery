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

//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'Ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
// Ant.mkdir(dir:"/Users/nebolsin/Projects/grails-jquery/grails-app/jobs")
//

Ant.property(environment:"env")
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"

includeTargets << grailsScript("Init")
checkVersion()
configureProxy()

// Hard coded for installation purpose
def jQueryVersion = '1.4.1'
def jQuerySources = 'jquery'

Ant.sequential {
    event("StatusUpdate", ["Downloading JQuery ${jQueryVersion}"])

    def files = ["jquery-${jQueryVersion}.js", "jquery-${jQueryVersion}.min.js"]

    mkdir(dir:"${basedir}/web-app/js/${jQuerySources}")
    files.each {
        get(dest:"${basedir}/web-app/js/${jQuerySources}/${it}",
            src:"http://code.jquery.com/${it}",
            verbose:true)
    }
}
event("StatusFinal", ["JQuery ${jQueryVersion} installed successfully"])

