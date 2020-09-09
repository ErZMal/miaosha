package com.sjl.dao;

import com.sjl.entity.User;

public interface UserDao {

    User findById(Integer id);
}
