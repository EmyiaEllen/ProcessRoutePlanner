package com.example.domain;

import java.util.Set;

public class GlobalResource extends Resource {

    public GlobalResource(Long id, Set<Worker> workers) {
        super(id, workers);
    }

    @Override
    public boolean renewable() {
        return true;
    }
}
