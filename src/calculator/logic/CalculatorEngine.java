package calculator.logic;

/**
 * Core calculation engine handling all arithmetic and scientific operations.
 * Separates business logic from UI concerns.
 */
public class CalculatorEngine {

    private double currentValue = 0;
    private double previousValue = 0;
    private String pendingOperator = "";
    private boolean newNumber = true;
    private boolean hasError = false;
    private String lastExpression = "";

    // ─── Basic Arithmetic ─────────────────────────────────────────────────────

    public double add(double a, double b) { return a + b; }
    public double subtract(double a, double b) { return a - b; }
    public double multiply(double a, double b) { return a * b; }
    public double divide(double a, double b) {
        if (b == 0) throw new ArithmeticException("Division by zero");
        return a / b;
    }
    public double modulo(double a, double b) {
        if (b == 0) throw new ArithmeticException("Modulo by zero");
        return a % b;
    }

    // ─── Scientific Functions ─────────────────────────────────────────────────

    public double sin(double x, boolean degrees) {
        double rad = degrees ? Math.toRadians(x) : x;
        return Math.sin(rad);
    }
    public double cos(double x, boolean degrees) {
        double rad = degrees ? Math.toRadians(x) : x;
        return Math.cos(rad);
    }
    public double tan(double x, boolean degrees) {
        double rad = degrees ? Math.toRadians(x) : x;
        double cosTan = Math.cos(rad);
        if (Math.abs(cosTan) < 1e-10) throw new ArithmeticException("Undefined (tan 90°)");
        return Math.tan(rad);
    }
    public double asin(double x, boolean degrees) {
        if (x < -1 || x > 1) throw new ArithmeticException("Domain error");
        double r = Math.asin(x);
        return degrees ? Math.toDegrees(r) : r;
    }
    public double acos(double x, boolean degrees) {
        if (x < -1 || x > 1) throw new ArithmeticException("Domain error");
        double r = Math.acos(x);
        return degrees ? Math.toDegrees(r) : r;
    }
    public double atan(double x, boolean degrees) {
        double r = Math.atan(x);
        return degrees ? Math.toDegrees(r) : r;
    }
    public double log10(double x) {
        if (x <= 0) throw new ArithmeticException("Logarithm domain error");
        return Math.log10(x);
    }
    public double ln(double x) {
        if (x <= 0) throw new ArithmeticException("Logarithm domain error");
        return Math.log(x);
    }
    public double exp(double x)     { return Math.exp(x); }
    public double pow(double a, double b) { return Math.pow(a, b); }
    public double sqrt(double x) {
        if (x < 0) throw new ArithmeticException("√ of negative number");
        return Math.sqrt(x);
    }
    public double cbrt(double x)    { return Math.cbrt(x); }
    public double factorial(double n) {
        if (n < 0 || n != Math.floor(n)) throw new ArithmeticException("Factorial domain error");
        if (n > 170) throw new ArithmeticException("Overflow");
        long result = 1;
        for (int i = 2; i <= (int) n; i++) result *= i;
        return result;
    }
    public double reciprocal(double x) {
        if (x == 0) throw new ArithmeticException("Division by zero");
        return 1.0 / x;
    }
    public double square(double x)  { return x * x; }
    public double cube(double x)    { return x * x * x; }
    public double tenPow(double x)  { return Math.pow(10, x); }
    public double twoPow(double x)  { return Math.pow(2, x); }
    public double absVal(double x)  { return Math.abs(x); }
    public double negate(double x)  { return -x; }

    // ─── Programmer / Bitwise ─────────────────────────────────────────────────

    public long bitwiseAnd(long a, long b)  { return a & b; }
    public long bitwiseOr(long a, long b)   { return a | b; }
    public long bitwiseXor(long a, long b)  { return a ^ b; }
    public long bitwiseNot(long a)          { return ~a; }
    public long shiftLeft(long a, int n)    { return a << n; }
    public long shiftRight(long a, int n)   { return a >> n; }
    public long unsignedShiftRight(long a, int n) { return a >>> n; }

    // ─── State Management ─────────────────────────────────────────────────────

    public double getCurrentValue()      { return currentValue; }
    public void setCurrentValue(double v){ currentValue = v; }
    public double getPreviousValue()     { return previousValue; }
    public void setPreviousValue(double v){ previousValue = v; }
    public String getPendingOperator()   { return pendingOperator; }
    public void setPendingOperator(String op){ pendingOperator = op; }
    public boolean isNewNumber()         { return newNumber; }
    public void setNewNumber(boolean b)  { newNumber = b; }
    public boolean hasError()            { return hasError; }
    public void setError(boolean e)      { hasError = e; }
    public String getLastExpression()    { return lastExpression; }
    public void setLastExpression(String e){ lastExpression = e; }

    public void reset() {
        currentValue = 0;
        previousValue = 0;
        pendingOperator = "";
        newNumber = true;
        hasError = false;
        lastExpression = "";
    }

    /**
     * Evaluate the pending binary operation and return result.
     */
    public double evaluatePending(double secondOperand) {
        switch (pendingOperator) {
            case "+":   return add(previousValue, secondOperand);
            case "-":   return subtract(previousValue, secondOperand);
            case "×":   return multiply(previousValue, secondOperand);
            case "÷":   return divide(previousValue, secondOperand);
            case "%":   return modulo(previousValue, secondOperand);
            case "xʸ":  return pow(previousValue, secondOperand);
            case "ʸ√x": return pow(secondOperand, 1.0 / previousValue);
            case "AND": return bitwiseAnd((long) previousValue, (long) secondOperand);
            case "OR":  return bitwiseOr((long) previousValue, (long) secondOperand);
            case "XOR": return bitwiseXor((long) previousValue, (long) secondOperand);
            case "LSH": return shiftLeft((long) previousValue, (int) secondOperand);
            case "RSH": return shiftRight((long) previousValue, (int) secondOperand);
            default: return secondOperand;
        }
    }

    /**
     * Format a number smartly: no trailing zeros, scientific for huge/tiny values.
     */
    public static String format(double value) {
        if (Double.isNaN(value))      return "Error";
        if (Double.isInfinite(value)) return value > 0 ? "∞" : "-∞";
        if (value == Math.floor(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        String s = String.format("%.10g", value);
        // Remove trailing zeros after decimal
        if (s.contains(".") && !s.contains("e")) {
            s = s.replaceAll("0+$", "").replaceAll("\\.$", "");
        }
        return s;
    }
}
