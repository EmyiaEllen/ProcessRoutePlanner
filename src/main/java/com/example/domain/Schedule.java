package com.example.domain;

import lombok.Data;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;

import java.util.List;

/**
 * 调度类, 问题解决的类
 */
@Data
@PlanningSolution
public class Schedule {
    // 问题的事实集合, 运行过程中不可改变
    @ProblemFactCollectionProperty
    private List<Project> projects;
    @ProblemFactCollectionProperty
    private List<Job> jobs;
    @ProblemFactCollectionProperty
    private List<ExecutionMode> executionModes;
    @ProblemFactCollectionProperty
    private List<Resource> resources;
    @ProblemFactCollectionProperty
    private List<ResourceRequirement> resourceRequirements;

    // 规划实体, 动态变化
    @PlanningEntityCollectionProperty
    private List<Allocation> allocations;
}
