package com.sjl.dao;

import com.sjl.entity.Stock;

public interface StockDao {

    Stock checkStock(Integer id);

    void updateStock(Stock stock);
}
