package calculator.util;

/**
 * Utility class for number-base conversions used in Programmer Mode.
 */
public class BaseConverter {

    public enum Base { BIN, OCT, DEC, HEX }

    public static String toBase(long value, Base base) {
        switch (base) {
            case BIN: return Long.toBinaryString(value);
            case OCT: return Long.toOctalString(value);
            case DEC: return Long.toString(value);
            case HEX: return Long.toHexString(value).toUpperCase();
            default:  return Long.toString(value);
        }
    }

    public static long fromBase(String text, Base base) {
        if (text == null || text.isBlank()) return 0;
        text = text.trim().toUpperCase();
        switch (base) {
            case BIN: return Long.parseLong(text, 2);
            case OCT: return Long.parseLong(text, 8);
            case DEC: return Long.parseLong(text, 10);
            case HEX: return Long.parseLong(text, 16);
            default:  return Long.parseLong(text, 10);
        }
    }

    public static boolean isValidInBase(String text, Base base) {
        if (text == null || text.isBlank()) return false;
        try {
            fromBase(text, base);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns only the characters valid for the given base.
     */
    public static String validChars(Base base) {
        switch (base) {
            case BIN: return "01";
            case OCT: return "01234567";
            case DEC: return "0123456789";
            case HEX: return "0123456789ABCDEF";
            default:  return "0123456789";
        }
    }

    /**
     * Format a long value into a 64-bit binary string with spaces every 4 bits.
     */
    public static String toBinaryGrouped(long value) {
        String bin = String.format("%64s", Long.toBinaryString(value)).replace(' ', '0');
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bin.length(); i++) {
            if (i > 0 && i % 4 == 0) sb.append(' ');
            sb.append(bin.charAt(i));
        }
        return sb.toString();
    }

    /**
     * All four representations of a value.
     */
    public static String[] allRepresentations(long value) {
        return new String[]{
            Long.toBinaryString(value),
            Long.toOctalString(value),
            Long.toString(value),
            Long.toHexString(value).toUpperCase()
        };
    }
}
