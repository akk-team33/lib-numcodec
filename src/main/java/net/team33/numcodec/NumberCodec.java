package net.team33.numcodec;

import java.math.BigInteger;

public class NumberCodec {

    private final String minus = "-";
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
        return Encoder.valueOf(number)
                .encode(number, this);
    }

    public final BigInteger decode(final String encoded) throws NumberFormatException {
        return Decoder.valueOf(encoded, this).decode(encoded, this);
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
                return codec.minus + POSITIVE.encode(BigInteger.ZERO.subtract(num), codec);
            }
        };

        private static Encoder valueOf(final BigInteger number) {
            return (0 < BigInteger.ZERO.compareTo(number)) ? NEGATIVE : POSITIVE;
        }

        abstract String encode(BigInteger num, NumberCodec codec);
    }

    private enum Decoder {

        POSITIVE {
            @Override
            BigInteger decode(final String encoded, final NumberCodec codec) {
                return codec.plain.decode(encoded);
            }
        },
        NEGATIVE {
            @Override
            BigInteger decode(final String encoded, final NumberCodec codec) {
                return BigInteger.ZERO.subtract(POSITIVE.decode(encoded.substring(codec.minus.length()), codec));
            }
        };

        private static Decoder valueOf(final String encoded, final NumberCodec codec) {
            return encoded.startsWith(codec.minus) ? NEGATIVE : POSITIVE;
        }

        abstract BigInteger decode(String encoded, NumberCodec codec);
    }
}
