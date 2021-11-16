package dev.khbd.interp4j.maven;

import lombok.RequiredArgsConstructor;

import java.io.File;

/**
 * @author Sergei_Khadanovich
 */
@RequiredArgsConstructor
class OutputDirectoryInitializer {

    private final Config config;

    void initializeOutputDirectories() {
        initOutDirectory(config.getSourceOutputFolder());
        initOutDirectory(config.getTestSourceOutputFolder());
    }

    private void initOutDirectory(File output) {
        if (!output.exists()) {
            output.mkdirs();
        }
    }
}
