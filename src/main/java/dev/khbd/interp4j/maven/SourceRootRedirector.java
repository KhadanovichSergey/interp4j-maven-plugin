package dev.khbd.interp4j.maven;

import lombok.RequiredArgsConstructor;
import org.apache.maven.project.MavenProject;

/**
 * @author Sergei_Khadanovich
 */
@RequiredArgsConstructor
public class SourceRootRedirector {

    private final Config config;

    /**
     * Redirect source roots to output folder.
     *
     * @param project maven project
     */
    public void redirect(MavenProject project) {
        if (config.isTest()) {
            redirectTestCompileSourceRoots(project);
        } else {
            redirectCompileSourceRoots(project);
        }
    }

    private void redirectCompileSourceRoots(MavenProject project) {
        project.getCompileSourceRoots().clear();
        project.addCompileSourceRoot(config.getOutputFolder().getAbsolutePath());
    }

    private void redirectTestCompileSourceRoots(MavenProject project) {
        project.getTestCompileSourceRoots().clear();
        project.addTestCompileSourceRoot(config.getOutputFolder().getAbsolutePath());
    }
}
