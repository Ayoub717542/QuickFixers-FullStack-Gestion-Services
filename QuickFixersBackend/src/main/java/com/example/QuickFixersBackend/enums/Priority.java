package com.example.QuickFixersBackend.enums;


public enum Priority {
    BASSE("Basse", 0, 72),
    MOYENNE("Moyenne", 1, 48),
    HAUTE("Haute", 2, 24),
    URGENTE("Urgente", 3, 4);

    private final String label;
    private final int weight;
    private final int slaHeures;

    Priority(String label, int weight, int slaHeures) {
        this.label = label;
        this.weight = weight;
        this.slaHeures = slaHeures;
    }

    public String getLabel() {
        return label;
    }

    public int getWeight() {
        return weight;
    }

    public int getSlaHeures() {
        return slaHeures;
    }
}
