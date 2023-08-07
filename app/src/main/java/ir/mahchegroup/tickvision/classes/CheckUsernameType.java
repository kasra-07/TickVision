package ir.mahchegroup.tickvision.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckUsernameType {
    private static boolean isError = false;
    private static int length;
    private static final String[] english = {"a", "b", "c", "d", "e", "f", "g", "h", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ".", "_", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    private static final List<String> list = new ArrayList<>(Arrays.asList(english));

    public static boolean check(String str) {
        if (!isError) {
            for (int i = 0; i < str.length(); i++) {
                if (!list.contains(String.valueOf(str.charAt(i)))) {
                    isError = true;
                    break;
                }
            }
        } else {
            for (int i = 0; i < str.length(); i++) {
                if (list.contains(String.valueOf(str.charAt(i)))) {
                    length++;
                } else {
                    length = 0;
                }
            }
            if (length >= str.length()) {
                isError = false;
                length = 0;
            }
        }
        return isError;
    }
}