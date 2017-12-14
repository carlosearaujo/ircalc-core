package com.ircalc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trade trade;
    @Column
    private Long quantity;
    
    @Transient
    private transient boolean isDayTrade;

    public VirtualTrade(Trade trade, Long quantity) {
        this.trade = trade;
        this.quantity = quantity;
    }

    public VirtualTrade(Trade trade) {
        this(trade, trade.getQuantity());
    }

	public VirtualTrade(Trade trade, boolean isDayTrade) {
		this(trade);
		this.isDayTrade = isDayTrade;
	}

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	public Date getDate() {
		return getTrade().getDate();
	}

	public String getTicket() {
		return getTrade().getTicket();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VirtualTrade other = (VirtualTrade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public MarketDirection getMarketDirection() {
		return trade.getMarketDirection();
	}


	public Double pricePerUnitAfterFees(Double marketFeesAliquot) {
		return trade.pricePerUnitAfterFees(marketFeesAliquot);
	}
	
	public Double totalPriceAfterFees(Double marketFeesAliquot){
		return trade.pricePerUnitAfterFees(marketFeesAliquot) * quantity;
	}

	@JsonIgnore
	public MarketDirection getMarketDirectionComplement() {
		return trade.getMarketDirectionComplement();
	}
}
