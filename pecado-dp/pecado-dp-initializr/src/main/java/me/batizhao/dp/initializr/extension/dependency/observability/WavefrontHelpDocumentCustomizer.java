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

package me.batizhao.dp.initializr.extension.dependency.observability;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * A {@link HelpDocumentCustomizer} that provides additional references when Wavefront is
 * selected.
 *
 * @author Stephane Nicoll
 */
class WavefrontHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final VersionRange SPRING_BOOT_2_3_0_RC1_OR_LATER = VersionParser.DEFAULT.parseRange("2.3.0.RC1");

	private final boolean distributedTracingAvailable;

	private final Build build;

	WavefrontHelpDocumentCustomizer(Version platformVersion, Build build) {
		this.distributedTracingAvailable = SPRING_BOOT_2_3_0_RC1_OR_LATER.match(platformVersion);
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		if (!this.distributedTracingAvailable) {
			document.getWarnings().addItem("Distributed tracing with Wavefront requires Spring Boot 2.3 or later.");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("## Observability with Wavefront%n%n"));
		sb.append(String.format(
				"If you don't have a Wavefront account, the starter will create a freemium account for you.%n"));
		sb.append(String.format("The URL to access the Wavefront Service dashboard is logged on startup.%n"));

		if (this.build.dependencies().has("web") || this.build.dependencies().has("webflux")) {
			sb.append(
					String.format("%nYou can also access your dashboard using the `/actuator/wavefront` endpoint.%n"));
		}

		if (this.distributedTracingAvailable && !this.build.dependencies().has("cloud-starter-sleuth")) {
			sb.append(String.format(
					"%nFinally, you can opt-in for distributed tracing by adding the Spring Cloud Sleuth starter.%n"));
		}
		document.addSection((writer) -> writer.print(sb.toString()));
	}

}
