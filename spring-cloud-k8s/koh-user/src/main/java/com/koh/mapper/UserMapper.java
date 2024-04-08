package com.koh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.koh.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
