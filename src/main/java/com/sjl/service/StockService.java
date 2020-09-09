package com.sjl.service;

import com.sjl.entity.Stock;

public interface StockService {

    Integer kill(Integer id);

    int kill(Integer id, Integer userId, String md5);
}
