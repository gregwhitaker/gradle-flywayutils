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

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Plugin that adds helpful utilities for working with Flyway database migrations.
 */
public class FlywayUtilsPlugin implements Plugin<Project> {
    public static final String GROUP_NAME = "Database Migration Utils";

    @Override
    public void apply(Project project) {
        loadModules(project);
        project.afterEvaluate(this::loadDependencies);
    }

    /**
     * Loads the modules containing tasks and configuration for this plugin.
     *
     * @param project gradle project
     */
    private void loadModules(Project project) {
        FlywayUtilsModule.load(project);
    }

    /**
     *
     * @param project gradle project
     */
    private void loadDependencies(Project project) {
        project.getConfigurations().getByName("implementation").getDependencies()
            .add(project.getDependencies().create("org.flywaydb:flyway-core:6.0.8"));
    }
}
