package com.example.slover;

import com.example.domain.Allocation;
import com.example.domain.Schedule;
import org.optaplanner.core.api.domain.variable.VariableListener;
import org.optaplanner.core.api.score.director.ScoreDirector;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Queue;

public class PrecursorsDoneDateUpdatingVariableListener implements VariableListener<Schedule, Allocation> {

    @Override
    public void beforeVariableChanged(ScoreDirector<Schedule> scoreDirector, Allocation allocation) {
        //Do Nothing
    }

    @Override
    public void afterVariableChanged(ScoreDirector<Schedule> scoreDirector, Allocation allocation) {
        //Do Nothing
    }

    @Override
    public void beforeEntityAdded(ScoreDirector<Schedule> scoreDirector, Allocation allocation) {
        //Do Nothing
    }

    @Override
    public void afterEntityAdded(ScoreDirector<Schedule> scoreDirector, Allocation allocation) {
        updateAllocation(scoreDirector, allocation);
    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<Schedule> scoreDirector, Allocation allocation) {
        //Do Nothing
    }

    @Override
    public void afterEntityRemoved(ScoreDirector<Schedule> scoreDirector, Allocation allocation) {
        //Do Nothing
    }

    /**
     * 响应allocation更新时间, 并做出其他更新
     */
    protected void updateAllocation(ScoreDirector<Schedule> scoreDirector, Allocation originalAllocation) {
        Queue<Allocation> uncheckedSuccessorQueue = new ArrayDeque<>(originalAllocation.getSuccessors());
        while (!uncheckedSuccessorQueue.isEmpty()) {
            Allocation allocation = uncheckedSuccessorQueue.remove();
            // 判断后继结点是否需要因当前结点更新导致其结束日期发生变化
            // 同时修改最晚完成时间
            boolean updated = updatePredecessorsDoneDate(scoreDirector, allocation);
            if (updated) {
                // 如果当前结点的一个后继结点的结束时间发生变化
                // 那么所有依赖于该后继节点为前驱的其他结点也要做出相应的调整
                uncheckedSuccessorQueue.addAll(allocation.getSuccessors());
            }
        }
    }

    /**
     * 更新前驱任务完成时间列表
     *
     * @param scoreDirector never null
     * @param allocation    never null
     * @return 如果当前任务完成时间发生变化, 为true
     */
    protected boolean updatePredecessorsDoneDate(ScoreDirector<Schedule> scoreDirector, Allocation allocation) {
        // 寻找所有前置任务完成的最晚时间
        LocalDateTime doneDate = LocalDateTime.MIN;
        for (Allocation predecessorAllocation : allocation.getPrecursors()) {
            final LocalDateTime endDateTime = predecessorAllocation.getEndDateTime();
            doneDate = endDateTime.isAfter(doneDate) ? endDateTime : doneDate;
        }
        // 如果和当前相等, 说明没有发生变化
        if (doneDate.equals(allocation.getPrecursorsDoneDate())) {
            return false;
        }
        // 否则, 一定找到了更晚的日期, 需要改变其中的变量, 并发出改变事件
        scoreDirector.beforeVariableChanged(allocation, "precursorsDoneDate");
        allocation.setPrecursorsDoneDate(doneDate);
        scoreDirector.afterVariableChanged(allocation, "precursorsDoneDate");
        return true;
    }
}
