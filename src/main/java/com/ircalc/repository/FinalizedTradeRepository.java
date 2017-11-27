package com.ircalc.repository;

import com.ircalc.model.FinalizedTrade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Carlos on 26/11/2017.
 */
public interface FinalizedTradeRepository extends JpaRepository<FinalizedTrade, Long> {
}
