package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("work")
public class Work {
    @TableId(type = IdType.AUTO)
    private Long wid;
    private Long eid;
    private Long count;
    private Route route;
}
