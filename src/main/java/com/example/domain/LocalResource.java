package com.example.domain;

import java.util.Set;

/**
 * 局部资源/项目专属员工组
 */
public class LocalResource extends Resource {

    /**
     * 该员工组专属于某个项目, 其他项目不能使用
     */
    private Project project;
    private boolean renewable;

    public LocalResource(Long id, Set<Worker> workers) {
        super(id, workers);
    }

    public LocalResource(Long id, Set<Worker> workers, Project project, boolean renewable) {
        super(id, workers);
        this.project = project;
        this.renewable = renewable;
    }

    @Override
    public boolean renewable() {
        return renewable;
    }
}
