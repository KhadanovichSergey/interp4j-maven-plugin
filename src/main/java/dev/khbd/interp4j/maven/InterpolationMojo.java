package dev.khbd.interp4j.maven;

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
    @Parameter(property = "outputFolder",
            defaultValue = "${project.build.directory}/interp4j")
    private File outputFolder;

    @Override
    protected Config buildConfig() {
        return Config.builder()
                .outputFolder(outputFolder)
                .test(false)
                .build();
    }
}
