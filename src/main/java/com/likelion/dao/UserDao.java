package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    RowMapper<User> rowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getString("id"),rs.getString("name"),rs.getString("password"));
            return user;
        }
    };

    public void add(User user) throws SQLException {
        this.jdbcTemplate.update("INSERT INTO users(id, name, password) VALUES(?, ?, ?);",
                user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        return this.jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void deleteAll() throws SQLException {
        this.jdbcTemplate.update("DELETE FROM users");
    }

    public int getCount() throws SQLException {
        String sql = "SELECT count(*) FROM users;";
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<User> getAll(){
        String sql = "SELECT * FROM users ORDER BY id;";
        return this.jdbcTemplate.query(sql, rowMapper);
    }
}
