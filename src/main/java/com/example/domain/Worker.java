package com.example.domain;

import com.example.AbsEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 工人
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Worker extends AbsEntity {
    private String name;

    public Worker(Long id, String name) {
        super(id);
        this.name = name;
    }
}
