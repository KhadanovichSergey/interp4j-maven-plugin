package dev.khbd.interp4j.maven;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

/**
 * @author Sergei_Khadanovich
 */
public class InterpolationMojoTest {

    @Rule
    public MyMojoRule mojoRule = new MyMojoRule();
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

    @Test
    public void interpolate_productionOutputFolderWasOverridden_interpolateSources() throws Exception {
        File projectRoot = resources.getBasedir("production_output_was_overridden");

        mojoRule.executeMojo(projectRoot, "interpolate", "interpolate");

        File defaultOutput = new File(projectRoot, "target/interp4j");
        assertThat(defaultOutput).doesNotExist();
        File output = new File(projectRoot, "target/overridden-interp4j");
        assertThat(output).exists();
        TestResources.assertFileContents(projectRoot,
                "expected/dev/khbd/interp4j/examples/Main.java",
                "target/overridden-interp4j/dev/khbd/interp4j/examples/Main.java");
    }
}