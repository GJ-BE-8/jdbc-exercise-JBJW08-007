package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

@Slf4j
public class PreparedStatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student){
        //todo#1 학생 등록
        Connection connection = DbUtils.getConnection();
        String sql = "INSERT INTO jdbc_students(id,name,gender,age) VALUES (?,?,?,?)";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender().name());
            statement.setInt(4, student.getAge());

            return  statement.executeUpdate();
        } catch (SQLException e) {
            log.error("e: {}", e.getMessage());
        }


        return 0;
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 학생 조회
        Connection connection = DbUtils.getConnection();
        String sql = "SELECT * FROM jdbc_students WHERE id = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql);

        ){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery();){
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
            log.error("e: {}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int update(Student student){
        //todo#3 학생 수정 , name 수정
        String query = "UPDATE jdbc_students SET name = ?, gender = ?, age = ? WHERE id = ?";
        try (Connection connection = DbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getGender().name());
            preparedStatement.setInt(3, student.getAge());
            preparedStatement.setString(4, student.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating student", e);
        }
        return 0;
    }

    @Override
    public int deleteById(String id){
        //todo#4 학생 삭제
        Connection connection = DbUtils.getConnection();
        String sql = "DELETE FROM jdbc_students WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("e : {}", e.getMessage());
        }
        return 0;
    }

}
