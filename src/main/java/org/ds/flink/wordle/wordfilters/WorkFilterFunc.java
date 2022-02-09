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

    private static boolean charNotAt(String s, char c, int... indices) {
        for(int i: indices) {
            if(s.charAt(i) == c) return false;
        }
        return true;
    }

    public static boolean match(String s) {
        if(s.length() != 5) {
            return false;
        }

        return
                noDuplicateChars(s) &&
                        charNotAt(s,'r',1,3,0) &&
                        charNotAt(s,'o',2,4,1) &&
                        doesNotHave(s, 'a','s','e','i','n','t','w','d','y');
    }
}
