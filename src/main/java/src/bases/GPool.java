package src.bases;

import java.util.function.Supplier;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * {@code @author:} wh
 * {@code @date:} 2024/5/5 22:59
 */
public class GPool<T> {
    private final GenericObjectPool<T> pool;

    public GPool(Supplier<T> supplier) {
        GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(100);
        this.pool = new GenericObjectPool<>(new CustomPooledObjectFactory<>(supplier), config);
    }

    public T get() throws Exception {
        return pool.borrowObject();
    }

    public void put(T obj) {
        pool.returnObject(obj);
    }
}
