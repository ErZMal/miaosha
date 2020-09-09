package com.sjl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Stock {

    private Integer id;
    private String name;
    private Integer count;
    private Integer sale;
    private Integer version;
}
