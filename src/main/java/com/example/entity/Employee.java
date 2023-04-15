package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("employee")
public class Employee {
    @TableId(type = IdType.AUTO)
    private Long eid;
    private String name;
}
