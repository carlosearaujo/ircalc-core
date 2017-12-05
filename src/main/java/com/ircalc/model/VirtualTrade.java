package com.ircalc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import javax.persistence.*;

/**
 * Created by Carlos on 04/12/2017.
 */
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "VIRTUAL_TRADE")
public class VirtualTrade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

	public Date getDate() {
		return getTrade().getDate();
	}

	public String getTicket() {
		return getTrade().getTicket();
	}
}
