package de.codengine.krabat.anims;

public enum DirectionX {
    LEFT(-1), RIGHT(1);

    private final int val;

    DirectionX(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
