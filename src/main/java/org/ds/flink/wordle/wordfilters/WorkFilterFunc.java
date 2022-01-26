package org.ds.flink.wordle.wordfilters;

public class WorkFilterFunc {
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

    public static boolean match(String s) {
        if(s.length() != 5) {
            return false;
        }

        return
                s.charAt(4) == 'r' &&
                        s.charAt(0) == 's' &&
                        s.charAt(3) == 'a';// &&
                        //doesNotHave(s,'i','t','e','n','o','l');
    }
}
