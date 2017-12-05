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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER) 
    @JoinTable(name = "FINALIZED_TRADE_TRADE_REL")
    private List<VirtualTrade> referencedTrades;
    
    @ManyToOne(cascade = CascadeType.PERSIST) private VirtualTrade closeTrade;
    
    @Column private CloseTime closeTime;
    
	@Column(nullable = false) private Double exchangeFeeAliquot;
	
	@Column(nullable = false) private Double dealFeeAliquot;
	
	@Column(nullable = false)private Double registrationFeeAliquot;

    public Double getResult() {
        Double result = closeTrade.getTrade().priceAfterFees(getTotalMarketFees(closeTrade.getTrade())) - (getReferencedTradesAVGPrice() * closeTrade.getQuantity());
        return MarketDirection.BUY.equals(closeTrade.getTrade().getMarketDirection()) ? -result : result;
    }

    public Double getIR(){
        return getResult() > 0 ? getResult() * closeTime.getTaxAliquot() : 0;
    }

    public Double getReferencedTradesAVGPrice() {
        Long totalQuantity = 0L;
        Double totalCost = 0D;
        for(VirtualTrade virtualTrade : referencedTrades){
            totalQuantity += virtualTrade.getQuantity();
            totalCost += virtualTrade.getTrade().priceAfterFees(getTotalMarketFees(virtualTrade.getTrade()));
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
	
	public void setCloseTrade(VirtualTrade closeTrade){
		this.closeTrade = closeTrade;
		updateFees();
	}
	
	public void updateFees(){
		if(closeTime == null || closeTrade == null){
			return;
		}
		if(MarketType.DEFAULT.equals(closeTrade.getTrade().getMarketType())){
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
