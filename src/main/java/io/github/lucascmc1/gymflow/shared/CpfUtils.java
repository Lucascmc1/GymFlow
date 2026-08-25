package io.github.lucascmc1.gymflow.shared;

public final class CpfUtils {

    private CpfUtils() {
    }

    public static boolean isValid(String value) {
        String cpf = digitsOnly(value);
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }
        return calculateDigit(cpf.substring(0, 9), 10) == Character.getNumericValue(cpf.charAt(9))
                && calculateDigit(cpf.substring(0, 10), 11)
                == Character.getNumericValue(cpf.charAt(10));
    }

    public static String format(String value) {
        String cpf = digitsOnly(value);
        if (cpf.length() != 11) {
            return value;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    private static String digitsOnly(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static int calculateDigit(String base, int weight) {
        int total = 0;
        for (int index = 0; index < base.length(); index++) {
            total += Character.getNumericValue(base.charAt(index)) * (weight - index);
        }
        int remainder = 11 - total % 11;
        return remainder >= 10 ? 0 : remainder;
    }
}
