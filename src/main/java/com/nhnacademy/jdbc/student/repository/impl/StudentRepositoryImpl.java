package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import java.sql.*;
import java.util.Optional;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public int save(Connection connection, Student student){
        //todo#2 학생등록

        String query = "INSERT INTO jdbc_students(id, name, gender, age) VALUES(?,?,?,?)";

        try(
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ){

            preparedStatement.setString(1, student.getId());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setString(3, student.getGender().name());
            preparedStatement.setInt(4, student.getAge());

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("sql exception : {}", e);
        }
        return 0;
    }

    @Override
    public Optional<Student> findById(Connection connection,String id){
        //todo#3 학생조회

        String query = "SELECT * FROM jdbc_students WHERE id = ?";

        try(
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){

            preparedStatement.setString(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
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
            log.error("sql exception : {}", e);
        }

        return Optional.empty();
    }

    @Override
    public int update(Connection connection,Student student){
        //todo#4 학생수정

        String query = "UPDATE jdbc_students SET name = ? , age = ? , gender = ? WHERE id = ?";

        try(
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){

            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(3, student.getGender().name());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setString(4, student.getId());

            return preparedStatement.executeUpdate();


        } catch (SQLException e) {
            log.error("sql exception : {}", e);
        }

        return 0;
    }

    @Override
    public int deleteById(Connection connection,String id){
        //todo#5 학생삭제

        String query = "DELETE FROM jdbc_students WHERE id = ?";

        try(
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){

            preparedStatement.setString(1, id);

            return preparedStatement.executeUpdate();


        } catch (SQLException e) {
            log.error("sql exception : {}", e);
        }

        return 0;
    }

}