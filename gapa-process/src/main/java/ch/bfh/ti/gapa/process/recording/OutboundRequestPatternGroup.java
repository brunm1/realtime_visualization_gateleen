package ch.bfh.ti.gapa.process.recording;

public enum OutboundRequestPatternGroup {
    DATE("date"),
    METHOD("method"),
    URL("url"),
    RECEIVER("receiver");

    private String name;

    OutboundRequestPatternGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
