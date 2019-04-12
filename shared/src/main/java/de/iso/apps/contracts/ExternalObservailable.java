package de.iso.apps.contracts;

public interface ExternalObservailable<TKey, TValue> extends Comparable<ExternalObservailable<TKey, TValue>> {
    
    void valueChanged(TKey key, TValue value);
}
