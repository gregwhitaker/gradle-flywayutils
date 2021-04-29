/**
 * Copyright 2021 Greg Whitaker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
                    throw new GradleException("Cannot create migration-env directory.", e);
                }
            }
        });
    }
}
