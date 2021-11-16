package dev.khbd.interp4j.maven;

import com.github.javaparser.ParseResult;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import dev.khbd.interp4j.processor.s.SInterpolationProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Sergei_Khadanovich
 */
@RequiredArgsConstructor
class InterpolationExecutor {

    private final SInterpolationProcessor processor;
    private final InterpolationReporter reporter;
    private final Config config;

    void execute(MavenProject project) throws IOException {
        interpolateSourceRoots(project.getCompileSourceRoots(), config.getSourceOutputFolder());
        interpolateSourceRoots(project.getTestCompileSourceRoots(), config.getTestSourceOutputFolder());
    }

    void interpolateSourceRoots(List<String> roots, File output) throws IOException {
        for (String sourceRoot : roots) {
            reporter.debug("Interpolate source root: '%s'", sourceRoot);
            interpolateSourceRoot(sourceRoot, output);
        }
    }

    void interpolateSourceRoot(String sourceRoot, File output) throws IOException {
        SourceRoot root = new SourceRoot(Path.of(sourceRoot));
        List<ParseResult<CompilationUnit>> parseResults = root.tryToParse();
        for (ParseResult<CompilationUnit> result : parseResults) {
            result.ifSuccessful(cu -> cu.accept(processor, null));
            if (!result.getProblems().isEmpty()) {
                reportAllProblems(result.getProblems());
            }
        }
        root.saveAll(output.toPath());
    }

    private void reportAllProblems(List<Problem> problems) {
        for (Problem problem : problems) {
            reporter.error(problem.getVerboseMessage());
        }
    }
}
