package dev.khbd.interp4j.maven;

import com.github.javaparser.ParseResult;
import com.github.javaparser.Position;
import com.github.javaparser.Problem;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import dev.khbd.interp4j.processor.s.ProcessingProblem;
import dev.khbd.interp4j.processor.s.ProcessingResult;
import dev.khbd.interp4j.processor.s.SInterpolationProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * @author Sergei_Khadanovich
 */
@RequiredArgsConstructor
public class InterpolationExecutor {

    private final Reporter reporter;
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
            reporter.info(String.format("Interpolate source root: '%s'", sourceRoot));
            interpolateSourceRoot(sourceRoot);
        }
    }

    private void interpolateSourceRoot(String sourceRoot) throws IOException {
        SourceRoot root = new SourceRoot(Path.of(sourceRoot));
        List<ParseResult<CompilationUnit>> parseResults = root.tryToParse();
        for (ParseResult<CompilationUnit> parseResult : parseResults) {
            if (!parseResult.getProblems().isEmpty()) {
                reportParseProblems(parseResult.getProblems());
            }
            parseResult.ifSuccessful(unit -> {
                SInterpolationProcessor processor = SInterpolationProcessor.getInstance();
                ProcessingResult processingResult = processor.process(unit);
                reportProcessingProblems(unit, processingResult.getProblems());
            });
        }
        root.saveAll(config.getOutputFolder().toPath());
    }

    private void reportProcessingProblems(CompilationUnit unit, List<ProcessingProblem> problems) {
        for (ProcessingProblem problem : problems) {
            reportProcessingProblem(unit, problem);
        }
    }

    private void reportProcessingProblem(CompilationUnit unit, ProcessingProblem problem) {
        reporter.report(problem.getKind(), buildMessage(unit, problem));
    }

    private String buildMessage(CompilationUnit unit, ProcessingProblem problem) {
        StringBuilder msgBuilder = new StringBuilder();

        String compilationUnitPath = unit.getStorage()
                .map(st -> st.getPath().toString())
                .orElse(null);
        if (Objects.nonNull(compilationUnitPath)) {
            msgBuilder.append(compilationUnitPath);
        }

        Range range = problem.getRange();
        if (Objects.nonNull(range)) {
            msgBuilder.append(":");
            addPosition(msgBuilder, range.begin);
        }

        msgBuilder.append(" ");
        msgBuilder.append(problem.getMessage());

        return msgBuilder.toString();
    }

    private void addPosition(StringBuilder builder, Position begin) {
        builder.append("[");
        builder.append(begin.line);
        builder.append(",");
        builder.append(begin.column);
        builder.append("]");
    }

    private void reportParseProblems(List<Problem> problems) {
        for (Problem problem : problems) {
            reporter.error(problem.getVerboseMessage());
        }
    }
}
