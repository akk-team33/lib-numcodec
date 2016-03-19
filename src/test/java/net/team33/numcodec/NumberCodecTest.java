package net.team33.numcodec;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class NumberCodecTest {

    private static final NumberCodec SUBJECT2 = NumberCodec.using("01");
    private static final NumberCodec SUBJECT10 = NumberCodec.using("0123456789");
    private static final NumberCodec SUBJECT16 = NumberCodec.using("0123456789abcdef");

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
    public final void encode10negative() {
        final BigInteger sample = BigInteger.ZERO.subtract(newBigInteger());
        assertEquals(sample.toString(), SUBJECT10.encode(sample));
    }

    @Test
    public final void encode16() {
        final BigInteger sample = newBigInteger();
        assertEquals(sample.toString(16), SUBJECT16.encode(sample));
    }

    @Test
    public final void encode16negative() {
        final BigInteger sample = BigInteger.ZERO.subtract(newBigInteger());
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