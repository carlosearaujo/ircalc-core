package com.ircalc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**@author carlos.araujo
   @since  17 de nov de 2017*/
@Entity @Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "TRADE")
public class Trade {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

	public Trade(long quantity) {
		this.quantity = quantity;
	}

	public Trade(int quantity, MarketDirection marketDirection) {
		this(quantity);
		this.marketDirection = marketDirection;
	}

	public Trade(Date date) {
		this.date = date;
	}

	public Trade(MarketDirection marketDirection) {
		this.marketDirection = marketDirection;
	}

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
		if(pricePerUnit == null || quantity == null){
			return 0D;
		}
		return (pricePerUnit * quantity);
	}

	public Double getTotalBrokerCost(){
		if(brokerTax == null || brokerTaxFee == null){
			return 0D;
		}
		return brokerTax * (1 + brokerTaxFee);
	}
	
	public MarketDirection getMarketDirectionComplement(){
		if(MarketDirection.BUY.equals(this.getMarketDirection())){
			return MarketDirection.SELL;
		}
		return MarketDirection.BUY;
	}

}
