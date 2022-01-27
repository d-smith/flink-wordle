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

    private static boolean noDuplicateChars(String s) {
        for(int i=0; i< s.length();i++) {
            char c = s.charAt(i);
            if (s.indexOf(c) != s.lastIndexOf(c)) return false;
        }
        return true;
    }

    public static boolean match(String s) {
        if(s.length() != 5) {
            return false;
        }

        return //noDuplicateChars(s);
                s.charAt(2) == 'a' &&
                        s.charAt(3) == 'c' &&
                        s.charAt(4) == 'k' &&

                        doesNotHave(s,'y','e','r','s','p','i','n','o','b','l');
    }
}
