package dev.khbd.interp4j.maven;

import lombok.experimental.UtilityClass;

import java.io.File;

/**
 * @author Sergei_Khadanovich
 */
@UtilityClass
class OutputDirectoryInitializer {

    static void initializeOutputDirectory(File output) {
        if (!output.exists()) {
            output.mkdirs();
        }
    }
}