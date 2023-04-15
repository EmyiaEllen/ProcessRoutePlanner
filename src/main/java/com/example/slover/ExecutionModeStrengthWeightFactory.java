package com.example.slover;

import com.example.domain.ExecutionMode;
import com.example.domain.Resource;
import com.example.domain.ResourceRequirement;
import com.example.domain.Schedule;
import jakarta.annotation.Nonnull;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * ExecutionMode分配权重计算工厂
 */
public class ExecutionModeStrengthWeightFactory implements SelectionSorterWeightFactory<Schedule, ExecutionMode> {
    /**
     * 为ExecutionMode创建可以比较的{@link  ExecutionModeStrengthWeight} 对象
     *
     * @param schedule  never null, the {@link PlanningSolution} to which the selection belongs or applies to
     * @param selection never null, a {@link PlanningEntity}, a planningValue, a {@link Move} or a {@link Selector}
     * @return 可比较对象
     */
    @Override
    public ExecutionModeStrengthWeight createSorterWeight(Schedule schedule, ExecutionMode selection) {
        // 当前 ExecutionMode 所需要的资源
        Map<Resource, Integer> resourceMap = new HashMap<>(selection.getResourceRequirements().size());
        for (ResourceRequirement requirement : selection.getResourceRequirements()) {
            resourceMap.put(requirement.getResource(), 0);
        }
        // 整个Schedule总共的资源
        for (ResourceRequirement requirement : schedule.getResourceRequirements()) {
            resourceMap.computeIfPresent(requirement.getResource(), (k, v) -> v + requirement.getRequirement());
        }
        // 判断当前 ExecutionMode 使用资源是否超过了 所有资源
        double requirementDesirability = 0.0;
        for (ResourceRequirement requirement : selection.getResourceRequirements()) {
            final Resource resource = requirement.getResource();
            final Integer total = resourceMap.get(requirement.getResource());
            if (total > resource.getWorkers().size()) {
                requirementDesirability += (total - resource.getWorkers().size())
                        * (double) requirement.getRequirement()
                        * (resource.renewable() ? 1.0 : 100.0);
            }
        }
        return new ExecutionModeStrengthWeight(selection, requirementDesirability);
    }

    /**
     * 比较器, 比较两个Execution的权重大小
     */
    public static class ExecutionModeStrengthWeight implements Comparable<ExecutionModeStrengthWeight> {

        private static final Comparator<ExecutionModeStrengthWeight> COMPARATOR =
                Comparator.<ExecutionModeStrengthWeight>comparingDouble(__ -> __.requirementDesirability)
                        .thenComparing(__ -> __.executionMode, Comparator.comparingLong(ExecutionMode::getId));

        private final ExecutionMode executionMode;
        private final double requirementDesirability;

        public ExecutionModeStrengthWeight(ExecutionMode executionMode, double requirementDesirability) {
            this.executionMode = executionMode;
            this.requirementDesirability = requirementDesirability;
        }

        @Override
        public int compareTo(@Nonnull ExecutionModeStrengthWeight o) {
            return COMPARATOR.compare(this, o);
        }
    }
}
