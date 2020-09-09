package com.sjl.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.sjl.service.OrderService;
import com.sjl.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/stock")
@Slf4j
public class StockController {

    @Autowired
    private StockService stockService;

    private RateLimiter rateLimiter = RateLimiter.create(20);//每秒可以获得令牌的数量

    @Autowired
    private OrderService orderService;

    //接收秒杀请求参数，调用业务创建订单
    @GetMapping("/kill")
    public String kill(Integer id){

        //调用秒杀业务
        int orderId;
        try{
            //添加悲观锁
            //synchronized (this){
            orderId = stockService.kill(id);
            log.info("拿到订单的id为：[{}]",orderId);
            return "订单的id为："+orderId;
            //}
        }catch (Exception e){
            log.info(e.getMessage());
            return e.getMessage();
        }

    }
    //使用令牌桶进行限流处理 并使用乐观锁来处理超卖问题
    @GetMapping("/killToken")
    public String killToken(Integer id){
        // 加入令牌桶算法
        if (!rateLimiter.tryAcquire(2,TimeUnit.SECONDS)){
            log.info("请求被抛弃：抢购失败，当前秒杀活动过于火爆，请重试！");
            return "请求被抛弃：抢购失败，当前秒杀活动过于火爆，请重试！";
        }

        try{
            //调用秒杀业务
            //添加悲观锁
            //synchronized (this){
            int orderId = stockService.kill(id);
            log.info("拿到订单的id为：[{}]",orderId);
            return "订单的id为："+String.valueOf(orderId);
            //}
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }

    }

    //使用令牌桶进行限流处理 并使用乐观锁来处理超卖问题 使用MD5隐藏抢购连接
    @GetMapping("/killTokenMd5")
    public String killTokenMd5(Integer id,Integer userId,String md5){
        // 加入令牌桶算法
        if (!rateLimiter.tryAcquire(2,TimeUnit.SECONDS)){
            log.info("请求被抛弃：抢购失败，当前秒杀活动过于火爆，请重试！");
            return "请求被抛弃：抢购失败，当前秒杀活动过于火爆，请重试！";
        }

        try{
            //调用秒杀业务
            //添加悲观锁
            //synchronized (this){
            int orderId = stockService.kill(id,userId,md5);
            log.info("拿到订单的id为：[{}]",orderId);
            return "订单的id为："+String.valueOf(orderId);
            //}
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }

    }

    @GetMapping("/md5")
    public String getMd5(Integer id,Integer userId){
        String md5;
        try {
            md5 = orderService.getMd5(id,userId);
        }catch (Exception e){
            log.info(e.getMessage());
            return "生成MD5错误，信息为："+e.getMessage();
        }
        return "生成MD5信息为："+md5;
    }

    //令牌桶算法的使用示例
    @GetMapping("/sale")
    public String sale(Integer id){
        /*令牌桶算法有两种处理机制
        *   1. 没有获得到token的请求会一直等待直到获得token令牌*/
        //log.info("等待的时间："+rateLimiter.acquire());

        // 2.设置一个等待时间，如果在等待时间内得到token了，则处理业务，否则抛弃该请求
        if (!rateLimiter.tryAcquire(3, TimeUnit.SECONDS)){
            log.warn("当前请求已被限流，直接抛弃，无法进行后续业务处理");
            return "抢购失败！";
        }
        log.info("处理业务........");

        return "抢购成功！";
    }
}
