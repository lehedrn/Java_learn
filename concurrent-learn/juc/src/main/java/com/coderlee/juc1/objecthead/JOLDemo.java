package com.coderlee.juc1.objecthead;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

@Slf4j
public class JOLDemo {
    public static void main(String[] args) {

        log.info("vm deatils: {}", VM.current().details());

        Object obj = new Object();
        log.info("obj: {}", ClassLayout.parseInstance(obj).toPrintable());
        Customer1 c1 = new Customer1();
        log.info("c1: {}", ClassLayout.parseInstance(c1).toPrintable());
        Customer c = new Customer();
        log.info("c: {}", ClassLayout.parseInstance(c).toPrintable());
    }
}

class Customer1 {}

class Customer {
    boolean flag = false;
    long id;
    int age;
    String name;
}
