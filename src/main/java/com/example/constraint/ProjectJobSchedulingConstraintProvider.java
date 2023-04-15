package com.example.constraint;

import com.example.domain.Allocation;
import com.example.domain.JobType;
import com.example.domain.ResourceRequirement;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.stream.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProjectJobSchedulingConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[0];
    }

    /**
     * 非可再生资源容量限制
     */
    protected Constraint nonRenewableResourceCapacity(ConstraintFactory constraintFactory) {
        // 遍历ResourceRequirement
        return constraintFactory.forEach(ResourceRequirement.class)
                // 只处理非可再生资源
                .filter(resource -> !resource.isResourceRenewable())
                // 引入新的变量: Allocation
                .join(Allocation.class,
                        Joiners.equal(ResourceRequirement::getExecutionMode, Allocation::getExecutionMode))
                // 按照资源进行分组, 统计该资源的需求数量
                .groupBy((requirement, allocation) -> requirement.getResource(),
                        ConstraintCollectors.sum((requirement, allocation) -> requirement.getRequirement()))
                // filter 和 penalize 配合使用, 惩罚超出可用资源的分配
                .filter((resource, requirements) -> requirements > resource.getWorkers().size())
                // 惩罚硬约束, 对于不符合的情况要进行处罚, 处罚力度是超出的情况
                .penalize(HardMediumSoftScore.ONE_HARD,
                        (resource, requirements) -> requirements - resource.getWorkers().size())
                .asConstraint("非可再生资源约束");
    }

    /**
     * 可再生资源容量限制
     */
    protected Constraint renewableResourceCapacity(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(ResourceRequirement.class)
                .filter(ResourceRequirement::isResourceRenewable)
                .join(Allocation.class,
                        Joiners.equal(ResourceRequirement::getExecutionMode, Allocation::getExecutionMode))
                .flattenLast(a -> Allocation.range(a.getStartDateTime(), a.getEndDateTime()))
                .groupBy((resourceReq, date) -> resourceReq.getResource(),
                        (resourceReq, date) -> date,
                        ConstraintCollectors.sum((resourceReq, date) -> resourceReq.getRequirement()))
                .filter((resourceReq, date, totalRequirement) -> totalRequirement > resourceReq.getWorkers().size())
                .penalize(HardMediumSoftScore.ONE_HARD,
                        (resourceReq, date, totalRequirement) -> totalRequirement - resourceReq.getWorkers().size())
                .asConstraint("可再生资源约束");
    }

    protected Constraint totalProjectDelay(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Allocation.class)
                .filter(allocation -> allocation.getEndDateTime() != null)
                .filter(allocation -> allocation.getJobType() == JobType.SINK)
                .impact(HardMediumSoftScore.ONE_MEDIUM,
                        allocation -> allocation.getProjectCriticalPathEndDate() - allocation.getEndDate())
                .asConstraint("Total project delay");
    }

    protected Constraint totalMakespan(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Allocation.class)
                .filter(allocation -> allocation.getEndDate() != null)
                .filter(allocation -> allocation.getJobType() == JobType.SINK)
                .groupBy(ConstraintCollectors.max(Allocation::getEndDate))
                .penalize(HardMediumSoftScore.ONE_SOFT, maxEndDate -> maxEndDate)
                .asConstraint("Total makespan");
    }

}
