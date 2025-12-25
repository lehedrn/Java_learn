package com.coderlee.jdk8features.lambda;

@FunctionalInterface
public interface MyFun<T> {

    T getValue(T t);
}
