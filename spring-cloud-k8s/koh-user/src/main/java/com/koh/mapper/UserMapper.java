package com.koh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.koh.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
