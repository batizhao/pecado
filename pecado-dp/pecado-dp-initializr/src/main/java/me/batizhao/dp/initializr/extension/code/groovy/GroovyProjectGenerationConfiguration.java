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

package me.batizhao.dp.initializr.extension.code.groovy;

import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.language.groovy.GroovyLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that use the Groovy
 * language.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
@ConditionalOnLanguage(GroovyLanguage.ID)
class GroovyProjectGenerationConfiguration {

	@Bean
	BuildCustomizer<?> groovy3CompatibilityBuildCustomizer(ProjectDescription description) {
		return new Groovy3CompatibilityBuildCustomer(description.getLanguage().jvmVersion());
	}

}
