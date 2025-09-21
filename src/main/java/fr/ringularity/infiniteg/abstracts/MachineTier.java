package fr.ringularity.infiniteg.abstracts;

public enum MachineTier {
    BASIC(1),
    IMPROVED(1),
    ADVANCED(3);

    private final int size;

    MachineTier(int size) {
        this.size = size;
    }

    public int size() {
        return size;
    }

    public boolean isCube() {
        return size >= 3;
    }
}
