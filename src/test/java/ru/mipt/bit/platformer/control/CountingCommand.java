package ru.mipt.bit.platformer.control;

class CountingCommand implements Command {

    private int executions;

    @Override
    public void execute() {
        executions++;
    }

    int getExecutions() {
        return executions;
    }
}
