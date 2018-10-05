package com.gmail.chickenpowerrr.ranksync.api;

public interface Properties<T extends Properties> extends Cloneable {

    T addProperty(String key, Object value);

    T addProperty(String key, String value);

    T addProperty(String key, int value);

    T addProperty(String key, double value);

    T addProperty(String key, long value);

    Object getObject(String key);

    String getString(String key);

    int getInt(String key);

    double getDouble(String key);

    long getLong(String key);

    boolean has(String key);

    boolean has(String... keys);
}
