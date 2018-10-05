package com.gmail.chickenpowerrr.ranksync.api;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
public class BasicProperties implements Properties<BasicProperties> {

    private final Map<String, Object> properties;

    public BasicProperties() {
        this(new HashMap<>());
    }

    public BasicProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public BasicProperties addProperty(String key, int value) {
        this.properties.put(key, value);
        return this;
    }

    @Override
    public BasicProperties addProperty(String key, double value) {
        this.properties.put(key, value);
        return this;
    }

    @Override
    public BasicProperties addProperty(String key, Object value) {
        this.properties.put(key, value);
        return this;
    }

    @Override
    public BasicProperties addProperty(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

    @Override
    public BasicProperties addProperty(String key, long value) {
        this.properties.put(key, value);
        return this;
    }

    @Override
    public double getDouble(String key) {
        return (double) this.properties.get(key);
    }

    @Override
    public int getInt(String key) {
        return (int) this.properties.get(key);
    }

    @Override
    public Object getObject(String key) {
        return this.properties.get(key);
    }

    @Override
    public String getString(String key) {
        return (String) this.properties.get(key);
    }

    @Override
    public long getLong(String key) {
        return (long) this.properties.get(key);
    }

    @Override
    protected Properties clone() {
        return new BasicProperties(new HashMap<>(this.properties));
    }

    @Override
    public boolean has(String key) {
        return this.properties.containsKey(key);
    }

    @Override
    public boolean has(String... keys) {
        for(String key : keys) {
            if(!has(key)) {
                return false;
            }
        }
        return true;
    }
}
