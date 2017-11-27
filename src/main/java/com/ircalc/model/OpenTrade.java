package com.ircalc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Carlos on 25/11/2017.
 */

@Getter @Setter @NoArgsConstructor
@Entity(name = "OPEN_TRADE")
public class OpenTrade {


    @Id
    @SequenceGenerator(name="sqc_open_trade", sequenceName="sqc_open_trade")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqc_open_trade")
    private Long id;

    @Column
    private String ticket;
    @OneToMany
    @JoinTable(name =  "OPEN_TRADE_TRADE_REL")
    private List<Trade> referenceTrades;
    @Column
    private Long openQuantity;
    @Column
    private MarketDirection marketDirection;

    public OpenTrade(Trade origin){
        ticket = origin.getTicket();
        referenceTrades = Arrays.asList(new Trade[]{origin});
        openQuantity = origin.getQuantity();
        marketDirection = origin.getMarketDirection();
    }

    public void addNewReference(Trade trade) {
        openQuantity += trade.getQuantity();
        referenceTrades.add(trade);
    }

    public void decreaseOpenQuantity(Long quantity) {
        setOpenQuantity(getOpenQuantity() - quantity);
    }
}
