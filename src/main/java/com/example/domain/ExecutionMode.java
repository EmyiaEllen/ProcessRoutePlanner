package com.example.domain;

import com.example.AbsEntity;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 执行模式/任务安排/资源受限
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExecutionMode extends AbsEntity {

    /**
     * 所需要执行的任务/资源所受限于的任务
     */
    private Job job;
    /**
     * 任务所需的时间间隔/资源被使用的时长
     */
    private BigDecimal duration;
    /**
     * 任务的资源需求列表
     */
    private List<ResourceRequirement> resourceRequirements;

    public ExecutionMode(Long id, Job job, BigDecimal duration, List<ResourceRequirement> resourceRequirements) {
        super(id);
        this.job = job;
        this.duration = duration;
        this.resourceRequirements = resourceRequirements;
    }
}
