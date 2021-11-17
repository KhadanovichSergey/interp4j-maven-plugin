package dev.khbd.interp4j.maven;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ClassLoaderTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import lombok.RequiredArgsConstructor;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Sergei_Khadanovich
 */
@RequiredArgsConstructor
public class TypeSolverBuilder {

    private final InterpolationReporter reporter;
    private final Config config;

    /**
     * Build type solver by maven project info.
     *
     * @param project maven project
     * @return type solver
     * @throws IOException if any io error occur
     */
    public TypeSolver build(MavenProject project) throws IOException {
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(buildArtifactsTypeSolver(project));
        typeSolver.add(buildSourceRootsTypeSolver(project, project.getCompileSourceRoots()));
        typeSolver.add(new ClassLoaderTypeSolver(ClassLoader.getSystemClassLoader()));
        if (config.isTest()) {
            typeSolver.add(buildSourceRootsTypeSolver(project, project.getTestCompileSourceRoots()));
        }
        return typeSolver;
    }

    private CombinedTypeSolver buildArtifactsTypeSolver(MavenProject project) throws IOException {
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        Set<Artifact> artifacts = project.getArtifacts();

        if (Objects.isNull(artifacts) || artifacts.isEmpty()) {
            reporter.debug("Any artifacts were not found for project '%s'", project.getName());
            return combinedTypeSolver;
        }

        for (Artifact artifact : artifacts) {
            String absolutePath = artifact.getFile().getAbsolutePath();
            reporter.debug("Artifact '%s' was found for project '%s'", absolutePath, project.getName());

            if (artifact.getFile().isFile()) {
                // jar or war file, may be :)
                JarTypeSolver artifactTypeSolver = new JarTypeSolver(absolutePath);
                combinedTypeSolver.add(artifactTypeSolver);
            } else {
                // suppose it is a sub model classes' directory, for example, '/target/classes'
                URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{artifact.getFile().toURL()});
                ClassLoaderTypeSolver urlClassLoaderTypeSolver = new ClassLoaderTypeSolver(urlClassLoader);
                combinedTypeSolver.add(urlClassLoaderTypeSolver);
            }
        }

        return combinedTypeSolver;
    }

    private CombinedTypeSolver buildSourceRootsTypeSolver(MavenProject project, List<String> sourceRoots) {
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();

        for (String sourceRoot : sourceRoots) {
            reporter.debug("Compile source root '%s' was found for project '%s'", sourceRoot, project.getName());

            if (!Files.exists(Path.of(sourceRoot))) {
                reporter.debug("Compile source root '%s' does not exist. Skip It!", sourceRoot);
                continue;
            }

            JavaParserTypeSolver compileSourceRootSolver = new JavaParserTypeSolver(sourceRoot);
            combinedTypeSolver.add(compileSourceRootSolver);
        }

        return combinedTypeSolver;
    }
}
