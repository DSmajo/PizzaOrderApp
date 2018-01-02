package app.fit.ba.vjezbanjeib130011.helper;

import java.io.Serializable;

/**
 * Created by HOME on 20.10.2016.
 */

public interface MyRunnable<T> extends Serializable
{
    void  run(T t);
}
