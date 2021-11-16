package dev.khbd.interp4j.maven;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import org.apache.maven.project.MavenProject;

import java.io.IOException;

/**
 * @author Sergei_Khadanovich
 */
public interface TypeSolverBuilder {

    /**
     * Build type solver by maven project info.
     *
     * @param project maven project
     * @return type solver
     * @throws IOException if any io error occur
     */
    TypeSolver build(MavenProject project) throws IOException;
}
