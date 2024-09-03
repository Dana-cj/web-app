package ro.exampledana.service;

import org.mindrot.jbcrypt.BCrypt;
import ro.exampledana.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    private Connection dbConnection;

    public UserService(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void createUser(User user){
        String command = "insert into users values (?, ?)";
        try {
            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(command);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUser(String username) {
        User user= null;
        try {
            PreparedStatement statement2 = this.dbConnection.prepareStatement("select * from users where username = ?");
            statement2.setString(1, username);
            ResultSet result = statement2.executeQuery();
            result.next();
            user=new User(result.getString(1),
                        result.getString(2));
        } catch (SQLException e){
            e.printStackTrace(System.err);
        }
        return user;
    }
    public boolean checkPassword(String username, String password){
       // return findUser(username).getPassword().equals(password);
        return (findUser(username).getPassword() != null && BCrypt.checkpw(password, findUser(username).getPassword()));
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
