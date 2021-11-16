package dev.khbd.interp4j.maven;

import lombok.RequiredArgsConstructor;
import org.apache.maven.project.MavenProject;

/**
 * @author Sergei_Khadanovich
 */
@RequiredArgsConstructor
class SourceRootRedirector {

    private final Config config;

    void redirect(MavenProject project) {
        redirectCompileSourceRoot(project);
        redirectTestCompileSourceRoot(project);
    }

    void redirectCompileSourceRoot(MavenProject project) {
        project.getCompileSourceRoots().clear();
        project.addCompileSourceRoot(config.getSourceOutputFolder().getAbsolutePath());
    }

    void redirectTestCompileSourceRoot(MavenProject project) {
        project.getTestCompileSourceRoots().clear();
        project.addTestCompileSourceRoot(config.getTestSourceOutputFolder().getAbsolutePath());
    }
}
