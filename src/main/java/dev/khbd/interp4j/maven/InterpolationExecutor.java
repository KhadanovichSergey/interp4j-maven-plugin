package dev.khbd.interp4j.maven;

import com.github.javaparser.ParseResult;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import dev.khbd.interp4j.processor.s.SInterpolationProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Sergei_Khadanovich
 */
@RequiredArgsConstructor
public class InterpolationExecutor {

    private final SInterpolationProcessor processor;
    private final InterpolationReporter reporter;
    private final Config config;

    /**
     * Execute interpolation.
     *
     * @param project maven project
     * @throws IOException if any io error occur
     */
    public void execute(MavenProject project) throws IOException {
        if (config.isTest()) {
            interpolateSourceRoots(project.getTestCompileSourceRoots());
        } else {
            interpolateSourceRoots(project.getCompileSourceRoots());
        }
    }

    private void interpolateSourceRoots(List<String> roots) throws IOException {
        for (String sourceRoot : roots) {
            reporter.debug("Interpolate source root: '%s'", sourceRoot);
            interpolateSourceRoot(sourceRoot);
        }
    }

    private void interpolateSourceRoot(String sourceRoot) throws IOException {
        SourceRoot root = new SourceRoot(Path.of(sourceRoot));
        List<ParseResult<CompilationUnit>> parseResults = root.tryToParse();
        for (ParseResult<CompilationUnit> result : parseResults) {
            result.ifSuccessful(cu -> cu.accept(processor, null));
            if (!result.getProblems().isEmpty()) {
                reportAllProblems(result.getProblems());
            }
        }
        root.saveAll(config.getOutputFolder().toPath());
    }

    private void reportAllProblems(List<Problem> problems) {
        for (Problem problem : problems) {
            reporter.error(problem.getVerboseMessage());
        }
    }
}
