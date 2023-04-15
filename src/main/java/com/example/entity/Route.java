package com.example.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("route")
public class Route {
    @TableId(type = IdType.AUTO)
    private Long rid;
    private Long nid;
    private String name;
    private BigDecimal duration;
    private Boolean beEnd;
}
