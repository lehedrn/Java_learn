package com.coderlee.nio.path;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class PathDemo {
    public static void main(String[] args) {
        String projectRoot = System.getProperty("user.dir");
        String filePath = projectRoot + "/java-nio/nio-learn/src/main/resources/file6.text";
        Path path = Paths.get(filePath);
        log.info("path: {}", path);

        Path path2 = Paths.get(projectRoot, "java-nio", "nio-learn", "src", "main", "resources", "file6.text");
        log.info("path2: {}", path2);

        String originalPath = "/home/coderlee/../abc.text";
        Path path3 = Paths.get(originalPath);
        log.info("originalPath: {}", path3);
        Path path4 = path3.normalize();
        log.info("normalizePath: {}", path4);
    }
}
