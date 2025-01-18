package com.kaige.service.impl;

import com.kaige.constant.RedisKeyConstants;
import com.kaige.constant.SiteSettingConstants;
import com.kaige.entity.SiteSetting;
import com.kaige.entity.vo.Badge;
import com.kaige.entity.vo.Copyright;
import com.kaige.entity.vo.Favorite;
import com.kaige.entity.vo.Introduction;
import com.kaige.repository.SiteSettingRepository;
import com.kaige.service.RedisService;
import com.kaige.service.SiteSettingService;
import com.kaige.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SiteSettingServiceImpl implements SiteSettingService {

    @Autowired
    private SiteSettingRepository siteSettingRepository;
    @Autowired
    private RedisService redisService;

    //它会匹配被双引号包围的任意字符
    private static final Pattern PATTERN = Pattern.compile("\"(.*?)\"");
    @Override
    public Map<String, Object> getSiteInfo() {
        // 先从redis中查询
        String siteInfoMapKey = RedisKeyConstants.SITE_INFO_MAP;
        Map<String, Object> siteInfoMapFromRedis = redisService.getMapByValue(siteInfoMapKey);
        if (siteInfoMapFromRedis!= null){
            return siteInfoMapFromRedis;
        }
        // 否则从数据库中查询
        // 1.全表查询 siteSetting
        List<SiteSetting> siteSettingList = siteSettingRepository.getList();
        HashMap<String, Object> siteInfo = new HashMap<>(2);
        //徽章Vo
        List<Badge> badges = new ArrayList<>();
        //个人介绍Vo
        Introduction introduction = new Introduction();
        //爱好Vo
        List<Favorite> favorites = new ArrayList<>();
        // 滚动文字List Vo
        List<String> rollTests = new ArrayList<>();
        for (SiteSetting siteSetting :siteSettingList) {
            switch (siteSetting.type()){
                case 1->{
                    if (SiteSettingConstants.COPYRIGHT.equals(siteSetting.nameEn())){
                        // copyright对应的value 是json格式
                        //将json转为Java对象
                        Copyright copyright = JacksonUtils.readValue(siteSetting.value(), Copyright.class);
                        siteInfo.put(siteSetting.nameEn(),copyright);
                    }else {//对于基本数据类型（String,int）不需要进行 jackson 转换
                        siteInfo.put(siteSetting.nameEn(),siteSetting.value());
                    }
                }
                case 2->{
                    switch (siteSetting.nameEn()){
                        case SiteSettingConstants.AVATAR -> introduction.setAvatar(siteSetting.value());
                        case SiteSettingConstants.NAME -> introduction.setName(siteSetting.value());
                        case SiteSettingConstants.GITHUB -> introduction.setGithub(siteSetting.value());
                        case SiteSettingConstants.TELEGRAM -> introduction.setTelegram(siteSetting.value());
                        case SiteSettingConstants.QQ -> introduction.setQq(siteSetting.value());
                        case SiteSettingConstants.BILIBILI -> introduction.setBilibili(siteSetting.value());
                        case SiteSettingConstants.NETEASE -> introduction.setNetease(siteSetting.value());
                        case SiteSettingConstants.EMAIL -> introduction.setEmail(siteSetting.value());
                        case SiteSettingConstants.FAVORITE -> {
                            Favorite favorite = JacksonUtils.readValue(siteSetting.value(), Favorite.class);
                            favorites.add(favorite);
                        }
                        case SiteSettingConstants.ROLL_TEXT -> {
                            Matcher matcher = PATTERN.matcher(siteSetting.value());
                            while (matcher.find()){
                                rollTests.add(matcher.group(1));
                            }
                        }
                    }
                }
                case 3->{
                    Badge badge = JacksonUtils.readValue(siteSetting.value(), Badge.class);
                    badges.add(badge);
                }
            }
        }
        introduction.setFavorites(favorites);
        introduction.setRollText(rollTests);
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("introduction",introduction);
        map.put("siteInfo",siteInfo);
        map.put("badges",badges);
        redisService.saveMapToValue(siteInfoMapKey,map);
        return map;
    }

    @Override
    public Map<String, List<SiteSetting>> getList() {
        List<SiteSetting> list = siteSettingRepository.getList();
        List<SiteSetting> type1 = new ArrayList<>();
        List<SiteSetting> type2 = new ArrayList<>();
        List<SiteSetting> type3 = new ArrayList<>();
        for (SiteSetting siteSetting : list) {
            switch (siteSetting.type()){
                case 1->type1.add(siteSetting);
                case 2->type2.add(siteSetting);
                case 3->type3.add(siteSetting);
            }
        }
        HashMap<String, List<SiteSetting>> map = new HashMap<>(8);
        map.put("type1",type1);
        map.put("type2",type2);
        map.put("type3",type3);
        return map;
    }

    @Override
    public void updateSiteSetting(List<LinkedHashMap> siteSettings, List<BigInteger> deleteIds) {
        if (deleteIds!=null){
            for(BigInteger id:deleteIds){
                //删除
                siteSettingRepository.deleteSiteSettingInfoById(id);
            }
        }

        for (LinkedHashMap siteSetting : siteSettings) {
            //保存或者更新
            SiteSetting siteSetting1 = JacksonUtils.readValue(JacksonUtils.writeValueAsString(siteSetting), SiteSetting.class);
            if (siteSetting1.id()!=null){
                siteSettingRepository.update(siteSetting1);
                continue;
            }else {
                siteSettingRepository.save(siteSetting1);
            }
        }
        deleteSiteInfoRedisCache();
    }

    @Override
    public String getWebTitleSuffix() {
        return siteSettingRepository.getWebTitleSuffix();
    }

    private void deleteSiteInfoRedisCache() {
        redisService.deleteCacheByKey(RedisKeyConstants.SITE_INFO_MAP);
    }
}
