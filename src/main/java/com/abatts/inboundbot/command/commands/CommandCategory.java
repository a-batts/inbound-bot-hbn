package com.abatts.inboundbot.command.commands;

public enum CommandCategory {
    ADMIN(0), BASE(1), HBN(2), MUSIC(3), PW(4);

    private final int category;

    CommandCategory(int category) {
        this.category = category;
    }

    public int getCategoryInt(){
        return category;
    }
}
