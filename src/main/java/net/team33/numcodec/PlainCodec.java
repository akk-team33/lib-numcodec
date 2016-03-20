package net.team33.numcodec;

import java.math.BigInteger;

public class PlainCodec {

    private final String digits;
    private final int minLength;
    private final BigInteger base;

    private PlainCodec(final String digits, final int minLength) {
        this.digits = digits;
        this.minLength = minLength;
        this.base = BigInteger.valueOf(digits.length());
    }

    public static PlainCodec using(final String digits) throws NullPointerException {
        return using(digits, 1);
    }

    public static PlainCodec using(final String digits, final int minLength) {
        return new PlainCodec(digits, minLength);
    }

    public final String encode(final BigInteger number) throws NullPointerException {
        return pad(digitize(number, new StringBuilder())).toString();
    }

    private StringBuilder digitize(final BigInteger number, final StringBuilder result) {
        for (BigInteger tail = number; !BigInteger.ZERO.equals(tail); tail = tail.divide(base)) {
            final int digit = tail.mod(base).intValue();
            result.insert(0, digits.charAt(digit));
        }
        return result;
    }

    private StringBuilder pad(final StringBuilder result) {
        while (result.length() < minLength) {
            result.insert(0, digits.charAt(0));
        }
        return result;
    }

    public final BigInteger decode(final String encoded) throws NumberFormatException {
        return decode(encoded, encoded.length());
    }

    private BigInteger decode(final String encoded, final int length) throws NumberFormatException {
        BigInteger result = BigInteger.ZERO;
        for (int index = 0; index < length; ++index) {
            final char digit = encoded.charAt(index);
            final int value = digits.indexOf(digit);
            if (0 > value) {
                throw new NumberFormatException(
                        String.format(
                                "Illegal digit <%s> at index <%d>",
                                digit, index));
            } else {
                result = result.multiply(base).add(BigInteger.valueOf(value));
            }
        }
        return result;
    }
}
