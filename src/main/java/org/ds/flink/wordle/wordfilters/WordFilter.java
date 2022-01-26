package org.ds.flink.wordle.wordfilters;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import static org.ds.flink.wordle.wordfilters.WorkFilterFunc.match;

public class WordFilter implements FilterFunction<String> {

    @Override
    public boolean filter(String s) throws Exception {
        return match(s);
    }
}
