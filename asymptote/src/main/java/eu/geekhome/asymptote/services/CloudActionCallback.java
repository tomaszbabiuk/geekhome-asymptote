package eu.geekhome.asymptote.services;

public interface CloudActionCallback<T> {
    void success(T data);
    void failure(CloudException exception);
}
