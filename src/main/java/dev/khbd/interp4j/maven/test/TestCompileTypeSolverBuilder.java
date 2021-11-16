package dev.khbd.interp4j.maven.test;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import dev.khbd.interp4j.maven.AbstractTypeSolverBuilder;
import dev.khbd.interp4j.maven.InterpolationReporter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;

/**
 * @author Sergei_Khadanovich
 */
class TestCompileTypeSolverBuilder extends AbstractTypeSolverBuilder {

    TestCompileTypeSolverBuilder(InterpolationReporter reporter) {
        super(reporter);
    }

    @Override
    public TypeSolver build(MavenProject project) throws IOException {
        CombinedTypeSolver combinedTypeSolver = buildSolverFromCompileScope(project);
        combinedTypeSolver.add(buildSourceRootsTypeSolver(project, project.getTestCompileSourceRoots()));
        return combinedTypeSolver;
    }
}
