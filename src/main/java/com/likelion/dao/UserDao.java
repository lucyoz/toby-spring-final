package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;

public class UserDao {
    private DataSource dataSource;
    private JdbcContext jdbcContext;
    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext(dataSource);
    }


    public void add(User user) throws SQLException {
        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO users(id, name, password) VALUES(?,?,?);");
                ps.setString(1,user.getId());
                ps.setString(2,user.getName());
                ps.setString(3,user.getPassword());
                return ps;
            }
        });
    }

    public User get(String id) throws SQLException {
        Map<String, String> env = System.getenv();
        Connection c = DriverManager.getConnection(env.get("DB_HOST"),
                env.get("DB_USER"),env.get("DB_PASSWORD"));
        PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE id=?;");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        User user = null;

        if(rs.next()){
            user = new User(rs.getString("id"),rs.getString("name"),rs.getString("password"));
        }
        rs.close();
        ps.close();
        c.close();
        if(user==null)  throw new EmptyResultDataAccessException(1);
        return user;
    }

    public void deleteAll() throws SQLException {
        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("DELETE FROM users");
            }
        });

    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("SELECT count(*) FROM users");
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(rs!=null){
                try{
                    rs.close();
                }catch (SQLException e){
                }
            }
            if(ps!=null){
                try {
                    ps.close();
                }catch (SQLException e){
                }
            }
            if(c!=null){
                try{
                    c.close();
                }catch (SQLException e){
                }
            }
        }
    }
}
