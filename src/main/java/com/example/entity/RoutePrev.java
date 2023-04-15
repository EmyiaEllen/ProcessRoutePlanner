package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("route_prev")
@Data
public class RoutePrev {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long nid;
    private Long pid;
}
