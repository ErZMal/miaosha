package com.sjl.controller;

import com.sjl.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
@Slf4j
public class StockController {

    @Autowired
    private StockService stockService;

    //接收秒杀请求参数，调用业务创建订单
    @GetMapping("/kill")
    public String kill(Integer id){

        //调用秒杀业务
        int orderId;
        try{

            orderId = stockService.kill(id);
            log.info("拿到订单的id为：[{}]",orderId);
            return "订单的id为："+orderId;
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }

    }
}
