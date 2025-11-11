package com.coderlee.concurrent.design.active.object.wrong;

public interface URLService {
    String getShortUrl(String longUrl);
    void saveUrl(String shortUrl, String longUrl);
    String getLongUrlByShortUrl(String shortUrl);
}
