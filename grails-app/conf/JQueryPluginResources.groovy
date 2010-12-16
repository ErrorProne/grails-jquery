// Resource declarations for Resources plugin
// This is a bit ugly, we'll find a way to make this better in future
def appCtx = org.codehaus.groovy.grails.commons.ApplicationHolder.application.mainContext
def plugin = appCtx.pluginManager.getGrailsPlugin('jquery')
def jqver = plugin.instance.SHIPPED_VERSION

modules = {
    'jquery' {
        resource id:'js', url:[plugin: 'jquery', dir:'js/jquery', file:"jquery-${jqver}.min.js"], 
            disposition:'head', nominify: true
    }

    'jquery-dev' {
        resource id:'js', url:[plugin: 'jquery', dir:'js/jquery', file:"jquery-${jqver}.js"], 
            disposition:'head'
    }
}