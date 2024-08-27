package ro.exampledana.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ro.exampledana.entity.Task;
import ro.exampledana.service.TaskService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/history"})
public class HistoryServlet extends HttpServlet {
    private Connection dbConnection;
    public TaskService taskService;
    private String username;

    @Override
    public void init(ServletConfig config) {
               try {
            Context  initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/MyDB");
            dbConnection = ds.getConnection();
            taskService= new TaskService(dbConnection);
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //set tasks active
        request.setAttribute("historyActive", "active");
        //retrieve username
        HttpSession session = request.getSession(false);
        if (session != null) {
            username = (String) session.getAttribute("username");
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
        //read action and fulfill request
        String action = readActionParameter(request);
        switch (action) {
            case "list" -> {
                request.setAttribute("dueDateSortingActive", "active");
                listHistory(request, response);
            }
            case "listHistorySortedByPriority" -> {
                request.setAttribute("prioritySortingActive", "active");
                listHistorySortedByPriority(request, response);
            }
            case "delete"-> {
                int id= Integer.parseInt(request.getParameter("taskId"));
                taskService.delete(id);
                request.setAttribute("dueDateSortingActive", "active");
                listHistory(request, response);
            }
        }
    }

    private void listHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Task> tasks= taskService.findAll(username).stream()
                .filter(task->task.isHistory())
                .sorted(Comparator.comparing(Task::getDueDate)
                        .thenComparing(Task::getPriorityValue)
                        .thenComparing(Task::getInitialDate))
                .collect(Collectors.toList());
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("jsps/history/list.jsp").forward(request, response);
    }


    private void listHistorySortedByPriority(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> tasks= taskService.findAll(username)
                .stream()
                .filter(task->task.isHistory())
                .sorted(Comparator.comparing(Task::getPriorityValue)
                        .thenComparing(Task::getDueDate)
                        .thenComparing(Task::getInitialDate))
                .collect(Collectors.toList());
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("jsps/history/list.jsp").forward(request, response);
    }

    private static String readActionParameter(HttpServletRequest request) {
        String action= request.getParameter("action");
        action= (action==null)? "list":action;
        return action;
    }
//    private void listTasksSortedByDueDate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<Task> tasks= taskService.findAll()
//                .stream()
//
//                .sorted(Comparator.comparing(Task::getDueDate)
//                        .thenComparing(Task::getPriorityValue)
//                        .thenComparing(Task::getInitialDate))
//                .collect(Collectors.toList());
//        request.setAttribute("tasks", tasks);
//        request.getRequestDispatcher("jsps/task/list.jsp").forward(request, response);
//    }
//
//    private void listTasksSortedByPriority(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<Task> tasks= taskService.findAll()
//                .stream()
//                .sorted(Comparator.comparing(Task::getPriorityValue)
//                        .thenComparing(Task::getDueDate)
//                        .thenComparing(Task::getInitialDate))
//                .collect(Collectors.toList());
//        request.setAttribute("tasks", tasks);
//        request.getRequestDispatcher("jsps/task/list.jsp").forward(request, response);
//    }
//
//    private void listTasksSortedByStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<Task> tasks= taskService.findAll()
//                .stream()
//                .sorted(Comparator.comparing(Task::getStatus)
//                        .thenComparing(Task::getDueDate)
//                        .thenComparing(Task::getPriority)
//                        .thenComparing(Task::getInitialDate))
//                .collect(Collectors.toList());
//        request.setAttribute("tasks", tasks);
//        request.getRequestDispatcher("jsps/task/list.jsp").forward(request, response);
//    }
//
//    private static String readActionParameter(HttpServletRequest request) {
//        String action= request.getParameter("action");
//        action= (action==null)? "list":action;
//        return action;
//    }

}
