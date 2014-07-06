package com.github.tiim.jwsplugin

import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

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
        makeXml(xml, this)
        writer.close()
    }

    def static makeXml(MarkupBuilder xml, GenerateJNLPTask t) {
        def jnlp = [:]
        jnlp.put('spec', getValue(t.spec))
        jnlp.put('codebase', getValue(t.codebase))
        jnlp.put('href', getValue(t.jnlpHref))
        jnlp.put('version', getValue(t.version))

        def files = getValue(t.jarFiles).collect { "$it.name" }

        xml.jnlp(jnlp) {
            'information' {
                "title"(getValue(t.title))
                if (t.vendor != null && getValue(t.vendor) != null)
                    'vendor'(getValue(t.vendor))
                if (t.homepage != null && getValue(t.homepage) != null)
                    'homepage'(href: getValue(t.homepage))
                if (t.appDescription != null && getValue(t.appDescription) != null) {
                    def d = getValue(t.appDescription)
                    if (d instanceof String) {
                        'description'(kind: 'short', d)
                    } else if (d instanceof Map) {
                        d = d as Map<String, Object>
                        d.keySet().each {
                            'description'(kind: it, getValue(d[it]))
                        }
                    }
                }
                if (t.icon != null && getValue(t.icon) != null && !(getValue(t.icon) as List).isEmpty()) {
                    t.icon.each {
                        'icon'(it)
                    }
                }
                if (t.offlineAllowed != null && getValue(t.offlineAllowed))
                    'offline-allowed'
            }
            if ((t.updateCheck != null && getValue(t.updateCheck) != null) ||
                    (t.updatePolicy != null && getValue(t.updatePolicy) != null)) {
                def up = [:]
                if (t.updateCheck != null && getValue(t.updateCheck) != null) {
                    up.put('check', getValue(t.updateCheck))
                }
                if (t.updatePolicy != null && getValue(t.updatePolicy) != null) {
                    up.put('policy', getValue(t.updatePolicy))
                }
                'update'(up)
            }
            resources {
                if (t.javaVersion != null)
                    'java'(version: getValue(t.javaVersion))
                files.each {
                    'jar'(href: it)
                }
            }
            'application-desc'('main-class': getValue(t.mainClass))
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
