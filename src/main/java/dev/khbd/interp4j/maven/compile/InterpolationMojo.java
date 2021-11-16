package dev.khbd.interp4j.maven.compile;

import dev.khbd.interp4j.maven.AbstractInterpolationMojo;
import dev.khbd.interp4j.maven.Config;
import dev.khbd.interp4j.maven.InterpolationExecutor;
import dev.khbd.interp4j.maven.InterpolationReporter;
import dev.khbd.interp4j.maven.SourceRootRedirector;
import dev.khbd.interp4j.maven.TypeSolverBuilder;
import dev.khbd.interp4j.processor.s.SInterpolationProcessor;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;

/**
 * @author Sergei_Khadanovich
 */
@Mojo(name = "interpolate",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.COMPILE
)
public class InterpolationMojo extends AbstractInterpolationMojo {

    /**
     * Output directory where interp4j must generate his output (interpolated source code).
     */
    @Parameter(property = "Output folder for sources",
            defaultValue = "${project.build.directory}/generated-sources/interp4j")
    private File outputFolder;

    @Override
    protected Config buildConfig() {
        return Config.builder()
                .outputFolder(outputFolder)
                .build();
    }

    @Override
    protected SourceRootRedirector getSourceRootRedirector() {
        return new CompileSourceRootRedirector();
    }

    @Override
    protected TypeSolverBuilder getTypeSolverBuilder(InterpolationReporter reporter) {
        return new CompileTypeSolverBuilder(reporter);
    }

    @Override
    protected InterpolationExecutor getInterpolationExecutor(SInterpolationProcessor processor,
                                                             InterpolationReporter reporter) {
        return new CompileInterpolationExecutor(processor, reporter);
    }
}
