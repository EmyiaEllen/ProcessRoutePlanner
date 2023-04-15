package com.example.slover;

import com.example.domain.Allocation;
import com.example.domain.JobType;
import com.example.domain.Schedule;
import org.optaplanner.core.api.domain.entity.PinningFilter;

/**
 * 过滤器, 过滤掉起始Job和中止Job
 */
public class NotSourceOrSinkAllocationFilter implements PinningFilter<Schedule, Allocation> {
    @Override
    public boolean accept(Schedule schedule, Allocation allocation) {
        JobType jobType = allocation.getJob().getType();
        return jobType == JobType.SOURCE || jobType == JobType.SINK;
    }
}
