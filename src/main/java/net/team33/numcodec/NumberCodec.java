package net.team33.numcodec;

import java.math.BigInteger;

public class NumberCodec {

    private final String digits;
    private final BigInteger base;

    private NumberCodec(final String digits) {
        this.digits = digits;
        this.base = BigInteger.valueOf(digits.length());
    }

    public static NumberCodec using(final String digits) {
        return new NumberCodec(digits);
    }

    public final String encode(final BigInteger number) {
        final StringBuilder result = new StringBuilder();
        BigInteger tail = number;
        while (!BigInteger.ZERO.equals(tail)) {
            final int digit = tail.mod(base).intValue();
            result.insert(0, digits.charAt(digit));
            tail = tail.divide(base);
        }
        return result.toString();
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
