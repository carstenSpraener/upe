package upe.common;

import upe.process.UProcessComponent;

import java.util.List;
import java.util.function.Supplier;

public class MasterDetailConfiguration {
    private String masterListPath;
    private String detailProcessName;
    private String selectionIdentifier;
    private Supplier<List<?>> dataSupplier;

    public MasterDetailConfiguration(String masterListPath, String detailProcessName, String selectionIdentifier) {
        this.masterListPath = masterListPath;
        this.detailProcessName = detailProcessName;
        this.selectionIdentifier = selectionIdentifier;
    }

    public MasterDetailConfiguration withDataSupplier(Supplier<List<?>> dataSupplier) {
        this.dataSupplier = dataSupplier;
        return this;
    }

    public String getMasterListPath() {
        return masterListPath;
    }

    public String getDetailProcessName() {
        return detailProcessName;
    }

    public String getSelectionIdentifier() {
        return selectionIdentifier;
    }

    public Supplier<List<?>> getDataSupplier() {
        return dataSupplier;
    }
}
