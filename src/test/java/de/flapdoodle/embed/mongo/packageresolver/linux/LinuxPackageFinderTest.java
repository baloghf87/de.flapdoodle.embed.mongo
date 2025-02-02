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
package de.flapdoodle.embed.mongo.packageresolver.linux;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.packageresolver.HtmlParserResultTester;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.distribution.Version;
import de.flapdoodle.os.CommonArchitecture;
import de.flapdoodle.os.ImmutablePlatform;
import de.flapdoodle.os.OS;
import de.flapdoodle.os.Platform;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LinuxPackageFinderTest {

  /*
    Linux (legacy) undefined
    https://fastdl.mongodb.org/linux/mongodb-linux-i686-{}.tgz
    3.2.21 - 3.2.0, 3.0.14 - 3.0.0, 2.6.12 - 2.6.0
  */
  @ParameterizedTest
  @ValueSource(strings = {"3.2.21 - 3.2.0", "3.0.14 - 3.0.0", "2.6.12 - 2.6.0"})
  public void legacy32Bit(String version) {
    assertThat(linuxWith(CommonArchitecture.X86_32), version)
            .resolvesTo("/linux/mongodb-linux-i686-{}.tgz");
  }

  /*
    Linux (legacy) x64
    https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-{}.tgz
    4.0.26 - 4.0.0, 3.6.22 - 3.6.0, 3.4.23 - 3.4.9, 3.4.7 - 3.4.0, 3.2.21 - 3.2.0, 3.0.14 - 3.0.0, 2.6.12 - 2.6.0
  */
  @ParameterizedTest
  @ValueSource(strings = {"4.0.26 - 4.0.0", "3.6.22 - 3.6.0", "3.4.23 - 3.4.9", "3.4.7 - 3.4.0", "3.2.21 - 3.2.0", "3.0.14 - 3.0.0", "2.6.12 - 2.6.0"})
  public void  legacy64Bit(String version) {
    assertThat(linuxWith(CommonArchitecture.X86_64), version)
            .resolvesTo("/linux/mongodb-linux-x86_64-{}.tgz");
  }


  private static Platform linuxWith(CommonArchitecture architecture) {
    return ImmutablePlatform.builder()
            .operatingSystem(OS.Linux)
            .architecture(architecture)
            .build();
  }

  private static HtmlParserResultTester assertThat(Platform platform, String versionList) {
    return HtmlParserResultTester.with(
            new LinuxPackageFinder(Command.Mongo),
            version -> Distribution.of(Version.of(version), platform),
            versionList);
  }

}