package com.rover12421.shaka.lib.util;

public class PublicXmlParamer {
    public int id;
    public String type;
    public String old_name;
    public String new_name;
    public String path;
    public String ext;
    public String orgFilepath;
    public String renameFilepath;

    @Override
    public String toString() {
        return
                "id=" + id +
                ", type='" + type + '\'' +
                ", old_name='" + old_name + '\'' +
                ", new_name='" + new_name + '\'' +
                ", path='" + path + '\'';

    }
}
