public enum Jobs {
    CEO("CEO"),
    MANAGER("MANAGER"),
    ACCOUNTING("ACCOUNTING"),
    MARKETING("MARKETING"),
    QUALITY_CONTROL("QUALITY_CONTROL"),
    RECEPTIONIST("RECEPTIONIST"),
    NO_POSITION("");

    private final String label;

    Jobs(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
