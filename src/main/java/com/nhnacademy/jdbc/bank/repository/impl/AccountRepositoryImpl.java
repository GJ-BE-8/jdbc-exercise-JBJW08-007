package com.nhnacademy.jdbc.bank.repository.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class AccountRepositoryImpl implements AccountRepository {

    public Optional<Account> findByAccountNumber(Connection connection, long accountNumber){
        //todo#1 계좌-조회
        String query = "SELECT * FROM jdbc_account WHERE account_number = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1, accountNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                Account account = new Account(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getLong(3)
                );
                return Optional.of(account);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int save(Connection connection, Account account) {
        //todo#2 계좌-등록, executeUpdate() 결과를 반환 합니다.
        String query = "INSERT INTO jdbc_account(account_number, name, balance) VALUES(?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setLong(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setLong(3, account.getBalance());

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByAccountNumber(Connection connection, long accountNumber){
        int count=0;
        //todo#3 select count(*)를 이용해서 계좌의 개수를 count해서 반환
        String query = "SELECT COUNT(*) FROM jdbc_account WHERE account_number = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1, accountNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1); // 첫 번째 열의 값을 가져옵니다.
                }
            }

        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }

        return count;
    }

    @Override
    public int deposit(Connection connection, long accountNumber, long amount){
        //todo#4 입금, executeUpdate() 결과를 반환 합니다.
        String query = "UPDATE jdbc_account SET balance = balance + ? WHERE account_number = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1,amount);
            preparedStatement.setLong(2,accountNumber);

            return preparedStatement.executeUpdate();
        }catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }

        return 0;
    }

    @Override
    public int withdraw(Connection connection, long accountNumber, long amount){
        //todo#5 출금, executeUpdate() 결과를 반환 합니다.
        String query = "UPDATE jdbc_account SET balance = balance - ? WHERE account_number = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1,amount);
            preparedStatement.setLong(2,accountNumber);

            return preparedStatement.executeUpdate();
        }catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }
        return 0;
    }

    @Override
    public int deleteByAccountNumber(Connection connection, long accountNumber) {
        //todo#6 계좌 삭제, executeUpdate() 결과를 반환 합니다.
        String query = "DELETE FROM jdbc_account WHERE account_number = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1,accountNumber);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("sql exception : {}", e.getMessage());
        }
        return 0;
    }
}
