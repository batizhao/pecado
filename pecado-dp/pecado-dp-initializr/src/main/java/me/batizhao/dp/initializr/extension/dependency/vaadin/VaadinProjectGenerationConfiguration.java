/*
 * Copyright 2012-2021 the original author or authors.
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

package me.batizhao.dp.initializr.extension.dependency.vaadin;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.scm.git.GitIgnoreCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Vaadin.
 *
 * @author Stephane Nicoll
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("vaadin")
class VaadinProjectGenerationConfiguration {

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	VaadinMavenBuildCustomizer vaadinMavenBuildCustomizer() {
		return new VaadinMavenBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	BuildCustomizer<GradleBuild> vaadinGradleBuildCustomizer() {
		return (build) -> build.plugins().add("com.vaadin", (plugin) -> plugin.setVersion("0.14.3.7"));
	}

	@Bean
	GitIgnoreCustomizer vaadinGitIgnoreCustomizer() {
		return (gitignore) -> gitignore.getGeneral().add("node_modules");
	}

}
