package ch.github.tiim.jwsplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPluginConvention
import org.gradle.api.plugins.JavaPlugin

/**
 * @author Tim
 * @since 07 - 2014
 */
class WebstartPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.apply(plugin: 'java')
        project.apply(plugin: 'application')


        def ext = project.extensions.create('webstart', WebstartPluginExtension)
        defaultValues(ext, project)
        addJNLPTask(project, ext)

    }

    static def addJNLPTask(Project pro, WebstartPluginExtension ext) {
        def t = pro.tasks.create('generateJNLP', GenerateJNLPTask) as GenerateJNLPTask

        t.description = "Creates an jnlp file used for java web start"
        t.outputFile = pro.file({ ext.outputFile })
        t.mainClass = { ext.mainClass }
        t.jarFiles = { ext.jarFiles }
        t.spec = { ext.spec }
        t.codebase = { ext.codebase }
        t.jnlpHref = { ext.jnlpHref }
        t.version = { ext.version }
        t.title = { ext.title }
        t.vendor = { ext.vendor }
        t.homepage = { ext.homepage }
        t.appDescription = { ext.appDescription }
        t.icon = { ext.icon }
        t.offlineAllowed = { ext.offlineAllowed }
        t.javaVersion = { ext.javaVersion }
        t.updateCheck = { ext.updateCheck }
        t.updatePolicy = { ext.updatePolicy }
    }

    static def defaultValues(WebstartPluginExtension ext, Project pro) {
        def appPlugin = pro.extensions.findByName("application") as ApplicationPluginConvention
        ext.title = { pro.archivesBaseName }
        ext.mainClass = { pro.mainClassName }
        ext.javaVersion = { pro.targetCompatibility.toString() }
        ext.version = { pro.version }
        ext.outputFile = { 'build/jnlp/' + pro.archivesBaseName + ".jnlp" }
        ext.jarFiles = {
            (pro.tasks[JavaPlugin.JAR_TASK_NAME].outputs.files + pro.configurations.runtime)
        }
    }
}
