package com.example.hustory.reservation;

public class CardItem {
    private String id;
    private String NameStr;
    private String VersionStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameStr() {
        return NameStr;
    }

    public void setNameStr(String nameStr) {
        NameStr = nameStr;
    }

    public String getVersionStr() {
        return VersionStr;
    }

    public void setVersionStr(String versionStr) {
        VersionStr = versionStr;
    }

    public CardItem(String id, String nameStr, String versionStr) {
        this.id = id;
        NameStr = nameStr;
        VersionStr = versionStr;
    }
}
