package com.coderlee.jdk8features.repeatable;

import java.lang.annotation.*;

@Repeatable(Permissions.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {
    String value();
}
