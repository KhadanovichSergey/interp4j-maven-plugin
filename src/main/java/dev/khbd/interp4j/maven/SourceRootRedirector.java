package dev.khbd.interp4j.maven;

import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * @author Sergei_Khadanovich
 */
public interface SourceRootRedirector {

    /**
     * Redirect source roots to output folder.
     *
     * @param project      maven project
     * @param outputFolder new source output folder
     */
    void redirect(MavenProject project, File outputFolder);
}
