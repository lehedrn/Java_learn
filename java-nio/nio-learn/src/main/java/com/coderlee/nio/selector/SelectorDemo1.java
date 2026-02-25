package com.coderlee.nio.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class SelectorDemo1 {

    public static void main(String[] args) {
        try (
                Selector selector = Selector.open();
                ServerSocketChannel ssc = ServerSocketChannel.open()
        ) {
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(9999));
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    log.info("accept");
                } else if (key.isConnectable()) {
                    log.info("connect");
                } else if (key.isReadable()) {
                    log.info("read");
                } else if (key.isWritable()) {
                    log.info("write");
                }
                iterator.remove();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
