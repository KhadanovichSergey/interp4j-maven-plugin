package dev.khbd.interp4j.maven.test;

import dev.khbd.interp4j.maven.SourceRootRedirector;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * @author Sergei_Khadanovich
 */
class TestCompileSourceRootRedirector implements SourceRootRedirector {
    @Override
    public void redirect(MavenProject project, File outputFolder) {
        project.getTestCompileSourceRoots().clear();
        project.addTestCompileSourceRoot(outputFolder.getAbsolutePath());
    }
}
