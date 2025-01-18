package com.kaige.service;

import com.kaige.entity.SiteSetting;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface SiteSettingService {

    Map<String, Object> getSiteInfo();

    Map<String, List<SiteSetting>> getList();

    void updateSiteSetting(List<LinkedHashMap> siteSettings, List<BigInteger> deleteIds);

    String getWebTitleSuffix();
}
