package ch.bfh.ti.gapa.process;

public interface AsyncTaskHandler<T> {
    void onResult(T result);
    void onError(Throwable err);
}
