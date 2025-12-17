package com.coderlee.juc1.cas;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * AtomicStampedReference使用示例
 * 演示如何使用带时间戳的原子引用来避免CAS操作中的ABA问题
 */
@Slf4j
public class AtomicStampedDemo {
    public static void main(String[] args) {
        // 创建初始书籍对象
        Book javaBook = Book.builder().id(1).name("Java").build();

        // 创建带时间戳的原子引用，初始引用为javaBook，时间戳为1
        AtomicStampedReference<Book> stampedReference = new AtomicStampedReference<>(javaBook, 1);

        // 输出初始状态：引用对象和时间戳
        log.info("reference: {}, stamp: {}", stampedReference.getReference(), stampedReference.getStamp());

        // 创建另一个书籍对象
        Book pythonBook = Book.builder().id(2).name("Python").build();
        boolean b;

        // 执行第一次CAS操作：将javaBook替换为pythonBook，同时将时间戳从1更新为2
        b = stampedReference.compareAndSet(javaBook, pythonBook, stampedReference.getStamp(), stampedReference.getStamp() + 1);

        // 输出操作结果：新的引用对象、时间戳和操作是否成功
        log.info("reference: {}, stamp: {}, compareAndSet: {}", stampedReference.getReference(), stampedReference.getStamp(), b);

        // 执行第二次CAS操作：将pythonBook替换回javaBook，同时将时间戳从2更新为3
        b = stampedReference.compareAndSet(pythonBook, javaBook, stampedReference.getStamp(), stampedReference.getStamp() + 1);

        // 输出最终结果：引用对象、时间戳和操作是否成功
        log.info("reference: {}, stamp: {}, compareAndSet: {}", stampedReference.getReference(), stampedReference.getStamp(), b);
    }
}

/**
 * 书籍实体类
 * 使用Lombok注解简化代码
 */
@Data
@Builder
class Book {
    private int id;      // 书籍ID
    private String name; // 书籍名称
}
