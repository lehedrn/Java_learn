package com.coderlee.concurrent.learn.lab01;

/**
 * 有问题的单例模式
 */
public class SingleInstance {
    private static SingleInstance instance;

    public static SingleInstance getInstance(){
        if(instance == null){
            synchronized (SingleInstance.class){
                if(instance == null){
                    instance = new SingleInstance();
                }
            }
        }
        return instance;
    }
}
