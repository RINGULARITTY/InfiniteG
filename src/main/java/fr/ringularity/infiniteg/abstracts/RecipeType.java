package fr.ringularity.infiniteg.abstracts;

public enum RecipeType {
    NONE("none"),
    ENERGY("energy");

    private final String typeName;

    RecipeType(String typeName) {
        this.typeName = typeName;
    }

    public String typeName() {
        return typeName;
    }
}
