package com.example.domain;

import com.example.AbsEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 项目/订单
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Project extends AbsEntity {
    private String name;
    private String customer;
    private Long count;
    private LocalDateTime deadline;

    public Project(Long id, String name, String customer, Long count, LocalDateTime deadline) {
        super(id);
        this.name = name;
        this.customer = customer;
        this.count = count;
        this.deadline = deadline;
    }
}
