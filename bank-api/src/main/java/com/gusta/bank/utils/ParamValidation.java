package com.gusta.bank.utils;

public class ParamValidation {

    // method created to shorten "if...throw new ...Exception" in code
    public static final <V> void checkIfIsNullOrBlankThrowingEx(V... variable) {
        for (V v : variable) {
            if (v == null) {
                throw new IllegalArgumentException("Please put a valid value");
            }
            if (v.toString().isBlank()) {
                throw new IllegalArgumentException("Please put a valid value");
            }
        }
    }
    // end

    // method created to shorten "variable == null" or "variable.toString().isBlank()" conditions in code
    public static final <V> boolean checkIfIsNullOrBlank(V... variable) {
        for (V v : variable) {
            if (v == null) {
                return true;
            }
            if (v.toString().isBlank()) {
                return true;
            }
        }
        return false;
    }
    // end

}
