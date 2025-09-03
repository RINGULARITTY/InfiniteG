package fr.ringularity.infiniteg.format;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public final class BigIntegerFormat {
    private BigIntegerFormat() {}

    private static final String[] SUFFIX = { "K", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };

    public static String format(BigInteger n) {
        boolean neg = n.signum() < 0;
        BigInteger abs = n.abs();

        if (abs.compareTo(BigInteger.valueOf(1000)) < 0) {
            return (neg ? "-" : "") + abs.toString();
        }

        int digits = abs.toString().length();
        int group = (digits - 1) / 3;

        BigDecimal val = new BigDecimal(abs).movePointLeft(group * 3)
                .round(new MathContext(3, RoundingMode.DOWN));

        if (val.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            val = val.movePointLeft(3);
            group += 1;
        }

        int baseIndex = group - 1;
        int cycle = baseIndex / SUFFIX.length;
        int tierIdx = baseIndex % SUFFIX.length;
        String suffix = SUFFIX[tierIdx] + (cycle == 0 ? "" : String.valueOf(cycle + 1));

        String s = val.stripTrailingZeros().toPlainString();
        if (s.indexOf('.') >= 0) {
            while (s.endsWith("0")) s = s.substring(0, s.length() - 1);
            if (s.endsWith(".")) s = s.substring(0, s.length() - 1);
        }

        return (neg ? "-" : "") + s + suffix;
    }
}
