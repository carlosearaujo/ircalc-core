package com.ircalc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Carlos on 26/11/2017.
 */
@Entity
@Table(name = "FINALIZED_TRADE") @NoArgsConstructor @Getter @Setter
public class FinalizedTrade {
	
	private static final Double DEFAULT_DEAL_FEE = (0.004994 / 100);
	private static final Double DEFAULT_EXCHANGE_FEE= (0.0275 / 100);
	private static final Double DEFAULT_REGISTRATION_FEE = (0.0);

	private static final Double DEFAULT_DEAL_FEE_DAYTRADE = DEFAULT_DEAL_FEE;
	private static final Double DEFAULT_EXCHANGE_FEE_DAYTRADE = (0.0200 /100);

	private static final Double DEFAULT_DEAL_FEE_OPTION = (0.037 / 100);
	private static final Double DEFAULT_EXCHANGE_FEE_OPTION = (0.0275 / 100);
	private static final Double DEFAULT_REGISTRATION_FEE_OPTION = (0.0695 / 100);

	private static final Double DEFAULT_DEAL_FEE_OPTION_DAYTRADE = (0.013 / 100);
	private static final Double DEFAULT_EXCHANGE_FEE_OPTION_DAYTRADE = (0.018 /100);
	private static final Double DEFAULT_REGISTRATION_FEE_OPTION_DAYTRADE = (0.014 / 100);

    @Id
    @SequenceGenerator(name="sqc_finalized_trade", sequenceName="sqc_finalized_trade")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqc_finalized_trade")
    private Long id;

    @OneToMany(fetch = FetchType.EAGER) 
    @JoinTable(name = "FINALIZED_TRADE_TRADE_REL")
    private List<Trade> referencedTrades;
    
    @ManyToOne private Trade closeTrade;
    
    @Column private CloseTime closeTime;
    
	@Column(nullable = false) private Double exchangeFeeAliquot;
	
	@Column(nullable = false) private Double dealFeeAliquot;
	
	@Column(nullable = false)private Double registrationFeeAliquot;

    public FinalizedTrade(OpenTrade openTrade, Trade closeTrade) {
        
    }

    public Double getResult() {
        Double result = closeTrade.priceAfterFees(getTotalMarketFees(closeTrade)) - (getReferencedTradesAVGPrice() * closeTrade.getQuantity());
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
            totalCost += referencedTrade.priceAfterFees(getTotalMarketFees(referencedTrade));
        }
        return totalCost / totalQuantity;
    }

	public Double getTotalMarketFees(Trade trade){
		return (exchangeFeeAliquot + dealFeeAliquot + registrationFeeAliquot) * trade.priceBeforeFees();
	}
	
	public void setCloseTime(CloseTime closeTime){
		this.closeTime = closeTime;
		updateFees();
	}
	
	public void setCloseTrade(Trade closeTrade){
		this.closeTrade = closeTrade;
		updateFees();
	}
	
	public void updateFees(){
		if(closeTime == null || closeTrade == null){
			return;
		}
		if(MarketType.DEFAULT.equals(closeTrade.getMarketType())){
			if(CloseTime.NORMAL.equals(closeTime)){
				exchangeFeeAliquot = DEFAULT_EXCHANGE_FEE;
				dealFeeAliquot = DEFAULT_DEAL_FEE;
				registrationFeeAliquot = DEFAULT_REGISTRATION_FEE;
			}
			else if(CloseTime.DAYTRADE.equals(closeTime)){
				exchangeFeeAliquot = DEFAULT_EXCHANGE_FEE_DAYTRADE;
				dealFeeAliquot = DEFAULT_DEAL_FEE_DAYTRADE;
				registrationFeeAliquot = DEFAULT_REGISTRATION_FEE;
			}
		}
		else{
			if(CloseTime.NORMAL.equals(closeTime)){
				exchangeFeeAliquot = DEFAULT_EXCHANGE_FEE_OPTION;
				dealFeeAliquot = DEFAULT_DEAL_FEE_OPTION;
				registrationFeeAliquot = DEFAULT_REGISTRATION_FEE_OPTION;
			}
			else if(CloseTime.DAYTRADE.equals(closeTime)){
				exchangeFeeAliquot = DEFAULT_EXCHANGE_FEE_OPTION_DAYTRADE;
				dealFeeAliquot = DEFAULT_DEAL_FEE_OPTION_DAYTRADE;
				registrationFeeAliquot = DEFAULT_REGISTRATION_FEE_OPTION_DAYTRADE;
			}
		}
	}
}
