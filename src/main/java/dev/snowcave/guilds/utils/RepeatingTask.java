package dev.snowcave.guilds.utils;

import java.util.concurrent.Callable;

/**
 * Created by WinterBear on 09/11/2023.
 */
public class RepeatingTask implements Callable<Boolean> {

    private final Callable<Boolean> target;

    private final String reference;

    public RepeatingTask(String reference, Callable<Boolean> target) {
        this.target = target;
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public Boolean call() throws Exception {
        return target.call();
    }
}
