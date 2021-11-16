package dev.khbd.interp4j.maven.compile;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import dev.khbd.interp4j.maven.AbstractTypeSolverBuilder;
import dev.khbd.interp4j.maven.InterpolationReporter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;

/**
 * @author Sergei_Khadanovich
 */
class CompileTypeSolverBuilder extends AbstractTypeSolverBuilder {

    CompileTypeSolverBuilder(InterpolationReporter reporter) {
        super(reporter);
    }

    @Override
    public TypeSolver build(MavenProject project) throws IOException {
        return buildSolverFromCompileScope(project);
    }
}
