package org.rbw.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * people 查询类
 */
public interface PeopleMapper {

    @Select("")
    List<Map<String, Object>> listAll();

    @Select("Select * from t_people where id = #{id}")
    Map<String, Object> queryById(int id);

    @Insert("insert into t_people values (#{id}, #{name}, #{age}, #{gender})")
    void inertOne(People row);

}
