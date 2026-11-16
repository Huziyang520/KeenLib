package com.huziyang520.keenlib.platform.services;

/**
 * Loader-neutral view of the values behind one registered config spec, so callers never touch a loader type.
 *
 * <p>Reads return the fallback and writes are ignored until the loader has loaded the file.
 */
public interface SpecBinding {

    boolean has(String key);

    boolean getBoolean(String key, boolean fallback);

    int getInt(String key, int fallback);

    double getDouble(String key, double fallback);

    String getString(String key, String fallback);

    void set(String key, Object value);

    boolean save();
}
