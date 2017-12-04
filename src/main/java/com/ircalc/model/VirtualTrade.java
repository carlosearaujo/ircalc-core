package com.ircalc.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Carlos on 04/12/2017.
 */
@Entity
@Getter @Setter
@Table(name = "VIRTUAL_TRADE")
public class VirtualTrade {

    @Id
    @SequenceGenerator(name="sqc_virtual_trade", sequenceName="sqc_virtual_trade")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqc_virtual_trade")
    private Long id;

    @ManyToOne
    private Trade trade;
    @Column
    private Long quantity;


    public VirtualTrade(Trade trade, Long quantity) {
        this.trade = trade;
        this.quantity = quantity;
    }

    public VirtualTrade(Trade trade) {
        this(trade, trade.getQuantity());
    }
}
