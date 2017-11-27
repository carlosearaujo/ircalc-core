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
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**@author carlos.araujo
   @since  17 de nov de 2017*/
@Entity @Getter @Setter
@Table(name = "TRADE")
public class Trade {

	private static final Double DEFAULT_DEAL_FEE = (0.004994 / 100);
	private static final Double DEFAULT_EXCHANGE_FEE= (0.0275 / 100);
	private static final Double DEFAULT_REGISTRATION_FEE = (0.0);
	private static final Double DEFAULT_TOTAL_MARKET_FEE = DEFAULT_DEAL_FEE + DEFAULT_EXCHANGE_FEE;

	private static final Double DEFAULT_DEAL_FEE_DAYTRADE = DEFAULT_DEAL_FEE;
	private static final Double DEFAULT_EXCHANGE_FEE_DAYTRADE = (0.0200 /100);
	private static final Double DEFAULT_TOTAL_MARKET_FEE_DAY_TRADE = DEFAULT_DEAL_FEE_DAYTRADE + DEFAULT_EXCHANGE_FEE_DAYTRADE;

	private static final Double DEFAULT_DEAL_FEE_OPTION = (0.037 / 100);
	private static final Double DEFAULT_EXCHANGE_FEE_OPTION = (0.0275 / 100);
	private static final Double DEFAULT_REGISTRATION_FEE_OPTION = (0.0695 / 100);
	private static final Double DEFAULT_TOTAL_MARKET_OPTION_FEE = DEFAULT_DEAL_FEE_OPTION + DEFAULT_EXCHANGE_FEE_OPTION + DEFAULT_REGISTRATION_FEE_OPTION;

	private static final Double DEFAULT_DEAL_FEE_OPTION_DAYTRADE = (0.013 / 100);
	private static final Double DEFAULT_EXCHANGE_FEE_OPTION_DAYTRADE = (0.018 /100);
	private static final Double DEFAULT_REGISTRATION_FEE_OPTION_DAYTRADE = (0.014 / 100);
	private static final Double DEFAULT_TOTAL_MARKET_FEE_OPTION_DAY_TRADE = DEFAULT_DEAL_FEE_OPTION_DAYTRADE + DEFAULT_EXCHANGE_FEE_OPTION_DAYTRADE + DEFAULT_REGISTRATION_FEE_OPTION_DAYTRADE;

	private static final Double DEFAULT_BROKER_FEE = (5.0 / 100);

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
	@Column(nullable = false)
	private Double exchangeFeeAliquot;
	@Column(nullable = false)
	private Double dealFeeAliquot;
	@Column(nullable = false)
	private Double registrationFeeAliquot;

	public Double getFinalPrice(){
		return priceBeforeFees() + (getTotalFees() * (MarketDirection.BUY.equals(marketDirection) ? 1 : -1));
	}

	public Double getTotalFees(){
		return getTotalMarketFees() + getTotalBrokerCost();
	}

	public Double avgBeforeFees(){
		return priceBeforeFees() / quantity;
	}

	public Double avgFinalPrice(){
		return getFinalPrice() / quantity;
	}

	public Double priceBeforeFees(){
		return (pricePerUnit * quantity);
	}

	public Double getTotalMarketFees(){
		return (exchangeFeeAliquot + dealFeeAliquot + registrationFeeAliquot) * priceBeforeFees();
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
		if(MarketType.DEFAULT.equals(marketType)){
			exchangeFeeAliquot = DEFAULT_EXCHANGE_FEE;
			dealFeeAliquot = DEFAULT_DEAL_FEE;
			registrationFeeAliquot = DEFAULT_REGISTRATION_FEE;
		}
		else{
			exchangeFeeAliquot = DEFAULT_EXCHANGE_FEE_OPTION;
			dealFeeAliquot = DEFAULT_DEAL_FEE_OPTION;
			registrationFeeAliquot = DEFAULT_REGISTRATION_FEE_OPTION;
		}
	}

}
