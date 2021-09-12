package interfaces;

import java.io.IOException;
import java.io.Serializable;

public interface SerializeConversor<S extends Serializable> {
    byte[] serialize(S obj) throws IOException;

    S deserialize(byte[] data) throws IOException;
}
