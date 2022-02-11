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

    public static boolean hasAt(String s, char c, int i) {
        return s.charAt(i) == c;
    }

    public static boolean hasButNotAt(String s, char c, int... indices) {
        if(charNotAt(s,c,indices) == false) {
            return false;
        }

        return has(s,c);

    }

    public static boolean match(String s) {
        if(s.length() != 5) {
            return false;
        }

        return
                noDuplicateChars(s) &&
                        hasButNotAt(s,'l',0) &&
                        hasAt(s,'e',3) &&
                        hasAt(s,'r',4) &&
                        doesNotHave(s, 'a','o','s','i','t');
    }
}
