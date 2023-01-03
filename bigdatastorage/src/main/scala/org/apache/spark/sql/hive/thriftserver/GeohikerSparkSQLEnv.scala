/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.hive.thriftserver

import java.io.PrintStream
import java.nio.charset.StandardCharsets.UTF_8
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{GeohikerSqlSupport, SparkSession}
import org.apache.spark.sql.hive.{HiveContext, HiveExternalCatalog, HiveUtils}
import org.apache.spark.util.Utils

/** A singleton object for the master program. The slaves should not access this. */
private[hive] object GeohikerSparkSQLEnv extends Logging {
  logDebug("Initializing GeohikerSparkSQLEnv")

  var hiveContext: HiveContext = _
  var sparkContext: SparkContext = _

  def init() {
    if (hiveContext == null) {
      val sparkConf = new SparkConf(loadDefaults = true)
      // If user doesn't specify the appName, we want to get [SparkSQL::localHostName] instead of
      // the default appName [GeohikerSparkSQLCLIDriver] in cli or beeline.
      val maybeAppName = sparkConf
        .getOption("spark.app.name")
        .filterNot(_ == classOf[GeohikerSparkSQLCLIDriver].getName)
        .filterNot(_ == classOf[GeohikerThriftServer].getName)

      sparkConf
        .setAppName(maybeAppName.getOrElse(s"SparkSQL::${Utils.localHostName()}"))
        .set("spark.jars.packages", "io.delta:delta-core_2.12:0.8.0")
        .set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
        .set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")

      val sc = SparkContext.getOrCreate(sparkConf)
      hiveContext = new HiveContext(sc)
      val sparkSession = hiveContext.sparkSession
      sparkContext = sparkSession.sparkContext


      val metadataHive = sparkSession
        .sharedState.externalCatalog.unwrapped.asInstanceOf[HiveExternalCatalog].client
      metadataHive.setOut(new PrintStream(System.out, true, UTF_8.name()))
      metadataHive.setInfo(new PrintStream(System.err, true, UTF_8.name()))
      metadataHive.setError(new PrintStream(System.err, true, UTF_8.name()))
      sparkSession.conf.set(HiveUtils.FAKE_HIVE_VERSION.key, HiveUtils.builtinHiveVersion)
    }

    GeohikerSqlSupport.init(hiveContext)
  }

  /** Cleans up and shuts down the Spark SQL environments. */
  def stop() {
    logDebug("Shutting down Spark SQL Environment")
    // Stop the SparkContext
    if (GeohikerSparkSQLEnv.sparkContext != null) {
      sparkContext.stop()
      sparkContext = null
      hiveContext = null
    }
  }
}
