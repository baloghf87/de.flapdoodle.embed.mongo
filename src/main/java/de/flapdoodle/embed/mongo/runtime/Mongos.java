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
package de.flapdoodle.embed.mongo.runtime;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.flapdoodle.embed.mongo.config.IMongosConfig;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;

/**
 *
 */
public class Mongos extends AbstractMongo {

	public static List<String> getCommandLine(IMongosConfig config, IExtractedFileSet files)
			throws UnknownHostException {
		List<String> ret = new ArrayList<String>();

		ret.addAll(Arrays.asList(files.executable().getAbsolutePath(), 
				"--chunkSize", "1"));
		if (config.cmdOptions().isVerbose()) {
			ret.add("-v");
		}
		applyDefaultOptions(config, ret);
		applyNet(config.net(),ret);
		
		if (config.getConfigDB()!=null) {
			ret.add("--configdb");
			ret.add(config.getConfigDB());
		}

		if (config.args() != null && !config.args().isEmpty()) {
			for (String key : config.args().keySet()) {
				ret.add(key);
				String val = config.args().get(key);
				if (val != null && !val.isEmpty()) {
					ret.add(val);
				}
			}
		}

		return ret;
	}
}
