package dev.khbd.interp4j.maven;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import dev.khbd.interp4j.processor.s.SInterpolationProcessor;
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

        OutputDirectoryInitializer.initializeOutputDirectory(config.getOutputFolder());

        InterpolationReporter reporter = new InterpolationReporter(getLog());

        try {
            TypeSolver typeSolver = getTypeSolverBuilder(reporter).build(project);
            SInterpolationProcessor processor = new SInterpolationProcessor(typeSolver, reporter);

            InterpolationExecutor executor = getInterpolationExecutor(processor, reporter);
            executor.execute(project, config.getOutputFolder());

            getSourceRootRedirector().redirect(project, config.getOutputFolder());

            if (reporter.isAnyErrorReported()) {
                throw new MojoFailureException("Interpolation was completed with errors");
            }
        } catch (IOException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    protected abstract Config buildConfig();

    protected abstract SourceRootRedirector getSourceRootRedirector();

    protected abstract TypeSolverBuilder getTypeSolverBuilder(InterpolationReporter reporter);

    protected abstract InterpolationExecutor getInterpolationExecutor(SInterpolationProcessor processor,
                                                                      InterpolationReporter reporter);
}
