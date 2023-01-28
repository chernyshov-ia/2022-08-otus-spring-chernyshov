package ru.otus.domain;

import lombok.Getter;

@Getter
public class Task {
    private Instruction instruction;
    private String params;

    public Task(Instruction instruction, String params) {
        this.instruction = instruction;
        this.params = params;
    }

    @Override
    public String toString() {
        return "Task@" + hashCode() + "  { " + instruction + " -> " + params + " }";
    }
}
