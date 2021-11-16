package dev.khbd.interp4j.maven;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import dev.khbd.interp4j.processor.s.SInterpolationProcessor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

/**
 * @author Sergei_Khadanovich
 */
@Mojo(name = "interpolate",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.TEST
)
public class InterpolationMojo extends AbstractMojo {

    /**
     * Output directory where interp4j must generate his output (interpolated source code).
     */
    @Parameter(property = "Output folder for sources",
            defaultValue = "${project.build.directory}/generated-sources/interp4j")
    private File outputFolder;

    /**
     * Output directory where interp4j must generate his output (interpolated test source code).
     */
    @Parameter(property = "Output folder for test sources",
            defaultValue = "${project.build.directory}/generated-test-sources/interp4j")
    private File testOutputFolder;

    /**
     * Current maven project info.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (project.getPackaging().equals("pom")) {
            return;
        }

        Config config = buildConfig();

        new OutputDirectoryInitializer(config).initializeOutputDirectories();

        InterpolationReporter reporter = new InterpolationReporter(getLog());

        try {
            TypeSolver typeSolver = new TypeSolverBuilder(reporter)
                    .build(project);
            SInterpolationProcessor processor = new SInterpolationProcessor(typeSolver, reporter);

            InterpolationExecutor executor = new InterpolationExecutor(processor, reporter, config);
            executor.execute(project);

            new SourceRootRedirector(config).redirect(project);

            if (reporter.isAnyErrorReported()) {
                throw new MojoFailureException("Interpolation was completed with errors");
            }
        } catch (IOException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    private Config buildConfig() {
        return Config.builder()
                .sourceOutputFolder(outputFolder)
                .testSourceOutputFolder(testOutputFolder)
                .build();
    }
}
