package com.example.demo.Model;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;


import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class User implements RowMapper<User> {

    private int id;
    private String username;
    private String password;

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        return user;
    }
}
