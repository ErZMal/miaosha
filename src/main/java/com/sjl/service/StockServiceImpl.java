package com.sjl.service;

import com.sjl.dao.OrderDao;
import com.sjl.dao.StockDao;
import com.sjl.entity.Order;
import com.sjl.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    @Autowired
    private StockDao stockDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public  Integer kill(Integer id) {
        // 校验库存
        Stock stock = checkStock(id);

        // 扣除库存
        updateStock(stock);

        // 创建订单
       return createOrder(stock);
    }

    //校验库存
    private Stock checkStock(Integer id){
        Stock stock = stockDao.checkStock(id);
        if (stock == null) throw new RuntimeException("商品信息不存在！！");
        if (stock.getSale().equals(stock.getCount()))
            throw new RuntimeException("库存不足！！！");
        return stock;
    }

    //扣除库存
    private void updateStock(Stock stock){
        stock.setSale(stock.getSale() + 1);
        stockDao.updateStock(stock);
    }

    //生成订单
    private int createOrder(Stock stock){
        Order order = new Order();
        order.setName(stock.getName()).setSid(stock.getId()).setCreateDate(new Date());
        orderDao.createOrder(order);
        return order.getId();
    }

}
