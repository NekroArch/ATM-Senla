package org.example.userio;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum Command {
    BALANCE(1),
    WITHDRAW(2),
    DEPOSIT(3),
    EXIT(0);

    public int getId() {
        return id;
    }

    private final int id;
    private static final Map<Integer, Command> idToCommand = stream(values())
            .collect(toMap(Command::getId, identity()));

    public static Command of(int id){
        return idToCommand.get(id);
    }

    Command(int id) {
        this.id = id;
    }
}
