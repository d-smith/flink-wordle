package org.ds.flink.wordle;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class WordFilter2 implements FilterFunction<Tuple2<String,String>> {
    @Override
    public boolean filter(Tuple2<String, String> t2) throws Exception {
        return true;
    }
}
