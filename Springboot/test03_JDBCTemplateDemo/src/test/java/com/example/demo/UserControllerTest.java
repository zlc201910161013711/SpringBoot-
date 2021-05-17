package com.example.demo;

import com.example.demo.Model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void createUserTable () throws Exception {
        System.out.println("start!");
        String sql = "CREATE TABLE `user`(\n" +
                "`id` int(10) not null auto_increment,\n" +
                "`username` varchar(100) default null,\n" +
                "`password` varchar(100) default null,\n" +
                "primary key(`id`)\n" +
                ") engine = innoDB auto_increment = 1 default charset = utf8;\n" +
                "\n";
        jdbcTemplate.execute(sql);
    }

    @Test
    public void saveUserTest() throws Exception {
        String sql = "insert into user(username,password) values ('zlc','123')";
        int rows = jdbcTemplate.update(sql);
        System.out.println(rows);
    }

    @Test
    public void getUserByName () throws Exception {
        String name = "zlc";
        String sql = "select * from user where username = ?";
        List<User> list = jdbcTemplate.query(sql,new User(),new Object[]{name});
        for (User user : list) {
            System.out.println(user);
        }
    }

}
