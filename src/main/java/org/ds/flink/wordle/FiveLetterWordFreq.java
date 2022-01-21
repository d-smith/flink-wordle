/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ds.flink.wordle;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.util.Collector;


public class FiveLetterWordFreq {

	public static void main(String[] args) throws Exception {
		// set up the batch execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		final ParameterTool params = ParameterTool.fromArgs(args);
		if(!params.has("wordfile")) {
			System.err.println("wordfile arg not specified");
			System.exit(1);
		}

		DataSet<String> text = env.readTextFile(params.get("wordfile"));

		DataSet<Tuple2<Character,Integer>> counts =
				text
						.map(s -> s.replaceAll("\\p{Punct}", ""))
				.filter(s -> s.length() == 5)
				.filter(s -> Character.isLowerCase(s.charAt(0)))
				.flatMap((String s, Collector<Tuple2<Character,Integer>> collector) ->{
						for(int i=0;i<s.length();i++) {
							collector.collect(Tuple2.of(s.charAt(i),1));
						}
					}
				).returns(Types.TUPLE(Types.CHAR,Types.INT))
				.groupBy(0)
				.aggregate(Aggregations.SUM,1);

		DataSet<Tuple2<Character,Integer>> sorted = counts.sortPartition(1, Order.DESCENDING).setParallelism(1);
 		sorted.print();



		// execute program
		env.execute("Flink Batch Java API Skeleton");
	}
}
