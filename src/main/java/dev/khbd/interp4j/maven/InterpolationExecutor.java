package dev.khbd.interp4j.maven;

import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

/**
 * @author Sergei_Khadanovich
 */
public interface InterpolationExecutor {

    /**
     * Execute interpolation.
     *
     * @param project      maven project
     * @param outputFolder output folder
     * @throws IOException if any io error occur
     */
    void execute(MavenProject project, File outputFolder) throws IOException;
}
