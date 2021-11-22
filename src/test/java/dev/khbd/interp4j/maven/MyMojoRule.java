package dev.khbd.interp4j.maven;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Sergei_Khadanovich
 */
public class MyMojoRule extends MojoRule {

    public void executeMojo(File basedir, String goal, String executionId) throws Exception {
        MavenProject project = readMavenProject(basedir);
        MavenSession session = newMavenSession(project);
        MojoExecution execution = newMojoExecution(project, goal, executionId);
        this.executeMojo(session, project, execution);
    }

    private MojoExecution newMojoExecution(MavenProject project, String goal, String executionId) throws Exception {
        AbstractMojoTestCase testCase = getTestCase();

        MojoDescriptor mojoDescriptor = getDescriptor(testCase, goal);

        Plugin plugin = project.getPlugin(mojoDescriptor.getPluginDescriptor().getPluginLookupKey());
        Map<String, PluginExecution> executionsAsMap = plugin.getExecutionsAsMap();
        PluginExecution pluginExecution = executionsAsMap.get(executionId);

        MojoExecution execution = new MojoExecution(mojoDescriptor, (Xpp3Dom) pluginExecution.getConfiguration());

        finalizeMojoConfiguration(testCase, execution);

        return execution;
    }

    private AbstractMojoTestCase getTestCase() throws Exception {
        Field field = MojoRule.class.getDeclaredField("testCase");
        field.setAccessible(true);
        return (AbstractMojoTestCase) field.get(this);
    }

    private MojoDescriptor getDescriptor(AbstractMojoTestCase testCase, String goal) throws Exception {
        Field field = AbstractMojoTestCase.class.getDeclaredField("mojoDescriptors");
        field.setAccessible(true);
        Map<String, MojoDescriptor> descriptors = (Map<String, MojoDescriptor>) field.get(testCase);
        return descriptors.get(goal);
    }

    private void finalizeMojoConfiguration(AbstractMojoTestCase testCase, MojoExecution execution) throws Exception {
        Method method = AbstractMojoTestCase.class.getDeclaredMethod("finalizeMojoConfiguration", MojoExecution.class);
        method.setAccessible(true);
        method.invoke(testCase, execution);
    }

}
