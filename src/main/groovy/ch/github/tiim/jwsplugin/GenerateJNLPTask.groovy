package ch.github.tiim.jwsplugin

import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Optional

import java.util.concurrent.Callable

/**
 * @author Tim
 * @since 07 - 2014
 */
class GenerateJNLPTask extends DefaultTask {

    @OutputFile
    def File outputFile

    @InputFiles
    def jarFiles

    @Input
    def mainClass

    @Input
    def spec

    @Input
    def codebase

    @Input
    def jnlpHref

    @Input
    def version

    @Input
    def title

    @Optional
    @Input
    def vendor

    @Optional
    @Input
    def homepage

    @Optional
    @Input
    def appDescription

    @Optional
    @Input
    def icon

    @Input
    def offlineAllowed

    @Input
    def javaVersion

    @Input
    def updateCheck

    @Input
    def updatePolicy


    @TaskAction
    def generateJNLP() {
        outputFile.parentFile.mkdirs()
        def writer = new FileWriter(outputFile)
        def xml = new MarkupBuilder(writer)
        makeXml(xml)
        writer.close()
    }

    def makeXml(MarkupBuilder xml) {
        def jnlp = [:]
        jnlp.put('spec', getValue(spec))
        jnlp.put('codebase', getValue(codebase))
        jnlp.put('href', getValue(jnlpHref))
        jnlp.put('version', getValue(version))

        def files = getValue(jarFiles).collect { "$it.name" }

        xml.jnlp(jnlp) {
            'information' {
                'title'(getValue(title))
                if (vendor != null && getValue(vendor) != null)
                    'vendor'(getValue(vendor))
                if (homepage != null && getValue(homepage) != null)
                    'homepage'(href: getValue(homepage))
                if (appDescription != null && getValue(appDescription) != null) {
                    def d = getValue(appDescription)
                    if (d instanceof String) {
                        'description'(kind: 'short', d)
                    } else if (d instanceof Map) {
                        d = d as Map<String, Object>
                        d.keySet().each {
                            'description'(kind: it, getValue(d[it]))
                        }
                    }
                }
                if (icon != null && getValue(icon) != null && !(getValue(icon) as List).isEmpty()) {
                    icon.each {
                        'icon'(it)
                    }
                }
                if (offlineAllowed != null && getValue(offlineAllowed))
                    'offline-allowed'
            }
            if ((updateCheck != null && getValue(updateCheck) != null) ||
                    (updatePolicy != null && getValue(updatePolicy) != null)) {
                def up = [:]
                if (updateCheck != null && getValue(updateCheck)) {
                    up.put('check', getValue(updateCheck))
                }
                if (updatePolicy != null && getValue(updatePolicy) != null) {
                    up.put('policy', getValue(updatePolicy))
                }
                'update'(up)
            }
            resources {
                if (javaVersion != null)
                    'java'(version: getValue(javaVersion))
                files.each {
                    'jar'(href: it)
                }
            }
            'application-desc'('main-class': getValue(mainClass))
        }
    }

    static def getValue(o) {
        if (o instanceof Callable) {
            return getValue(o.call())
        }
        if (o instanceof Closure) {
            return getValue(o())
        }
        return o
    }
}
