package com.ircalc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 26/11/2017.
 */
@Entity
@Table(name = "FINALIZED_TRADE") @NoArgsConstructor @Getter @Setter
public class FinalizedTrade {

    @Id
    @SequenceGenerator(name="sqc_finalized_trade", sequenceName="sqc_finalized_trade")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqc_finalized_trade")
    private Long id;

    @OneToMany(fetch = FetchType.EAGER) 
    @JoinTable(name = "FINALIZED_TRADE_TRADE_REL")
    private List<Trade> referencedTrades;
    @ManyToOne
    private Trade closeTrade;
    @Column
    private CloseTime closeTime;

    public FinalizedTrade(OpenTrade openTrade, Trade closeTrade) {
        referencedTrades = new ArrayList<>(openTrade.getReferenceTrades());
        this.closeTrade = closeTrade;
        closeTime = CloseTime.NORMAL;
    }

    public Double getResult() {
        Double result = closeTrade.getFinalPrice() - (getReferencedTradesAVGPrice() * closeTrade.getQuantity());
        return MarketDirection.BUY.equals(closeTrade.getMarketDirection()) ? -result : result;
    }

    public Double getIR(){
        return getResult() > 0 ? getResult() * closeTime.getTaxAliquot() : 0;
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
