package com.coderlee.jdk8features.repeatable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderService {
    @Permission("ADMIN")
    @Permission("USER")
    public void deleteOrder() {
        log.info("Executing deleteOrder...");
    }

    @Permission("GUEST")
    public void viewOrder() {
        log.info("Executing viewOrder...");
    }
}
