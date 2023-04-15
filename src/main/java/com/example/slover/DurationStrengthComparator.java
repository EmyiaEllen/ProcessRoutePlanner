package com.example.slover;

import jakarta.annotation.Nonnull;

import java.math.BigDecimal;
import java.util.Comparator;

public class DurationStrengthComparator implements Comparator<BigDecimal> {
    @Override
    public int compare(@Nonnull BigDecimal o1, @Nonnull BigDecimal o2) {
        return o1.compareTo(o2);
    }
}
