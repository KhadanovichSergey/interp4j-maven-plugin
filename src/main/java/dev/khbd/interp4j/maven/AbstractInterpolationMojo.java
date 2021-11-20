package dev.khbd.interp4j.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;

/**
 * @author Sergei_Khadanovich
 */
public abstract class AbstractInterpolationMojo extends AbstractMojo {

    /**
     * Current maven project info.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;

    @Override
    public void execute() throws MojoFailureException {
        if (project.getPackaging().equals("pom")) {
            return;
        }

        Config config = buildConfig();

        Reporter reporter = new Reporter(getLog());
        OutputDirectoryInitializer.initializeOutputDirectory(config.getOutputFolder());

        try {
            InterpolationExecutor executor = new InterpolationExecutor(reporter, config);
            executor.execute(project);

            new SourceRootRedirector(config).redirect(project);

            if (reporter.isErrorOccurred()) {
                throw new MojoFailureException("Interpolation was completed with errors");
            }
        } catch (IOException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    protected abstract Config buildConfig();
}
