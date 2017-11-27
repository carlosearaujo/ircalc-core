package com.ircalc.model;

/**
 * Created by Carlos on 26/11/2017.
 */
public enum CloseTime {
    NORMAL(15.0 / 100), DAYTRADE(20.0 / 100);

    private CloseTime(Double taxAliquot){
        this.taxAliquot = taxAliquot;
    }

    private Double taxAliquot;

    public Double getTaxAliquot() {
        return taxAliquot;
    }
}
