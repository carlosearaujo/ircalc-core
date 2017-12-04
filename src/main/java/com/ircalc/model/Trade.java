package com.ircalc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**@author carlos.araujo
   @since  17 de nov de 2017*/
@Entity @Getter @Setter
@Table(name = "TRADE")
public class Trade {

	@Id
	@SequenceGenerator(name="sqc_trade", sequenceName="sqc_trade")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqc_trade")
	private Long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@Column(nullable = false)
	private Date date;
	@Column(nullable = false)
	private String ticket;
	@Column(nullable = false)
	private Double pricePerUnit;
	@Column(nullable = false)
	private Long quantity;
	@Column(nullable = false)
	private MarketType marketType = MarketType.DEFAULT;
	@Column(nullable = false)
	private MarketDirection marketDirection;
	@Column(nullable = false)
	private Double brokerTax;
	@Column(nullable = false)
	private Double brokerTaxFee;

	public Double priceAfterFees(Double marketFees){
		return priceBeforeFees() + (getTotalFees(marketFees) * (MarketDirection.BUY.equals(marketDirection) ? 1 : -1));
	}
	
	public Double getPriceAfterBrokerFees(){
		return priceBeforeFees() + (getTotalBrokerCost() * (MarketDirection.BUY.equals(marketDirection) ? 1 : -1));
	}

	public Double getTotalFees(Double marketFees){
		return marketFees + getTotalBrokerCost();
	}

	public Double avgBeforeFees(){
		return priceBeforeFees() / quantity;
	}

	public Double avgFinalPrice(Double marketFees){
		return priceAfterFees(marketFees) / quantity;
	}

	public Double priceBeforeFees(){
		return (pricePerUnit * quantity);
	}

	public Double getTotalBrokerCost(){
		return brokerTax * (1 + brokerTaxFee);
	}
	
	public MarketDirection getMarketDirectionComplement(){
		if(MarketDirection.BUY.equals(this.getMarketDirection())){
			return MarketDirection.SELL;
		}
		return MarketDirection.BUY;
	}

	public void setMarketType(MarketType marketType){
		this.marketType = marketType;
	}

}
