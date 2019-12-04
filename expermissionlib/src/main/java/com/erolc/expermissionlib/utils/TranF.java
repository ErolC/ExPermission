package com.erolc.expermissionlib.utils;

import androidx.core.util.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class TranF{
    public static  <T> Function1<T,Unit> _Unit(Consumer<T> consumer){
        return t -> {
            consumer.accept(t);
            return Unit.INSTANCE;
        };
    }

}
