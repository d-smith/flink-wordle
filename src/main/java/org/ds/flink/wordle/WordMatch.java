package org.ds.flink.wordle;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.util.Collector;
import org.ds.flink.wordle.wordfilters.WordFilter;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordMatch {
    private static Map<Character, Integer> charWeights=
            Map.ofEntries(
                Map.entry('e',6892),
                Map.entry('a',6758),
                Map.entry('s',6126),
                Map.entry('o',4871),
                Map.entry('r',4430),
                Map.entry('i',4349),
                Map.entry('t',3897),
                Map.entry('l',3679),
                Map.entry('n',3381),
                Map.entry('u',2937),
                Map.entry('d',2494),
                Map.entry('c',2404),
                Map.entry('y',2224),
                Map.entry('p',2217),
                Map.entry('m',2105),
                Map.entry('h',2040),
                Map.entry('g',1740),
                Map.entry('b',1731),
                Map.entry('k',1478),
                Map.entry('f',1162),
                Map.entry('w',1105),
                Map.entry('v',773),
                Map.entry('z',411),
                Map.entry('x',333),
                Map.entry('j',286),
                Map.entry('q',128)
            );


    private static boolean doesNotHave(String s, char... chars) {
        for(char c: chars) {
            if(s.indexOf(c) != -1) {
                return false;
            }
        }
        return true;
    }

    private static boolean has(String s, char c) {
        return s.indexOf(c) >= 0;
    }

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        final ParameterTool params = ParameterTool.fromArgs(args);
        if(!params.has("wordfile")) {
            System.err.println("wordfile arg not specified");
            System.exit(1);
        }

        DataSet<String> text = env.readTextFile(params.get("wordfile"));

        DataSet<Tuple2<String,Integer>> counts = text
                .map(s -> s.replaceAll("\\p{Punct}", ""))
                .filter(s -> s.length() == 5)
                .filter(s -> Character.isLowerCase(s.charAt(0)))
                .filter(new WordFilter())
                .flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
                    @Override
                    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {

                        try {

                            int score = 0;
                            for (int i = 0; i < s.length(); i++) {
                                int letterScore = charWeights.get(s.charAt(i));
                                score += letterScore;
                            }

                            collector.collect(Tuple2.of(s, score));
                        } catch(Throwable t) {
                            System.err.println("EXCEPTION PROCESSING " + s);
                        }
                    }
                })
                .groupBy(0)
                .aggregate(Aggregations.SUM,1);

        DataSet<Tuple2<String,Integer>> sorted = counts.sortPartition(1, Order.ASCENDING).setParallelism(1);
        sorted.print();
    }
}
