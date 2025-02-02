/**
 * Copyright (C) 2011
 *   Michael Mosmann <michael@mosmann.de>
 *   Martin Jöhren <m.joehren@googlemail.com>
 *
 * with contributions from
 * 	konstantin-ba@github,Archimedes Trajano	(trajano@github)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.embed.mongo.packageresolver;

import de.flapdoodle.embed.mongo.distribution.HasMongotoolsPackage;
import de.flapdoodle.embed.mongo.distribution.MongotoolsVersion;
import de.flapdoodle.embed.process.config.store.DistributionPackage;
import de.flapdoodle.embed.process.config.store.FileSet;
import de.flapdoodle.embed.process.config.store.PackageResolver;
import de.flapdoodle.embed.process.distribution.ArchiveType;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.distribution.Version;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.function.Function;

@Value.Immutable
public abstract class UrlTemplatePackageResolver implements PackageFinder {

  protected abstract ArchiveType archiveType();
  protected abstract FileSet fileSet();
  protected abstract String urlTemplate();

  @Override
  public Optional<DistributionPackage> packageFor(Distribution distribution) {
    String path=render(urlTemplate(), distribution);
    return Optional.of(DistributionPackage.of(archiveType(), fileSet(), path));
  }

  private static String render(String urlTemplate, Distribution distribution) {
    String version=distribution.version().asInDownloadPath();
    String withVersion = urlTemplate.replace("{version}", version);

    Optional<String> toolsVersion = Optional.of(distribution.version())
        .flatMap(it -> it instanceof HasMongotoolsPackage ? Optional.of((HasMongotoolsPackage) it) : Optional.empty())
        .flatMap(HasMongotoolsPackage::mongotoolsVersion)
        .map(Version::asInDownloadPath);

    String withOrWithoutToolsVersion=toolsVersion.isPresent()
        ? withVersion.replace("{tools.version}", toolsVersion.get())
        : withVersion;

    return withOrWithoutToolsVersion;
  }

  public static ImmutableUrlTemplatePackageResolver.Builder builder() {
    return ImmutableUrlTemplatePackageResolver.builder();
  }
}
