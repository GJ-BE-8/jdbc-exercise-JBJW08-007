package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class StatementStudentRepository implements StudentRepository {


    @Override
    public int save(Student student){
        //todo#1 insert student
        Connection connection = DbUtils.getConnection();
        String sql = String.format("INSERT INTO jdbc_students(id ,name, gender, age) VALUES ('%s', '%s', '%s', '%s')"
                , student.getId()
                , student.getName()
                , student.getGender().name()
                , student.getAge());
        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("e : {}", e.getMessage());
        }
        return 0;
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 student 조회

        String sql = String.format("SELECT * FROM jdbc_students WHERE id = '%s'", id);

        try (Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();){

            try(ResultSet resultSet = statement.executeQuery(sql);) {
                while (resultSet.next()) {
                    Student student = new Student(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            Student.GENDER.valueOf(resultSet.getString("gender")),
                            resultSet.getInt("age"),
                            resultSet.getTimestamp("created_at").toLocalDateTime()
                    );

                    return Optional.of(student);

                }
            }
        } catch (SQLException e) {
            log.error("e : {}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int update(Student student){
        //todo#3 student 수정, name <- 수정합니다.
        Connection connection = DbUtils.getConnection();
        String sql = String.format("UPDATE jdbc_students SET name = '%s', age = '%s', gender='%s' WHERE id =  '%s'",
                student.getName(),
                student.getAge(),
                student.getGender().name(),
                student.getId());

        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("e : {}", e.getMessage());
        }
        return 0;
    }

    @Override
    public int deleteById(String id){
        //todo#4 student 삭제
        Connection connection = DbUtils.getConnection();
        String sql = String.format("DELETE FROM jdbc_students WHERE id = '%s'", id);
        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("e : {}", e.getMessage());
        }

        return 0;
    }

}
