package com.example.demo.hints2;

public class PluginSettings {
    String mappingFile = "";
    Boolean showMissing  = true;
    Boolean showEmpty  = true;
    Boolean showValidated  = true;
    Boolean validatedColorBlue  = false;
    Boolean displayHintsInline  = true;


    public PluginSettings() {
    }

    public String getMappingFile() {
        return mappingFile;
    }

    public void setMappingFile(String mappingFile) {
        this.mappingFile = mappingFile;
    }

    public Boolean getShowMissing() {
        return showMissing;
    }

    public void setShowMissing(Boolean showMissing) {
        this.showMissing = showMissing;
    }

    public Boolean getShowEmpty() {
        return showEmpty;
    }

    public void setShowEmpty(Boolean showEmpty) {
        this.showEmpty = showEmpty;
    }

    public Boolean getShowValidated() {
        return showValidated;
    }

    public void setShowValidated(Boolean showValidated) {
        this.showValidated = showValidated;
    }

    public Boolean getValidatedColorBlue() {
        return validatedColorBlue;
    }

    public void setValidatedColorBlue(Boolean validatedColorBlue) {
        this.validatedColorBlue = validatedColorBlue;
    }

    public Boolean getDisplayHintsInline() {
        return displayHintsInline;
    }

    public void setDisplayHintsInline(Boolean displayHintsInline) {
        this.displayHintsInline = displayHintsInline;
    }
}
