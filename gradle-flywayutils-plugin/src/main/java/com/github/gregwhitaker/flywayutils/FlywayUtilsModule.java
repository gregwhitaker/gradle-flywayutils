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
package com.github.gregwhitaker.flywayutils;

import com.github.gregwhitaker.flywayutils.migrations.CreateMigrationClassTask;
import com.github.gregwhitaker.flywayutils.migrations.CreateMigrationScriptTask;
import com.github.gregwhitaker.flywayutils.migrations.InitMigrationDirectoryTask;
import org.gradle.api.Project;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads tasks and configuration for FlywayUtilsPlugin.
 */
public class FlywayUtilsModule {
    // Task Names
    public static final String CREATE_MIGRATION_SCRIPT_TASK_NAME = "createMigrationScript";
    public static final String CREATE_MIGRATION_CLASS_TASK_NAME = "createMigrationClass";
    public static final String INIT_MIGRATION_DIRECTORY_TASK_NAME = "initMigrationDirectory";

    /**
     * Loads and configures tasks for the plugin.
     *
     * @param project gradle project
     */
    public static void load(Project project) {
        final Map<String, Class> tasks = new HashMap<>();
        tasks.put(CREATE_MIGRATION_SCRIPT_TASK_NAME, CreateMigrationScriptTask.class);
        tasks.put(CREATE_MIGRATION_CLASS_TASK_NAME, CreateMigrationClassTask.class);
        tasks.put(INIT_MIGRATION_DIRECTORY_TASK_NAME, InitMigrationDirectoryTask.class);

        tasks.forEach((name, clazz) -> {
            // Register the default tasks with the project
            project.getTasks().create(name, clazz);
        });
    }
}
