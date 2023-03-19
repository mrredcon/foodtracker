package edu.utsa.cs3443.anw198.foodtracker;

public interface APIListener<T> {
    void onResponse(T result);
    void onFailure(Throwable error);
}
