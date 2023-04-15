package com.example.domain;

import lombok.Data;

/**
 * 资源需求
 */
@Data
public class ResourceRequirement {
    /**
     * 所需要使用的资源
     */
    private Resource resource;
    /**
     * 需要资源的数量
     */
    private int requirement;
    /**
     * 可能存在的资源受限信息 <br>
     * {@link  ExecutionMode#job} - 该资源受限于哪种工序 <br>
     * {@link ExecutionMode#duration} - 该资源的使用时长 <br>
     * {@link ExecutionMode#resourceRequirements} - 无意义 <br>
     */
    private ExecutionMode executionMode;

    public boolean isResourceRenewable() {
        return resource.renewable();
    }
}
