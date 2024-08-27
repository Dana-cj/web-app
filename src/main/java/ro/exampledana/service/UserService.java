package ro.exampledana.service;

import ro.exampledana.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {
    private Connection dbConnection;

    public UserService(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void createUser(User user){
        String command = "insert into files values (?, ?)";
        try {
            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(command);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePassword(String newPassword, String username) {
        String insertCommand = "update users set password = ? where username = ?";
        try {
            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(insertCommand);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
