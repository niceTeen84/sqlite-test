package org.rbw.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * people 查询类
 */
public interface PeopleMapper {

    @Select("select * from t_people where name like '%${subName}%'")
    List<Map<String, Object>> listAll(Map<String, String> param);

    @Select("Select * from t_people where id = #{id}")
    Map<String, Object> queryById(int id);

    @Insert("insert into t_people values (#{id}, #{name}, #{age}, #{gender})")
    void inertOne(People row);

    /**
     * test map param and test if the db pk can increase automatic
     * @param row map
     */
    @Insert("Insert into t_people (name, age, gender) values (#{name},#{age}, #{gender})")
    void inertOneWithMap(Map<String, Object> row);

}
