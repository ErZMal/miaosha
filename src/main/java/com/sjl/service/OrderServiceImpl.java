package com.sjl.service;

import com.sjl.dao.StockDao;
import com.sjl.dao.UserDao;
import com.sjl.entity.Stock;
import com.sjl.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String getMd5(Integer id, Integer userId) {
        //验证用户是否合理
        User user = userDao.findById(userId);
        if (user == null) throw new RuntimeException("用户信息不合法");
        //验证商品是否合理
        Stock stock = stockDao.checkStock(id);
        if (stock == null) throw new RuntimeException("商品信息不合法");
        //生成 hashKey
        String hashKey = "Key_"+userId+"_"+id;
        //生成MD5
        String md5 = DigestUtils.md5DigestAsHex((userId+id+"!Q@W").getBytes());

        redisTemplate.opsForValue().set(hashKey,md5,120, TimeUnit.SECONDS);
        return md5;
    }
}
