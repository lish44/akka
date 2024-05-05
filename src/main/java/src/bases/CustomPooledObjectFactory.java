package src.bases;

/**
 * {@code @author:} wh
 * {@code @date:} 2024/5/5 23:01
 */
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.function.Supplier;

public class CustomPooledObjectFactory<T> implements PooledObjectFactory<T> {
    private final Supplier<T> supplier;

    public CustomPooledObjectFactory(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public PooledObject<T> makeObject() {
        return new DefaultPooledObject<>(supplier.get());
    }

    @Override
    public void destroyObject(PooledObject<T> pooledObject) {
        // Here you can destroy the object if needed
    }

    @Override
    public boolean validateObject(PooledObject<T> pooledObject) {
        // Here you can validate the object if needed
        return true;
    }

    @Override
    public void activateObject(PooledObject<T> pooledObject) {
        // Here you can activate the object if needed
    }

    @Override
    public void passivateObject(PooledObject<T> pooledObject) {
        // Here you can passivate the object if needed
    }
}
