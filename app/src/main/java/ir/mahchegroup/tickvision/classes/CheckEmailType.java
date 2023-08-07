package ir.mahchegroup.tickvision.classes;

public class CheckEmailType {
    public static boolean check(String str) {
        boolean isError;
        String[] model = {"@", ".com", ".ir"};

        if (str.length() > 0) {
            if (str.contains(model[0]) && (str.endsWith(model[1]) || str.endsWith(model[2]))) {
                isError = false;
            }else {
                isError = true;
            }
        }else {
            isError = false;
        }
        return isError;
    }
}
