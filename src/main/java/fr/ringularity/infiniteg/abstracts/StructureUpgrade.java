package fr.ringularity.infiniteg.abstracts;

public enum StructureUpgrade {
    NONE("none"),
    ENERGY("energy");

    private final String typeName;

    StructureUpgrade(String typeName) {
        this.typeName = typeName;
    }

    public String typeName() {
        return typeName;
    }
}
