package com.ircalc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Carlos on 25/11/2017.
 */

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "OPEN_TRADE")
public class OpenTrade {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String ticket;
    @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
    @JoinTable(name =  "OPEN_TRADE_TRADE_REL", joinColumns = @JoinColumn(name = "OPEN_TRADE_ID"), inverseJoinColumns = @JoinColumn(name = "VIRTUAL_TRADE_ID"),
    		   foreignKey = @ForeignKey(name = "FK_OPEN_TRADE_TRADE_REL_OPEN_TRADE"), inverseForeignKey = @ForeignKey(name = "FK_OPEN_TRADE_TRADE_REL_VIRTUAL_TRADE"))
    private List<VirtualTrade> openVirtualTrades;
    @Column
    private Long openQuantity;
    @Column
    private MarketDirection marketDirection;
    @Column
    private CloseTime closeTime;

    public OpenTrade(VirtualTrade origin){
        ticket = origin.getTrade().getTicket();
        openVirtualTrades = new ArrayList<>(Arrays.asList(new VirtualTrade[]{origin}));
        openQuantity = origin.getQuantity();
        marketDirection = origin.getTrade().getMarketDirection();
    }

    public void addNewReference(VirtualTrade virtualTrade) {
    	if(CloseTime.DAYTRADE.equals(closeTime) && !virtualTrade.getDate().equals(openVirtualTrades.get(0).getDate())){
    		throw new RuntimeException("Trying add new reference with diferent date in daytrade type");
    	}
        openQuantity += virtualTrade.getQuantity();
        openVirtualTrades.add(virtualTrade);
    }

    public void decreaseOpenQuantity(Long quantity) {
        setOpenQuantity(getOpenQuantity() - quantity);
    }
}
