package org.ds.flink.wordle;


import java.util.Map;

public class FiveLetterWordScore {
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



    public static void main(String[] args) throws Exception {

    }
}
