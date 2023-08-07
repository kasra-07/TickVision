package ir.mahchegroup.tickvision.classes;

public class CheckEmailType {
    public static boolean check(String str) {
        String[] model = {"@", ".com", ".ir"};
        return  !str.contains(model[0]) || (!str.endsWith(model[1]) && !str.endsWith(model[2]));
    }
}
