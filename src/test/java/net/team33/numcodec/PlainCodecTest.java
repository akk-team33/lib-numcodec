package net.team33.numcodec;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class PlainCodecTest {

    private static final PlainCodec SUBJECT2 = PlainCodec.using("01");
    private static final PlainCodec SUBJECT10 = PlainCodec.using("0123456789");
    private static final PlainCodec SUBJECT16 = PlainCodec.using("0123456789abcdef");

    @Test(expected = NullPointerException.class)
    public final void usingNull() {
        PlainCodec.using(null);
    }

    @Test(expected = NullPointerException.class)
    public final void usingNull1() {
        PlainCodec.using(null, 1);
    }

    @Test(expected = NullPointerException.class)
    public final void encodeNull() {
        SUBJECT10.encode(null);
    }

    @Test
    public final void encode2() {
        final BigInteger sample = newBigInteger();
        assertEquals(sample.toString(2), SUBJECT2.encode(sample));
    }

    @Test
    public final void encode10() {
        final BigInteger sample = newBigInteger();
        assertEquals(sample.toString(), SUBJECT10.encode(sample));
    }

    @Test
    public final void encode10Zero() {
        final BigInteger sample = BigInteger.ZERO;
        assertEquals(sample.toString(), SUBJECT10.encode(sample));
    }

    @Test
    public final void encode16() {
        final BigInteger sample = newBigInteger();
        assertEquals(sample.toString(16), SUBJECT16.encode(sample));
    }

    @Test(expected = NumberFormatException.class)
    public final void decodeFail() {
        SUBJECT10.decode("123xy");
    }

    @Test
    public final void decode2() {
        final BigInteger origin = newBigInteger();
        final String sample = SUBJECT2.encode(origin);
        assertEquals(origin, SUBJECT2.decode(sample));
    }

    @Test
    public final void decode10() {
        final BigInteger origin = newBigInteger();
        final String sample = SUBJECT10.encode(origin);
        assertEquals(origin, SUBJECT10.decode(sample));
    }

    @Test
    public final void decode16() {
        final BigInteger origin = newBigInteger();
        final String sample = SUBJECT16.encode(origin);
        assertEquals(origin, SUBJECT16.decode(sample));
    }

    private BigInteger newBigInteger() {
        return new BigInteger(128, new Random());
    }
}