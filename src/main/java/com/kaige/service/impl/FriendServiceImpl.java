package com.kaige.service.impl;

import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.*;
import com.kaige.entity.dto.FriendInput;
import com.kaige.entity.vo.FriendInfoVo;
import com.kaige.handler.exception.PersistenceException;
import com.kaige.repository.FriendRepository;
import com.kaige.service.FriendService;
import com.kaige.service.RedisService;
import com.kaige.utils.markdown.MarkdownUtils;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private FriendRepository friendRepository;
    private final JSqlClient sqlClient;

    public FriendServiceImpl(JSqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public List<Friend> getFriendList() {
         FriendTable friend = FriendTable.$;
        return sqlClient.createQuery(friend)
                .select(friend)
                .execute();
    }

    @Override
    public FriendInfoVo getSiteFriendInfo() {
        SiteSettingTable siteSettingTable = SiteSettingTable.$;
//        从redis中获取
        String redisKey = RedisKeyConstants.FRIEND_INFO_MAP;
        FriendInfoVo friendInfoVo = redisService.getObjectByValue(redisKey, FriendInfoVo.class);
        if(friendInfoVo!= null){
            return friendInfoVo;
        }
//       redis中没有，从数据库中获取
        List<SiteSetting> settingListOfFriend = sqlClient.createQuery(siteSettingTable)
                .where(siteSettingTable.type().eq(4))
                .select(siteSettingTable.fetch(
                        SiteSettingFetcher.$
                                .nameEn()
                                .value()
                ))
                .execute();
        FriendInfoVo friendInfoVo1 = new FriendInfoVo();
        for (SiteSetting setting : settingListOfFriend) {
            if("friendContent".equals(setting.nameEn())){
                friendInfoVo1.setContent(MarkdownUtils.markdownToHtmlExtensions(setting.value()));
            }
            else if("friendCommentEnabled".equals(setting.nameEn())){
                friendInfoVo1.setCommentEnabled(true);
            }
        }
        redisService.saveObjectToValue(redisKey,friendInfoVo1);
        return friendInfoVo1;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addViewsByNickname(String nickname) {
        FriendTable friend = FriendTable.$;
        Integer execute = sqlClient.createUpdate(friend)
                .where(friend.nickname().eq(nickname))
                .set(friend.views(), friend.views().plus(1))
                .execute();

        if(execute != 1){
            throw new PersistenceException("操作失败");
        }
    }

    @Override
    public Page<Friend> getFriendListOfPage(Integer pageNum, Integer pageSize) {
       return friendRepository.getFriendListOfPage(pageNum, pageSize);
    }

    @Override
    public void updateFriend(BigInteger id, Boolean isPublic) {
        try {
            friendRepository.updateById(id,isPublic);
        } catch (Exception e) {
            throw new PersistenceException("操作失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFriend(FriendInput friendInput) {
        friendInput.setViews(0);
        friendInput.setCreateTime(LocalDateTime.now());
        try {
            friendRepository.save(friendInput);
        } catch (Exception e) {
            throw new PersistenceException("保存友联失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFriendInfo(FriendInput friendInput) {
        try {
            friendRepository.update(friendInput);
        } catch (Exception e) {
            throw new PersistenceException("更新友联失败");
        }
    }

    @Override
    public void deleteFriend(BigInteger id) {
        try {
            friendRepository.deleteById(id);
        } catch (Exception e) {
            throw new PersistenceException("删除友联失败");
        }
    }

    @Override
    public void updateCommentEnabled(Boolean commentEnabled) {
        try {
            friendRepository.updateCommentEnabled(commentEnabled);
        } catch (Exception e) {
            throw new PersistenceException("更新友联失败");
        }
        deleteFriendRedisCache();
    }

    @Override
    public void updateFriendInfoContent(String content) {
        try {
            friendRepository.updateFriendInfoContent(content);
        } catch (Exception e) {
            throw new PersistenceException("更新友联失败");
        }
        deleteFriendRedisCache();
    }

    private void deleteFriendRedisCache() {
        redisService.deleteCacheByKey(RedisKeyConstants.FRIEND_INFO_MAP);
    }

}
