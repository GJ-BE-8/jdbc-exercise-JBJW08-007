package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.Club;
import com.nhnacademy.jdbc.club.domain.ClubStudent;
import com.nhnacademy.jdbc.club.repository.ClubRegistrationRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class ClubRegistrationRepositoryImpl implements ClubRegistrationRepository {

    @Override
    public int save(Connection connection, String studentId, String clubId) {
        //todo#11 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환
        String query = "INSERT INTO jdbc_club_registrations VALUES (?,?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1,studentId);
            preparedStatement.setString(2,clubId);

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByStudentIdAndClubId(Connection connection, String studentId, String clubId) {
        //todo#12 - 핵생 -> 클럽 탈퇴, executeUpdate() 결과를 반환
        String query = "DELETE FROM jdbc_club_registrations WHERE student_id = ? AND club_id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1,studentId);
            preparedStatement.setString(2,clubId);

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClubStudent> findClubStudentsByStudentId(Connection connection, String studentId) {
        //todo#13 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환
        String query = "select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a inner join jdbc_club_registrations b on a.id=b.student_id inner join jdbc_club c on b.club_id=c.club_id where a.id=?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1,studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClubStudent> clubStudents = new ArrayList<>();
            while (resultSet.next()){
                ClubStudent clubStudent = new ClubStudent(
                        resultSet.getString("student_id"),
                        resultSet.getString("student_name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                );
                clubStudents.add(clubStudent);
            }
            return clubStudents;

        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public List<ClubStudent> findClubStudents(Connection connection) {
        //todo#21 - join
        String query = "select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a inner join jdbc_club_registrations b on a.id=b.student_id inner join jdbc_club c on b.club_id=c.club_id order by a.id asc, b.club_id asc";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClubStudent> clubStudents = new ArrayList<>();
            while (resultSet.next()){
                ClubStudent clubStudent = new ClubStudent(
                        resultSet.getString("student_id"),
                        resultSet.getString("student_name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                );
                clubStudents.add(clubStudent);
            }
            return clubStudents;

        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public List<ClubStudent> findClubStudents_left_join(Connection connection) {
        //todo#22 - left join
        String query = "select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a left join jdbc_club_registrations b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id order by a.id asc, b.club_id asc";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClubStudent> clubStudents = new ArrayList<>();
            while (resultSet.next()){
                ClubStudent clubStudent = new ClubStudent(
                        resultSet.getString("student_id"),
                        resultSet.getString("student_name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                );
                clubStudents.add(clubStudent);
            }
            return clubStudents;

        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public List<ClubStudent> findClubStudents_right_join(Connection connection) {
        //todo#23 - right join
        String query = "select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id order by c.club_id asc,a.id asc";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClubStudent> clubStudents = new ArrayList<>();
            while (resultSet.next()){
                ClubStudent clubStudent = new ClubStudent(
                        resultSet.getString("student_id"),
                        resultSet.getString("student_name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                );
                clubStudents.add(clubStudent);
            }
            return clubStudents;

        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<ClubStudent> findClubStudents_full_join(Connection connection) {
        //todo#24 - full join = left join union right join
        StringBuilder query = new StringBuilder();
        query.append("select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a left join jdbc_club_registrations b on a.id=b.student_id left join jdbc_club c on b.club_id = c.club_id");
        query.append(System.lineSeparator());
        query.append("union");
        query.append(System.lineSeparator());
        query.append(System.lineSeparator());
        query.append("select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id = c.club_id");

        try(PreparedStatement preparedStatement = connection.prepareStatement(query.toString())){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClubStudent> clubStudents = new ArrayList<>();
            while (resultSet.next()){
                ClubStudent clubStudent = new ClubStudent(
                        resultSet.getString("student_id"),
                        resultSet.getString("student_name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                );
                clubStudents.add(clubStudent);
            }
            return clubStudents;

        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<ClubStudent> findClubStudents_left_excluding_join(Connection connection) {
        //todo#25 - left excluding join

        String query = "SELECT a.id as student_id, a.name as student_name, c.club_id, c.club_name FROM jdbc_students a LEFT JOIN jdbc_club_registrations b on a.id = b.student_id  LEFT JOIN jdbc_club c on b.club_id = c.club_id where c.club_id is null order by a.id asc";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClubStudent> clubStudents = new ArrayList<>();
            while (resultSet.next()){
                ClubStudent clubStudent = new ClubStudent(
                        resultSet.getString("student_id"),
                        resultSet.getString("student_name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                );
                clubStudents.add(clubStudent);
            }
            return clubStudents;

        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<ClubStudent> findClubStudents_right_excluding_join(Connection connection) {
        //todo#26 - right excluding join
        String query = "SELECT a.id as student_id, a.name as student_name, c.club_id, c.club_name FROM jdbc_students a RIGHT JOIN jdbc_club_registrations b ON a.id = b.student_id RIGHT JOIN jdbc_club c on b.club_id = c.club_id WHERE a.id is null order by c.club_id asc";


        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClubStudent> clubStudents = new ArrayList<>();
            while (resultSet.next()){
                ClubStudent clubStudent = new ClubStudent(
                        resultSet.getString("student_id"),
                        resultSet.getString("student_name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                );
                clubStudents.add(clubStudent);
            }
            return clubStudents;

        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<ClubStudent> findClubStudents_outher_excluding_join(Connection connection) {
        //todo#27 - outher_excluding_join = left excluding join union right excluding join
        StringBuilder query = new StringBuilder();
        query.append("SELECT a.id as student_id, a.name as student_name, c.club_id, c.club_name FROM jdbc_students a LEFT JOIN jdbc_club_registrations b on a.id = b.student_id  LEFT JOIN jdbc_club c on b.club_id = c.club_id where c.club_id is null");
        query.append(System.lineSeparator());
        query.append("union");
        query.append(System.lineSeparator());
        query.append(System.lineSeparator());
        query.append("SELECT a.id as student_id, a.name as student_name, c.club_id, c.club_name FROM jdbc_students a RIGHT JOIN jdbc_club_registrations b ON a.id = b.student_id RIGHT JOIN jdbc_club c on b.club_id = c.club_id WHERE a.id is null");


        try(PreparedStatement preparedStatement = connection.prepareStatement(query.toString())){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClubStudent> clubStudents = new ArrayList<>();
            while (resultSet.next()){
                ClubStudent clubStudent = new ClubStudent(
                        resultSet.getString("student_id"),
                        resultSet.getString("student_name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                );
                clubStudents.add(clubStudent);
            }
            return clubStudents;

        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }
        return Collections.emptyList();
    }

}