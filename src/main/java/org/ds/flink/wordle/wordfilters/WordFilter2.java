package org.ds.flink.wordle.wordfilters;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import static org.ds.flink.wordle.wordfilters.WorkFilterFunc.match;

public class WordFilter2 implements FilterFunction<Tuple2<String,String>> {
    @Override
    public boolean filter(Tuple2<String, String> t2) throws Exception {
        return match(t2.f0);
    }
}
