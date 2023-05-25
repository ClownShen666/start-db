/* 
 * Copyright (C) 2022  ST-Lab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.urbcomp.cupid.db.udf.otherfunction

import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

class versionUdf extends AbstractUdf {

  override def name(): String = "version"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def evaluate(): String = {
    //    import java.util.jar.JarFile
    //    import scala.xml.XML
    //
    //    // 打包后的 JAR 文件路径
    //    val jarPath = "./"
    //
    //    // 创建 JarFile 对象
    //    val jarFile = new JarFile(jarPath)
    //
    //    // 获取 pom.xml 文件的输入流
    //    val pomEntry = jarFile.getEntry("META-INF/maven/your-group/your-artifact/pom.xml")
    //    val inputStream = jarFile.getInputStream(pomEntry)
    //
    //    // 解析 pom.xml 文件
    //    val pom = XML.load(inputStream)
    //
    //    // 获取 modelVersion 属性
    //    val modelVersion = (pom \ "modelVersion").text
    //    // 关闭 JarFile 和输入流
    //    jarFile.close()
    //    inputStream.close()
    //
    //    // 打印 modelVersion
    //    println(s"modelVersion: $modelVersion")
    //
    //    modelVersion
    //    "true"
    getClass.getPackage.getImplementationVersion

  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: () => String = evaluate
}
