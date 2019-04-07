package de.iso.apps.contracts;

public interface ExternalObserver<TKey, TValue> {
    void add(ExternalObservailable<TKey, TValue> observailable);
}
