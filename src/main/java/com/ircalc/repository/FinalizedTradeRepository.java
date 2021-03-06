package com.ircalc.repository;

import com.ircalc.model.FinalizedTrade;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Carlos on 26/11/2017.
 */
public interface FinalizedTradeRepository extends JpaRepository<FinalizedTrade, Long> {

	List<FinalizedTrade> findByCloseTradeIdIn(List<Long> openDayTradeReferenceIds);

	@Query("Select f FROM FinalizedTrade f JOIN f.openVirtualTrades ref WHERE ref.id = :virtualTradeId")
	List<FinalizedTrade> findAllWithReferenceId(@Param("virtualTradeId") Long virtualTradeId);

	List<FinalizedTrade> findAllByCloseTrade_Trade_Ticket(String ticket);

	@Query("FROM FinalizedTrade finalized WHERE finalized.closeTrade.trade.date >= :initInclusive AND finalized.closeTrade.trade.date < :endExclusive")
	List<FinalizedTrade> findByDateRange(@Param("initInclusive") Date initInclusive, @Param("endExclusive") Date endExclusive);
}
