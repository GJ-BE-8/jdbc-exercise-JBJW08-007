package com.nhnacademy.jdbc.user.repository.impl;

import com.nhnacademy.jdbc.user.domain.User;
import com.nhnacademy.jdbc.user.repository.UserRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Slf4j
public class StatementUserRepository implements UserRepository {

    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
        //todo#1 아이디, 비밀번호가 일치하는 User 조회

        String query = String.format("SELECT * FROM jdbc_users WHERE user_id = '%s' AND user_password = '%s'", userId, userPassword);



        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){

            try(ResultSet resultSet = statement.executeQuery(query);) {
                if (resultSet.next()) {
                    User user = new User(
                            resultSet.getString("user_id"),
                            resultSet.getString("user_name"),
                            resultSet.getString("user_password")
                    );

                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String userId) {
        //#todo#2-아이디로 User 조회
        String query = String.format("SELECT * FROM jdbc_users WHERE user_id ='%s'", userId);

        try(
                Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();
                ){

            try(ResultSet resultSet = statement.executeQuery(query)){
                if (resultSet.next()){
                    User user = new User(
                            resultSet.getString("user_id"),
                            resultSet.getString("user_name"),
                            resultSet.getString("user_password")
                    );
                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return Optional.empty();
    }

    @Override
    public int save(User user) {
        //todo#3- User 저장
        String query = String.format("INSERT INTO jdbc_users VALUES('%s','%s','%s')"
                , user.getUserId()
                , user.getUserName()
                , user.getUserPassword());

        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){
            return statement.executeUpdate(query);

        } catch (SQLException e) {
            log.error("sql exception : {}",e.getMessage());
        }

        return 0;
    }

    @Override
    public int updateUserPasswordByUserId(String userId, String userPassword) {
        //todo#4-User 비밀번호 변경
        String query = String.format("UPDATE jdbc_users SET user_password = '%s' WHERE user_id = '%s'",
                userPassword,
                userId
                );

        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){
            return statement.executeUpdate(query);

        } catch (SQLException e) {
            log.error("sql exception : {}",e.getMessage());
        }

        return 0;
    }

    @Override
    public int deleteByUserId(String userId) {
        //todo#5 - User 삭제
        String query = String.format("DELETE FROM jdbc_users  WHERE user_id = '%s'",
                userId
        );

        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){
            return statement.executeUpdate(query);

        } catch (SQLException e) {
            log.error("sql exception : {}",e.getMessage());
        }
        return 0;
    }

}
