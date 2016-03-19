package net.team33.numcodec;

import java.math.BigInteger;

public class NumberCodec {

    private final String digits;
    private final int minLength;
    private final BigInteger base;

    private NumberCodec(final String digits, final int minLength) {
        this.digits = digits;
        this.minLength = minLength;
        this.base = BigInteger.valueOf(digits.length());
    }

    public static NumberCodec using(final String digits) {
        return using(digits, 1);
    }

    public static NumberCodec using(final String digits, final int minLength) {
        return new NumberCodec(digits, minLength);
    }

    public final String encode(final BigInteger number) {
        return Encoder.valueOf(BigInteger.ZERO.compareTo(number))
                .encode(number, this)
                .toString();
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

    private enum Encoder {

        POSITIVE {
            @Override
            StringBuilder encode(final BigInteger num, final NumberCodec codec) {
                final StringBuilder result = new StringBuilder();
                BigInteger tail = num;
                while (!BigInteger.ZERO.equals(tail)) {
                    final int digit = tail.mod(codec.base).intValue();
                    result.insert(0, codec.digits.charAt(digit));
                    tail = tail.divide(codec.base);
                }
                while (result.length() < codec.minLength) {
                    result.insert(0, codec.digits.charAt(0));
                }
                return result;
            }
        },
        ZERO {
            @Override
            StringBuilder encode(final BigInteger num, final NumberCodec codec) {
                return POSITIVE.encode(num, codec);
            }
        },
        NEGATIVE {
            @Override
            StringBuilder encode(final BigInteger num, final NumberCodec codec) {
                return POSITIVE.encode(BigInteger.ZERO.subtract(num), codec).insert(0, '-');
            }
        };

        private static Encoder valueOf(final int cmp) {
            return (0 > cmp) ? POSITIVE : ((0 < cmp) ? NEGATIVE : ZERO);
        }

        abstract StringBuilder encode(BigInteger num, NumberCodec codec);
    }
}
