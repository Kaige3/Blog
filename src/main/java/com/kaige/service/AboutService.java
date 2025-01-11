package com.kaige.service;

import java.util.Map;

public interface AboutService {
    boolean getAboutCommentEnabled();

    Map<String,String> getAboutInfo();

    void updateAbout(Map<String, String> map);
}
