ALTER TABLE open_trade_trade_rel DROP CONSTRAINT fk_open_trade_trade_rel_virtual_trade;
ALTER TABLE open_trade_trade_rel
   ADD CONSTRAINT fk_open_trade_trade_rel_virtual_trade
   FOREIGN KEY (VIRTUAL_TRADE_ID) 
   REFERENCES VIRTUAL_TRADE(id) ON DELETE CASCADE;

ALTER TABLE finalized_trade_trade_rel DROP CONSTRAINT fk_finalized_trade_trade_rel_virtual_trade;
ALTER TABLE finalized_trade_trade_rel
   ADD CONSTRAINT fk_finalized_trade_trade_rel_virtual_trade
   FOREIGN KEY (VIRTUAL_TRADE_ID) 
   REFERENCES VIRTUAL_TRADE(id) ON DELETE CASCADE