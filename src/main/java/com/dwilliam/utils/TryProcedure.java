package com.dwilliam.utils;

@FunctionalInterface
public interface TryProcedure extends TrySupplier<Void> {

    @Override
    default Void get() throws Throwable {
        run();
        return null;
    }

    void run() throws Throwable;

    default TryProcedure then(TryProcedure after){
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