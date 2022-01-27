package org.ds.flink.wordle;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.util.Collector;
import org.ds.flink.wordle.wordfilters.WordFilter2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Word2Match {
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

    public static boolean isVowel(char c) {
        String vowels = "aeiou"; //ascii only, lower case
        return vowels.indexOf(c) >= 0;
    }

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        final ParameterTool params = ParameterTool.fromArgs(args);
        if(!params.has("wordfile")) {
            System.err.println("wordfile arg not specified");
            System.exit(1);
        }

        DataSet<String> text = env.readTextFile(params.get("wordfile"));

        DataSet<Tuple3<String,Integer,Integer>> counts = text
                .map(s-> {
                    String[] parts = s.split(" ");
                    return Tuple2.of(parts[0], parts[2]);
                })
                .returns(Types.TUPLE(Types.STRING,Types.STRING))
                .filter(new WordFilter2())
                .flatMap(new FlatMapFunction<Tuple2<String, String>, Tuple3<String,String,Integer>>() {
                    @Override
                    public void flatMap(Tuple2<String, String> t2, Collector<Tuple3<String, String, Integer>> collector) throws Exception {
                        try {
                            String s = t2.f0;
                            int score = 0;
                            List<Character> previous = new ArrayList<>();
                            for (int i = 0; i < s.length(); i++) {
                                char c = s.charAt(i);
                                int letterScore = charWeights.get(c);

                                if(!previous.contains(c)) {
                                    score += letterScore;
                                    if(isVowel(c)) {
                                        score += 2 * letterScore;
                                    }
                                }
                                previous.add(c);
                            }

                            collector.collect(Tuple3.of(t2.f0, t2.f1, score));
                        } catch(Throwable t) {
                            System.err.println("EXCEPTION PROCESSING " + t2);
                        }
                    }
                })
                .map(t3 -> Tuple3.of(t3.f0, Integer.parseInt(t3.f1), t3.f2))
                .returns(Types.TUPLE(Types.STRING, Types.INT,Types.INT));

        DataSet<Tuple3<String,Integer,Integer>> sorted = counts.sortPartition(1, Order.ASCENDING).setParallelism(1);
        sorted.print();


    }
}
