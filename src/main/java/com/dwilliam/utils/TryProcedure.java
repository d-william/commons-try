package com.dwilliam.utils;

@FunctionalInterface
public interface TryProcedure {

    void run();

    default TryProcedure andThen(TryProcedure after){
        return () -> {
            this.run();
            after.run();
        };
    }

    default TryProcedure compose(TryProcedure before){
        return () -> {
            before.run();
            this.run();
        };
    }

}