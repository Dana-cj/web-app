package ro.exampledana.service;

import ro.exampledana.entity.Doable;
import ro.exampledana.entity.Status;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DoableService {
    private Connection dbConnection;

    public DoableService(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }


//    public int nextId() {
//        int nextIndex=1;
//        try {
//            Statement statement = dbConnection.createStatement();
//            ResultSet results = statement.executeQuery("select tasks.id\n" +
//                    "from tasks\n" +
//                    "order by id desc\n" +
//                    "limit 1");
//            results.next();
//            nextIndex=  results.getInt(1)+1;
//
//        } catch (SQLException e) {
//            e.printStackTrace(System.err);
//        }
//        return nextIndex;
//    }

//       public Task findById(int id) {
//        Task task=null;
//        try {
//            PreparedStatement statement = dbConnection.prepareStatement("select * from tasks where id = ?");
//            statement.setInt(1, id);
//            ResultSet result = statement.executeQuery();
//            result.next();
//
//            GregorianCalendar date1= new GregorianCalendar();
//            java.util.Date myDate1= new java.util.Date(result.getDate(3).getTime());
//            date1.setTime(myDate1);
//
//            GregorianCalendar date2= new GregorianCalendar();
//            java.util.Date myDate2= new java.util.Date(result.getDate(4).getTime());
//            date2.setTime(myDate2);
//
//            task=  new Task(result.getInt(1),
//                            result.getString(2),
//                            date1,
//                            date2,
//                    Priority.valueOf(result.getString(5)),
//                    Status.valueOf(result.getString(6).replace(" ","_")));
//        } catch (SQLException e) {
//            e.printStackTrace(System.err);
//        }
//        return task;
//    }
//    public void updateTask(int id, String description, GregorianCalendar dueDate, Priority priority, Status status) {
//       // String priorityAsString= priority.toString();
//        String insertCommand = "update tasks set description = ?, due_date = ?, priority = ?, status = ?  where id = ?";
//        try {
//            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(insertCommand);
//            preparedStatement.setString(1, description);
//            //preparedStatement.setDate(2, new Date(initialDate.getTimeInMillis()));
//            preparedStatement.setDate(2,  new Date(dueDate.getTimeInMillis()));
//            preparedStatement.setString(3, priority.toString());
//            preparedStatement.setString(4, status.toString());
//            preparedStatement.setInt(5, id);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public void delete(int id) {
//        String insertCommand = "delete from tasks where id= ?";
//        try {
//            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(insertCommand);
//            preparedStatement.setInt(1,id);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
    public List<Doable> findAll() {
        List<Doable> doableList= new ArrayList<>();
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet results = statement.executeQuery("select * from tasks");
//            results. = statement.executeQuery("select * from tasks");
            while (results.next()) {
//                GregorianCalendar date1= new GregorianCalendar();
//                java.util.Date myDate1= new java.util.Date(results.getDate(3).getTime());
//                date1.setTime(myDate1);
//
//                GregorianCalendar date2= new GregorianCalendar();
//                java.util.Date myDate2= new java.util.Date(results.getDate(4).getTime());
//                date2.setTime(myDate2);

                doableList.add(
                        new Doable(results.getInt(1),
                                results.getString(2),
//                                date1,
//                                date2,
//                                Priority.valueOf(results.getString(5)),
                                Status.valueOf(results.getString(6).replace(" ", "_"))) {
                        });
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return doableList;
    }
}
