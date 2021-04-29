package com.github.gregwhitaker.flywayutils.migrations;

import com.github.gregwhitaker.flywayutils.FlywayUtilsPlugin;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Creates the migration directory structure.
 */
public class InitMigrationDirectoryTask extends DefaultTask {

    public InitMigrationDirectoryTask() {
        setGroup(FlywayUtilsPlugin.GROUP_NAME);
        setDescription("Initializes the flyway migration directory structure.");
    }

    @TaskAction
    public void run() {
        createMigrationDirectory();
        createMigrationEnvDirectory();
    }

    private void createMigrationDirectory() {
        final Path migrationPath = Paths.get(getProject().getRootProject().getProjectDir().getAbsolutePath(), "/src/main/resources/db/migration");

        if (!Files.exists(migrationPath)) {
            try {
                Files.createDirectories(migrationPath);
            } catch (IOException e) {
                throw new GradleException("Cannot create migration directory.", e);
            }
        }
    }

    private void createMigrationEnvDirectory() {
        final List<Path> migrationEnvPaths = Arrays.asList(
            Paths.get(getProject().getRootProject().getProjectDir().getAbsolutePath(), "/src/main/resources/db/migration-env/local/.gitkeep"),
            Paths.get(getProject().getRootProject().getProjectDir().getAbsolutePath(), "/src/main/resources/db/migration-env/integration/.gitkeep"),
            Paths.get(getProject().getRootProject().getProjectDir().getAbsolutePath(), "/src/main/resources/db/migration-env/test/.gitkeep"),
            Paths.get(getProject().getRootProject().getProjectDir().getAbsolutePath(), "/src/main/resources/db/migration-env/prod/.gitkeep")
        );

        migrationEnvPaths.forEach(path -> {
            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path.getParent());
                    Files.createFile(path);
                } catch (IOException e) {
                    throw new GradleException("Cannot create migration directory.", e);
                }
            }
        });
    }
}
