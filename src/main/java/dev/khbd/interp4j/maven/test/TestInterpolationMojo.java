package dev.khbd.interp4j.maven.test;

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
@Mojo(name = "test-interpolate",
        defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES,
        requiresDependencyResolution = ResolutionScope.TEST
)
public class TestInterpolationMojo extends AbstractInterpolationMojo {

    /**
     * Output directory where interp4j must generate his output (interpolated test source code).
     */
    @Parameter(property = "Output folder for test sources",
            defaultValue = "${project.build.directory}/generated-test-sources/interp4j")
    private File outputFolder;

    @Override
    protected Config buildConfig() {
        return Config.builder()
                .outputFolder(outputFolder)
                .build();
    }

    @Override
    protected SourceRootRedirector getSourceRootRedirector() {
        return new TestCompileSourceRootRedirector();
    }

    @Override
    protected TypeSolverBuilder getTypeSolverBuilder(InterpolationReporter reporter) {
        return new TestCompileTypeSolverBuilder(reporter);
    }

    @Override
    protected InterpolationExecutor getInterpolationExecutor(SInterpolationProcessor processor,
                                                             InterpolationReporter reporter) {
        return new TestCompileInterpolationExecutor(processor, reporter);
    }
}
