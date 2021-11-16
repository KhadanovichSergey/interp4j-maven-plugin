package dev.khbd.interp4j.maven;

import com.github.javaparser.ParseResult;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import dev.khbd.interp4j.processor.s.SInterpolationProcessor;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Sergei_Khadanovich
 */
@RequiredArgsConstructor
public abstract class AbstractInterpolationExecutor implements InterpolationExecutor {

    private final SInterpolationProcessor processor;
    private final InterpolationReporter reporter;

    protected void interpolateSourceRoots(List<String> roots, File outputFolder) throws IOException {
        for (String sourceRoot : roots) {
            reporter.debug("Interpolate source root: '%s'", sourceRoot);
            interpolateSourceRoot(sourceRoot, outputFolder);
        }
    }

    private void interpolateSourceRoot(String sourceRoot, File outputFolder) throws IOException {
        SourceRoot root = new SourceRoot(Path.of(sourceRoot));
        List<ParseResult<CompilationUnit>> parseResults = root.tryToParse();
        for (ParseResult<CompilationUnit> result : parseResults) {
            result.ifSuccessful(cu -> cu.accept(processor, null));
            if (!result.getProblems().isEmpty()) {
                reportAllProblems(result.getProblems());
            }
        }
        root.saveAll(outputFolder.toPath());
    }

    private void reportAllProblems(List<Problem> problems) {
        for (Problem problem : problems) {
            reporter.error(problem.getVerboseMessage());
        }
    }
}
