package dev.snowcave.guilds.core.data;

import java.io.IOException;

/**
 * Created by WinterBear on 11/11/2023.
 */
public class StorageControllerIOException extends IOException {

    public StorageControllerIOException(String message, Throwable cause){
        super(message, cause);
    }

}
