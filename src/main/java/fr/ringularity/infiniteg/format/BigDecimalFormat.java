package fr.ringularity.infiniteg.format;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class BigDecimalFormat {
    private BigDecimalFormat() {}

    private static final String[] SUFFIX = { "K", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };

    public static String format(BigDecimal x) {
        return format_n(x, 3);
    }

    public static String format_n(BigDecimal x, int significantDigits) {
        if (significantDigits < 1) {
            throw new IllegalArgumentException("significantDigits must be >= 1");
        }

        int sign = x.signum();
        if (sign == 0) return "0";

        boolean neg = sign < 0;
        BigDecimal abs = x.abs();

        if (abs.compareTo(BigDecimal.valueOf(1000)) < 0) {
            String s = abs.stripTrailingZeros().toPlainString();
            return (neg ? "-" : "") + s;
        }

        int precision = abs.precision();
        int scale = abs.scale();
        int intDigits = precision - scale;
        int group = (intDigits - 1) / 3;

        BigDecimal val = abs.movePointLeft(group * 3)
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
