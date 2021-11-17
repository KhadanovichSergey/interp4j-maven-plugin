package dev.khbd.interp4j.maven;

import com.github.javaparser.Range;
import dev.khbd.interp4j.processor.s.MessageType;
import dev.khbd.interp4j.processor.s.Reporter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.maven.plugin.logging.Log;

/**
 * @author Sergei_Khadanovich
 */
@RequiredArgsConstructor
public class InterpolationReporter implements Reporter {

    private final String label;
    private final Log logger;

    @Getter
    boolean anyErrorReported;

    public void debug(String template, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(buildMessage(template, args));
        }
    }

    public void error(String template, Object... args) {
        this.anyErrorReported = true;

        logger.error(buildMessage(template, args));
    }

    public void warn(String template, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(buildMessage(template, args));
        }
    }

    public void info(String template, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(buildMessage(template, args));
        }
    }

    private String buildMessage(String template, Object... args) {
        String msg = String.format(template, args);
        return "[ " + label + " ] " + msg;
    }

    @Override
    public void report(Range range, String message, MessageType type) {
        switch (type) {
            case ERROR:
                error(message);
                break;
            case WARN:
                warn(message);
                break;
            case INFO:
                info(message);
        }
    }
}
