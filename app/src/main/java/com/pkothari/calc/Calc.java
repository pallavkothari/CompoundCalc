package com.pkothari.calc;


import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.BitSet;

/**
 * Compounding calculator.
 *
 * Given 3, calculates the missing variable out of {FV, PV, rate, periods}
 */
public class Calc {

    BigDecimal futureValue; // input 0
    BigDecimal presentValue; // input 1
    BigDecimal periods; // input 2
    BigDecimal rate; // input 3
    BitSet inputs = new BitSet(4);

    Calc() {}

    public Calc futureValue(int fv) {
        this.futureValue = BigDecimal.valueOf(fv);
        this.inputs.flip(0);
        return this;
    }

    public Calc presentValue(int pv) {
        this.presentValue = BigDecimal.valueOf(pv);
        this.inputs.flip(1);
        return this;
    }

    public Calc period(int periods) {
        this.periods = BigDecimal.valueOf(periods);
        this.inputs.flip(2);
        return this;
    }

    public Calc rate(double rate) {
        this.rate = BigDecimal.valueOf(rate);
        this.inputs.flip(3);
        return this;
    }

    enum FIELD {
        FUTURE_VALUE, PRESENT_VALUE, PERIODS, RATE, ERROR
    }

    public FIELD go() {
        if (inputs.cardinality() != 3) return FIELD.ERROR;
        int missing = inputs.nextClearBit(0);
        switch (missing) {
            case 0:
                findFutureValue();
                break;
            case 1:
                findPresentValue();
                break;
            case 2:
                findPeriods();
                break;
            case 3:
                findRate();
                break;
            default:
                throw new Error("unknown index : " + missing);
        }
        return FIELD.values()[missing];
    }

    // (FV/PV)^1/n - 1
    private void findRate() {
        BigDecimal tmp = futureValue.divide(presentValue, 4, RoundingMode.CEILING);
        double r = Math.pow(tmp.doubleValue(), 1.0 / this.periods.doubleValue()) - 1;
        this.rate = BigDecimal.valueOf(r);
    }

    // log(FV/PV) / log(1+r)
    private void findPeriods() {
        BigDecimal tmp = futureValue.divide(presentValue, 4, RoundingMode.CEILING);
        double num = Math.log(tmp.doubleValue());
        double den = Math.log(1 + rate.doubleValue());
        this.periods = BigDecimal.valueOf(num).divide(BigDecimal.valueOf(den), 1, RoundingMode.CEILING);
    }

    // PV = FV / (1+r)^n
    private void findPresentValue() {
        this.presentValue = this.futureValue.divide(this.rate.add(BigDecimal.ONE).pow(this.periods.intValue()), 4, RoundingMode.HALF_EVEN);
    }

    // FV = PV(1+r)^n
    private void findFutureValue() {
        this.futureValue = this.presentValue.multiply(this.rate.add(BigDecimal.ONE).pow(this.periods.intValue()));
    }

    public BigDecimal getRateForDisplay() {
        return rate.movePointRight(2).round(MathContext.DECIMAL32);
    }
}
