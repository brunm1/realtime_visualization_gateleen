package ch.bfh.ti.gapa.process.recording;

public enum InboundRequestPatternGroup{
    DATE("date"),
    METHOD("method"),
    URL("url"),
    SENDER("sender");

    private String name;

    InboundRequestPatternGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
