package com.example.domain;

import com.example.slover.DurationStrengthComparator;
import com.example.slover.ExecutionModeStrengthWeightFactory;
import com.example.slover.NotSourceOrSinkAllocationFilter;
import com.example.slover.PrecursorsDoneDateUpdatingVariableListener;
import lombok.Data;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.CountableValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.ShadowVariable;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 工序安排
 */
@Data
@PlanningEntity(pinningFilter = NotSourceOrSinkAllocationFilter.class)
public class Allocation {
    /**
     * 工作所属项目
     */
    private Project project;
    /**
     * 工作
     */
    private Job job;
    /**
     * 工作重复次数
     */
    private Long repeat;
    /**
     * 项目的起始分配
     */
    private Allocation source;
    /**
     * 项目的最后分配
     */
    private Allocation sink;
    /**
     * 工作的前置分配工作
     */
    private List<Allocation> precursors;
    /**
     * 工作的后置分配工作
     */
    private List<Allocation> successors;

    /**
     * 工作使用的工作模式
     */
    @PlanningVariable(strengthWeightFactoryClass = ExecutionModeStrengthWeightFactory.class)
    private ExecutionMode executionMode;
    /**
     * 工作间隔（工作准备时间）
     */
    @PlanningVariable(strengthComparatorClass = DurationStrengthComparator.class)
    private Duration delay;

    /**
     * 全部前驱完成的最晚时间
     */
    @ShadowVariable(sourceVariableName = "executionMode",
            variableListenerClass = PrecursorsDoneDateUpdatingVariableListener.class)
    @ShadowVariable(sourceVariableName = "duration",
            variableListenerClass = PrecursorsDoneDateUpdatingVariableListener.class)
    private LocalDateTime precursorsDoneDate;

    /**
     * 获取本分配任务的开始时间
     */
    public LocalDateTime getStartDateTime() {
        if (precursorsDoneDate == null) return null;
        LocalDateTime start = precursorsDoneDate;
        if (delay != null)
            start = start.plus(delay);
        return start;
    }

    /**
     * 获取本分配任务的结束时间
     */
    public LocalDateTime getEndDateTime() {
        if (precursorsDoneDate == null) return null;
        LocalDateTime end = precursorsDoneDate;
        if (delay != null)
            end = end.plus(delay);
        if (executionMode.getDuration() != null) {
            final Job job = executionMode.getJob();
            if (job != null) {
                final Project project = job.getProject();
                if (project != null) {
                    final Long count = project.getCount();
                    final BigDecimal duration = executionMode.getDuration();
                    end = end.plus(Duration.ofNanos(toNanos(duration).multiply(BigDecimal.valueOf(count)).longValue()));
                }
            }
        }
        return end;
    }

    private static final BigDecimal NANOS_UNIT = BigDecimal.valueOf(1E9);

    private BigDecimal toNanos(BigDecimal second) {
        return second.multiply(NANOS_UNIT);
    }


    @ValueRangeProvider
    public List<ExecutionMode> getExecutionModeRange() {
        return job.getExecutionModes();
    }

    @ValueRangeProvider
    public CountableValueRange<LocalDateTime> getDelayRange() {
        return ValueRangeFactory.createLocalDateTimeValueRange(
                precursorsDoneDate, precursorsDoneDate.plusHours(12),
                1L, ChronoUnit.MINUTES
        );
    }

    public static List<LocalDateTime> range(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end))
            return Collections.emptyList();
        List<LocalDateTime> list = new ArrayList<>();
        LocalDateTime now = start;
        do {
            list.add(now);
            now = now.plus(Duration.ofMinutes(1L));
        } while (!now.isAfter(end));
        return list;
    }

    public JobType getJobType() {
        return job.getType();
    }
}
