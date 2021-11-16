package dev.khbd.interp4j.maven.compile;

import dev.khbd.interp4j.maven.SourceRootRedirector;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * @author Sergei_Khadanovich
 */
class CompileSourceRootRedirector implements SourceRootRedirector {
    @Override
    public void redirect(MavenProject project, File outputFolder) {
        project.getCompileSourceRoots().clear();
        project.addCompileSourceRoot(outputFolder.getAbsolutePath());
    }
}
