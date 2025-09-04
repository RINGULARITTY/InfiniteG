package fr.ringularity.infiniteg.format;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public final class BigIntegerFormat {
    private BigIntegerFormat() {}

    private static final String[] SUFFIX = { "K", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };

    public static String format(BigInteger n) {
        return format_n(n, 3);
    }

    public static String format_n(BigInteger n, int significantDigits) {
        if (significantDigits < 1) {
            throw new IllegalArgumentException("significantDigits must be >= 1");
        }

        boolean neg = n.signum() < 0;
        BigInteger abs = n.abs();

        if (abs.compareTo(BigInteger.valueOf(1000)) < 0) {
            return (neg ? "-" : "") + abs;
        }

        int digits = abs.toString().length();
        int group = (digits - 1) / 3;

        BigDecimal val = new BigDecimal(abs)
                .movePointLeft(group * 3)
                .round(new MathContext(significantDigits, RoundingMode.DOWN));

        if (val.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            val = val.movePointLeft(3);
            group += 1;
        }

        int baseIndex = group - 1;
        int cycle = baseIndex / SUFFIX.length;
        int tierIdx = baseIndex % SUFFIX.length;
        String suffix = SUFFIX[tierIdx] + (cycle == 0 ? "" : String.valueOf(cycle + 1));

        String s = val.stripTrailingZeros().toPlainString();

        return (neg ? "-" : "") + s + suffix;
    }
}
