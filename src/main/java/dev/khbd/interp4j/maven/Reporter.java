package dev.khbd.interp4j.maven;

import dev.khbd.interp4j.processor.s.ProblemKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.maven.plugin.logging.Log;

/**
 * @author Sergei_Khadanovich
 */
@RequiredArgsConstructor
public class Reporter {

    private final Log logger;
    @Getter
    private boolean errorOccurred;

    public void report(ProblemKind kind, String msg) {
        switch (kind) {
            case ERROR:
                error(msg);
                break;
            case WARN:
                warn(msg);
        }
    }

    public void info(String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    public void warn(String msg) {
        if (logger.isWarnEnabled()) {
            logger.warn(msg);
        }
    }

    public void error(String msg) {
        logger.error(msg);
        errorOccurred = true;
    }
}
