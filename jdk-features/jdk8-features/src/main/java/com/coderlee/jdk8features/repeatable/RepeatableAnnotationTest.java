package com.coderlee.jdk8features.repeatable;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Method;

@Slf4j
public class RepeatableAnnotationTest {
    @Test
    public void testRepeatableAnnotations() throws Exception {
        Class<OrderService> clazz = OrderService.class;

        for (Method method : clazz.getDeclaredMethods()) {
            log.info("Method: {}", method.getName());

            // 5️⃣ 通过 getAnnotationsByType 获取重复注解
            Permission[] permissions = method.getAnnotationsByType(Permission.class);

            if (permissions.length == 0) {
                log.info("  No permissions assigned.");
            } else {
                for (Permission p : permissions) {
                    log.info("  Permission: {}", p.value());
                }
            }

            // 6️⃣ 验证容器注解也存在（编译器生成）
            Permissions container = method.getAnnotation(Permissions.class);
            if (container != null) {
                log.info("  Container annotation contains {} permissions.", container.value().length);
            }
        }
    }
}
