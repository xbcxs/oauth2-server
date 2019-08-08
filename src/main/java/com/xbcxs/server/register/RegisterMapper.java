package com.xbcxs.server.register;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaosh on 2019/8/8.
 */
@Mapper
public interface RegisterMapper {

    int insert(Register record);

    int insertSelective(Register record);

    Register selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Register record);

    int updateByPrimaryKey(Register record);

    List<Register> selectRegister();

    List<Map> selectAll(Map parameterMap);
}
