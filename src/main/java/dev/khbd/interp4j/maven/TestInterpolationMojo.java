package dev.khbd.interp4j.maven;

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
            defaultValue = "${project.build.directory}/test-interp4j")
    private File outputFolder;

    @Override
    protected Config buildConfig() {
        return Config.builder()
                .outputFolder(outputFolder)
                .test(true)
                .build();
    }
}
