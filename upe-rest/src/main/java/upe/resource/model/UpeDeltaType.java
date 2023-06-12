package upe.resource.model;

public enum UpeDeltaType {
    RB("RB", "ReBuild of the complete process state"),
    VC("VC", "Value Change"),
    AC("AC", "ACtion triggered"),
    CL("CL", "CaLl process"),
    JP("JP", "Jump to Process"),
    FP("FP", "Finish Process"),
    CP("CP", "Cancel Process");

    private String key;
    private String description;

    UpeDeltaType(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public static UpeDeltaType fromKey(String key) {
        for( UpeDeltaType t : UpeDeltaType.values() ) {
            if( t.key.equals(key) ) {
                return t;
            }
        }
        return null;
    }
}
