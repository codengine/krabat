package de.codengine.krabat.anims;

public enum DirectionY {
    UP(-1), DOWN(1);

    private final int val;

    DirectionY(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
