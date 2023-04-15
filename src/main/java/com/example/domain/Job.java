package com.example.domain;

import com.example.AbsEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/**
 * 工作/工序
 */
@Getter
@Setter
@ToString
public class Job extends AbsEntity {
    /**
     * 工序所属的项目/产品/订单
     */
    private Project project;
    /**
     * 工序类型：起点，中间，终点
     */
    private JobType type;
    /**
     * 前驱工序集合, 完成所有前驱工序才能开始当前工序
     */
    private Set<Job> precursorSet;
    /**
     * 后继工序集合, 完成当前工序是开启下个工序的条件之一
     */
    private Set<Job> successorSet;
    /**
     * 该工序的可执行模式列表, 表示工序所需要的时间和资源
     */
    private List<ExecutionMode> executionModes;
}
