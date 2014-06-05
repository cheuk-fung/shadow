package com.github.jengelman.gradle.plugins.shadow

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention

class ShadowPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.plugins.apply(JavaPlugin)
        createShadowConfiguration(project)
        configureShadowTask(project)
    }

    private void createShadowConfiguration(Project project) {
        project.configurations.create('shadow')
    }

    private void configureShadowTask(Project project) {
        JavaPluginConvention convention = project.convention.getPlugin(JavaPluginConvention)
        ShadowJar shadow = project.tasks.create('shadowJar', ShadowJar)
        shadow.conventionMapping.with {
            map('classifier') {
                'all'
            }
            map('manifest') {
                project.tasks.jar.manifest
            }
        }
        shadow.from(convention.sourceSets.main.output)
        shadow.from(project.configurations.runtime)
        shadow.exclude('META-INF/INDEX.LIST', 'META-INF/*.SF', 'META-INF/*.DSA', 'META-INF/*.RSA')

        ArchivePublishArtifact shadowArtifact = new ArchivePublishArtifact(shadow)
        project.configurations.shadow.artifacts.add(shadowArtifact)
    }
}
