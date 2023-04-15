package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbsEntity {
    private static final Map<Class<? extends AbsEntity>, AtomicLong> autoIdColumnMap = new HashMap<>();

    private Long id;

    public AbsEntity() {
        this.id = autoIdColumnMap
                .computeIfAbsent(this.getClass(), __ -> new AtomicLong())
                .incrementAndGet();
    }

    public AbsEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbsEntity absEntity)) return false;

        return Objects.equals(id, absEntity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
