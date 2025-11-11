package com.coderlee.concurrent.design.active.object.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class URLTransfer {
    private static final String URL_SPLIT_URL = "//";
    public static String generateShortLink(String longLink) {
        if (null == longLink || longLink.length() <= 0) {
            return null;
        }
        String urlPrefix = "";
        String schema = getSchema(longLink);
        if (null != schema && schema.length() > 0) {
            urlPrefix = schema.concat(URL_SPLIT_URL);
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(longLink.getBytes());
            byte[] hash = md5.digest();
            StringBuilder shortLink = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                shortLink.append(Integer.toString((hash[i] & 0xFF) + 0x100, 16).substring(1));
            }
            return urlPrefix.concat(shortLink.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getSchema(String longLink) {
        String schema = "";
        if (!longLink.contains(URL_SPLIT_URL)) {
            return schema;
        }
        return longLink.split(URL_SPLIT_URL)[0];
    }
}
