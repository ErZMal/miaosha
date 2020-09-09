package com.sjl.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Order {

    private Integer id;
    private Integer sid;
    private String name;
    private Date createDate;
}
