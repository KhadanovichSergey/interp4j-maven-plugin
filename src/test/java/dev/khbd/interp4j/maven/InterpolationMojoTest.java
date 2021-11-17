package dev.khbd.interp4j.maven;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

/**
 * @author Sergei_Khadanovich
 */
public class InterpolationMojoTest {

    @Rule
    public MojoRule mojoRule = new MojoRule();
    @Rule
    public TestResources resources = new TestResources();

    @Test
    public void interpolate_singleModuleProject_interpolateSources() throws Exception {
        File projectRoot = resources.getBasedir("single_module");
        mojoRule.executeMojo(projectRoot, "interpolate");

        File output = new File(projectRoot, "target/interp4j");
        assertThat(output).exists();
        TestResources.assertFileContents(projectRoot,
                "expected/dev/khbd/interp4j/examples/Main.java",
                "target/interp4j/dev/khbd/interp4j/examples/Main.java");
    }
}