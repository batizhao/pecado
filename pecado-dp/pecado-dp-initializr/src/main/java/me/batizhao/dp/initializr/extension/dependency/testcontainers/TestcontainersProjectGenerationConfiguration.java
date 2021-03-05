/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.batizhao.dp.initializr.extension.dependency.testcontainers;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import me.batizhao.dp.initializr.support.implicit.ImplicitDependency;
import me.batizhao.dp.initializr.support.implicit.ImplicitDependencyBuildCustomizer;
import me.batizhao.dp.initializr.support.implicit.ImplicitDependencyHelpDocumentCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Testcontainers.
 *
 * @author Maciej Walkowiak
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
public class TestcontainersProjectGenerationConfiguration {

	private final Iterable<ImplicitDependency> dependencies;

	public TestcontainersProjectGenerationConfiguration() {
		this.dependencies = TestcontainersModuleRegistry.create();
	}

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	public ImplicitDependencyBuildCustomizer testContainersBuildCustomizer() {
		return new ImplicitDependencyBuildCustomizer(this.dependencies);
	}

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	public ImplicitDependencyHelpDocumentCustomizer testcontainersHelpCustomizer(Build build) {
		return new ImplicitDependencyHelpDocumentCustomizer(this.dependencies, build);
	}

}
