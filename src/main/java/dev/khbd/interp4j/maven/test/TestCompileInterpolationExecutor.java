package dev.khbd.interp4j.maven.test;

import dev.khbd.interp4j.maven.AbstractInterpolationExecutor;
import dev.khbd.interp4j.maven.InterpolationReporter;
import dev.khbd.interp4j.processor.s.SInterpolationProcessor;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

/**
 * @author Sergei_Khadanovich
 */
class TestCompileInterpolationExecutor extends AbstractInterpolationExecutor {

    TestCompileInterpolationExecutor(SInterpolationProcessor processor,
                                     InterpolationReporter reporter) {
        super(processor, reporter);
    }

    @Override
    public void execute(MavenProject project, File outputFolder) throws IOException {
        interpolateSourceRoots(project.getTestCompileSourceRoots(), outputFolder);
    }
}
