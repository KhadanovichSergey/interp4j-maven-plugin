package dev.khbd.interp4j.maven;

import lombok.Builder;
import lombok.Getter;

import java.io.File;

/**
 * @author Sergei_Khadanovich
 */
@Builder
@Getter
public class Config {
    private final File outputFolder;
    private final boolean test;
}
