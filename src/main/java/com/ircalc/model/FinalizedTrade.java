package com.ircalc.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Carlos on 26/11/2017.
 */
@Entity(name = "FINALIZED_TRADE") @NoArgsConstructor
public class FinalizedTrade {

    @Id
    @SequenceGenerator(name="sqc_finalized_trade", sequenceName="sqc_finalized_trade")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqc_finalized_trade")
    private Long id;

    @OneToMany @JoinTable(name = "FINALIZED_TRADE_TRADE_REL")
    private List<Trade> referencedTrades;
    @ManyToOne
    private Trade closeTrade;
    @Column
    private CloseTime closeTime;

    public FinalizedTrade(OpenTrade openTrade, Trade closeTrade) {
        referencedTrades = openTrade.getReferenceTrades();
        this.closeTrade = closeTrade;
        closeTime = CloseTime.NORMAL;
    }

    public Double result() {
        return closeTrade.getFinalPrice() - (getReferencedTradesAVGPrice() * closeTrade.getQuantity());
    }

    public Double getIR(){
        return result() > 0 ? result() * closeTime.getTaxAliquot() : 0;
    }

    public Double getReferencedTradesAVGPrice() {
        Long totalQuantity = 0L;
        Double totalCost = 0D;
        for(Trade referencedTrade : referencedTrades){
            totalQuantity += referencedTrade.getQuantity();
            totalCost += referencedTrade.getFinalPrice();
        }
        return totalCost / totalQuantity;
    }
}
