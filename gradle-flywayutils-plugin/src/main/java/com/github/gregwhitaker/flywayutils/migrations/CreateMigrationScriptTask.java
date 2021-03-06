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
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Task that creates a new Flyway migration script in the project with standard naming convention.
 */
public class CreateMigrationScriptTask extends DefaultTask {
    private static final List<String> VALID_MIGRATION_TYPES = Arrays.asList("V", "U", "R");

    @Input
    private String type;

    @Input
    private String ver;

    @Input
    private String desc;

    @Optional
    @Input
    private String env;

    public CreateMigrationScriptTask() {
        setGroup(FlywayUtilsPlugin.GROUP_NAME);
        setDescription("Creates a new Flyway migration script with standard naming convention.");
    }

    @TaskAction
    public void run() {
        validateMigrationDirectories();
        validateType(type);
        validateVersion(ver);
        validateDescription(desc);
        validateEnv(env);

        String scriptName = FlywayScriptName.generate(FlywayScriptName.Type.get(type), ver, desc);

        Path scriptPath;
        if (StringUtils.isBlank(env)) {
            scriptPath = Paths.get(getProject().getRootProject().getProjectDir().getAbsolutePath(), "/src/main/resources/db/migration", scriptName);
        } else {
            scriptPath = Paths.get(getProject().getRootProject().getProjectDir().getAbsolutePath(), "/src/main/resources/db/migration-env", env.toLowerCase(), scriptName);
        }

        try {
            if (!Files.exists(scriptPath)) {
                scriptPath.toFile().createNewFile();
            }
        } catch (IOException e) {
            throw new GradleException("Error occurred while creating migration script", e);
        }

        System.out.println("Created migration script: " + scriptPath.toString());
    }

    private void validateMigrationDirectories() {
        final Path migrationPath = Paths.get(getProject().getRootProject().getProjectDir().getAbsolutePath(), "/src/main/resources/db/migration");
        final Path migrationEnvPath = Paths.get(getProject().getRootProject().getProjectDir().getAbsolutePath(), "/src/main/resources/db/migration-env");

        if (!Files.exists(migrationPath)) {
            try {
                Files.createDirectories(migrationPath);
            } catch (IOException e) {
                throw new GradleException("Cannot create migration directory.", e);
            }
        }

        if (!Files.exists(migrationEnvPath)) {
            try {
                Files.createDirectories(migrationEnvPath);
            } catch (IOException e) {
                throw new GradleException("Cannot create migration-env directory.", e);
            }
        }
    }

    private void validateType(String type) {
        // Required argument
        if (StringUtils.isBlank(type)) {
            throw new GradleException("Parameter 'type' is required.");
        }

        // Value must be a supported migration type
        if (!VALID_MIGRATION_TYPES.contains(type.toUpperCase())) {
            throw new GradleException("Invalid 'type' parameter value. Must be either 'V', 'U', or 'R'.");
        }
    }

    private void validateVersion(String version) {
        // Required argument
        if (StringUtils.isBlank(version)) {
            throw new GradleException("Parameter 'ver' is required.");
        }
    }

    private void validateDescription(String description) {
        // Required argument
        if (StringUtils.isBlank(description)) {
            throw new GradleException("Parameter 'desc' is required.");
        }
    }

    private void validateEnv(String env) {
        // Optional argument that when supplied must be a valid environment name in the `migration-env` directory
        if (StringUtils.isNotBlank(env)) {
            Path migrationEnvPath = Paths.get(getProject().getRootProject().getProjectDir().getAbsolutePath(), "/src/main/resources/db/migration-env");
            File[] envDirs = migrationEnvPath.toFile().listFiles(File::isDirectory);

            if (envDirs != null) {
                if (Arrays.stream(envDirs).noneMatch(file -> file.getName().equalsIgnoreCase(env))) {
                    throw new GradleException(String.format("No environment directory for environment '%s' exists in the 'migration-env' directory.", env));
                }
            } else {
                throw new GradleException("No environment directory exist in 'migration-env' directory.");
            }
        }
    }

    public String getType() {
        return type;
    }

    @Option(option = "type", description = "Migration type (V,R,U)")
    public void setType(String type) {
        this.type = type;
    }

    public String getVer() {
        return ver;
    }

    @Option(option = "ver", description = "Migration version")
    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getDesc() {
        return desc;
    }

    @Option(option = "desc", description = "Migration description")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEnv() {
        return env;
    }

    @Option(option = "env", description = "Migration environment (defaults to all environments)")
    public void setEnv(String env) {
        this.env = env;
    }
}
