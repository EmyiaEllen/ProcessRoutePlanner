package com.example.domain;

import com.example.AbsEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * 资源/同一类型的员工
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class Resource extends AbsEntity {
    private Set<Worker> workers;

    public Resource(Long id, Set<Worker> workers) {
        super(id);
        this.workers = workers;
    }

    public abstract boolean renewable();
}
