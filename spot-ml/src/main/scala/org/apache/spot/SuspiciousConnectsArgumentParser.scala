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

package org.apache.spot


/**
  * Parses arguments for the suspicious connections analysis.
  */
object SuspiciousConnectsArgumentParser {

  case class SuspiciousConnectsConfig(analysis: String = "",
                                      inputPath: String = "",
                                      feedbackFile: String = "",
                                      duplicationFactor: Int = 1,
                                      topicCount: Int = 20,
                                      userDomain: String = "",
                                      hdfsScoredConnect: String = "",
                                      threshold: Double = 1.0d,
                                      maxResults: Int = -1,
                                      outputDelimiter: String = "\t",
                                      ldaPRGSeed: Option[Long] = None,
                                      ldaMaxiterations: Int = 20,
                                      ldaAlpha: Double = 1.02,
                                      ldaBeta: Double = 1.001,
                                      // flow tuning options
                                      // these are for the flow tuning branch only!

                                      flatBinTime: Boolean = false,
                                      expBinIBytes: Boolean = false,
                                      expBinIPkts: Boolean = false,
                                      useProtocol: Boolean = false)

  val parser: scopt.OptionParser[SuspiciousConnectsConfig] = new scopt.OptionParser[SuspiciousConnectsConfig]("LDA") {

    head("LDA Process", "1.1")

    opt[String]("analysis").required().valueName("< flow | proxy | dns >").
      action((x, c) => c.copy(analysis = x)).
      text("choice of suspicious connections analysis to perform")

    opt[String]("input").required().valueName("<hdfs path>").
      action((x, c) => c.copy(inputPath = x)).
      text("HDFS path to input")

    opt[String]("feedback").valueName("<local file>").
      action((x, c) => c.copy(feedbackFile = x)).
      text("the local path of the file that contains the feedback scores")

    opt[Int]("dupfactor").valueName("<non-negative integer>").
      action((x, c) => c.copy(duplicationFactor = x)).
      text("duplication factor controlling how to downgrade non-threatening connects from the feedback file")


    opt[String]("ldatopiccount").required().valueName("number of topics in topic model").
      action((x, c) => c.copy(topicCount = x.toInt)).
      text("topic count")

    opt[String]("userdomain").valueName("<user domain>").
      action((x, c) => c.copy(userDomain = x)).
      text("Domain of spot user (example: intel)")

    opt[String]("scored").required().valueName("<hdfs path>").
      action((x, c) => c.copy(hdfsScoredConnect = x)).
      text("HDFS path for results")

    opt[Double]("threshold").required().valueName("float64").
      action((x, c) => c.copy(threshold = x)).
      text("probability threshold for declaring anomalies")

    opt[Int]("maxresults").required().valueName("integer").
      action((x, c) => c.copy(maxResults = x)).
      text("number of most suspicious connections to return")

    opt[String]("delimiter").optional().valueName("character").
      action((x, c) => c.copy(outputDelimiter = x)).
      text("number of most suspicious connections to return")

    opt[String]("prgseed").optional().valueName("long").
      action((x, c) => c.copy(ldaPRGSeed = Some(x.toLong))).
      text("seed for the pseudorandom generator")

    opt[Int]("ldamaxiterations").optional().valueName("int").
      action((x, c) => c.copy(ldaMaxiterations = x)).
      text("maximum number of iterations used in LDA")


    opt[Double]("ldaalpha").optional().valueName("float64").
      action((x, c) => c.copy(ldaAlpha = x)).
      text("document concentration for lda, default 1.02")

    opt[Double]("ldabeta").optional().valueName("float64").
      action((x, c) => c.copy(ldaBeta = x)).
      text("topic concentration for lda, default 1.001")


    // these are for the flow tuning branch only!
    opt[Boolean]("hourbins").optional().
      action((x, c) => c.copy(flatBinTime = true)).
      text("use per-hour binning strategy for flow time of day")

    opt[Boolean]("expbinbytes").optional().
      action((x, c) => c.copy(expBinIBytes = true)).
      text("use exponential binning strategy for flow byte counts")

    opt[Boolean]("expbinpkts").optional().
      action((x, c) => c.copy(expBinIPkts = true)).
      text("use exponential binning strategy for flow packet counts")

    opt[Boolean]("protocol").optional().
      action((x, c) => c.copy(useProtocol = true)).
      text("incorporate flow protocol into netflow word creation")

  }
}
