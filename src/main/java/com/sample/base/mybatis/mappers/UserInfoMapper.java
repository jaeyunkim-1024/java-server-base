package com.sample.base.mybatis.mappers;

import com.sample.base.client.user.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    List<UserInfo> selectUserInfo();
}
