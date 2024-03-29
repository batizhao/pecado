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

package me.batizhao.dp.initializr.extension.dependency.springcloud;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.ProjectDescriptionDiff;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

import java.util.Map;

/**
 * A {@link HelpDocumentCustomizer} that adds a warning if Spring Cloud Gateway is used
 * with Spring MVC.
 *
 * @author Stephane Nicoll
 */
public class SpringCloudGatewayHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final ProjectDescriptionDiff diff;

	public SpringCloudGatewayHelpDocumentCustomizer(ProjectDescriptionDiff diff) {
		this.diff = diff;
	}

	@Override
	public void customize(HelpDocument document) {
		Map<String, Dependency> originalDependencies = this.diff.getOriginal().getRequestedDependencies();
		if (originalDependencies.containsKey("cloud-gateway") && originalDependencies.containsKey("web")) {
			document.getWarnings().addItem(
					"Spring Cloud Gateway requires Spring WebFlux, your choice of Spring Web has been replaced accordingly.");
		}
	}

}
