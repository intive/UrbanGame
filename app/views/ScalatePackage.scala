/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 *       http://blstream.github.com/UrbanGame/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package views

import org.fusesource.scalate.{Binding, TemplateSource}
import org.fusesource.scalate.support.TemplatePackage


/**
 * Defines some common imports, attributes and methods across templates in package foo and below
 */
class ScalatePackage extends TemplatePackage {

  	/** Returns the Scala code to add to the top of the generated template method */
	override def header(source: TemplateSource, bindings: List[Binding]) = 
		"""
			-import controllers._

	  	""".stripMargin
}