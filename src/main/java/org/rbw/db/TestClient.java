package org.rbw.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * conf without xml file
 * what the fuck xml file ugly and stupid
 *
 * @author renbowen
 * @version 2020-05-05
 */
public class TestClient {
    private static final Logger log = LoggerFactory.getLogger(TestClient.class);

    public static void main(String[] args) {
        TestClient cli = new TestClient();

        Map<String, Object> map = new HashMap<>();
        map.put("name", "JFK");
        map.put("age", 38);
        map.put("gender", "male");

        /*People p = new People();
        p.setId(5);
        p.setName("eva");
        p.setAge(17);
        p.setGender("female");*/

        cli.insertOne(map);
        // System.out.println(cli.listAll("e"));


    }

    /**
     * get data source
     * @return the hikari cp data source
     */
    private static DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setPoolName("sqlite3 pool");
        config.setJdbcUrl("jdbc:sqlite:./test1.db"); // dot means relative path under app dir
        return new HikariDataSource(config);
    }

    /**
     * mybatis is work with the core sql session factory
     * so we need to init it with tx mgr and data source
     *
     * @return sql session factory
     */
    private static SqlSessionFactory getSqlSessionFactory() {

        final DataSource ds = getDataSource();
        JdbcTransactionFactory jtf = new JdbcTransactionFactory();
        Environment env = new Environment("dev", jtf, ds);
        Configuration conf = new Configuration(env);
        // conf.addMapper(PeopleMapper.class);
        conf.addMappers("org.rbw.db");

        return new SqlSessionFactoryBuilder().build(conf);
    }

    private Map<String, Object> queryById(final int id) {
        try (SqlSession session = getSqlSessionFactory().openSession()) {
            PeopleMapper mapper = session.getMapper(PeopleMapper.class);
            final Map<String, Object> people = mapper.queryById(id);
            System.out.println(people);
            return people;
        }
    }

    private void insertOne(People row) {
        try (SqlSession session = getSqlSessionFactory().openSession()) {
            PeopleMapper mapper = session.getMapper(PeopleMapper.class);
            mapper.inertOne(row);
            System.out.println("what the fuck");
            // int i = 5 / 0; // in order to test transaction is worked
            session.commit();
        }
    }

    private List<Map<String, Object>> listAll(final String query) {
        try (SqlSession session = getSqlSessionFactory().openSession()) {
            PeopleMapper mapper = session.getMapper(PeopleMapper.class);

            HashMap<String, String> map = new HashMap<>() {{
                put("subName", query);
            }};

            return mapper.listAll(map);
        }
    }

    /**
     * according to the result the pk auto increase is based in the max id
     * and if use map as the param,
     * each key must be the same wit the <b>#{}<b/> inside val
     * @param row map
     */
    private void insertOne(Map<String, Object> row) {
        try (SqlSession session = getSqlSessionFactory().openSession()) {
            PeopleMapper mapper = session.getMapper(PeopleMapper.class);
            mapper.inertOneWithMap(row);
            System.out.println("what the fuck");
            // int i = 5 / 0; // in order to test transaction is worked
            session.commit();
        }
    }

}
