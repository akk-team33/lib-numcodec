package net.team33.numcodec;

import java.math.BigInteger;

public class NumberCodec {

    private final PlainCodec plain;

    private NumberCodec(final String digits, final int minLength) {
        this.plain = PlainCodec.using(digits, minLength);
    }

    public static NumberCodec using(final String digits) {
        return using(digits, 1);
    }

    public static NumberCodec using(final String digits, final int minLength) {
        return new NumberCodec(digits, minLength);
    }

    public final String encode(final BigInteger number) {
        return Encoder.valueOf(BigInteger.ZERO.compareTo(number))
                .encode(number, this);
    }

    public final BigInteger decode(final String encoded) throws NumberFormatException {
        return plain.decode(encoded);
    }

    private enum Encoder {

        POSITIVE {
            @Override
            String encode(final BigInteger num, final NumberCodec codec) {
                return codec.plain.encode(num);
            }
        },
        NEGATIVE {
            @Override
            String encode(final BigInteger num, final NumberCodec codec) {
                return "-" + POSITIVE.encode(BigInteger.ZERO.subtract(num), codec);
            }
        };

        private static Encoder valueOf(final int cmp) {
            return (0 < cmp) ? NEGATIVE : POSITIVE;
        }

        abstract String encode(BigInteger num, NumberCodec codec);
    }
}
