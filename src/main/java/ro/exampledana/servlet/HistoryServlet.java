package ro.exampledana.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.exampledana.entity.Doable;
import ro.exampledana.service.DoableService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/history"})
public class HistoryServlet extends HttpServlet {
    private Connection dbConnection;
    public DoableService doableService;
   // public static String browserLanguage;

    @Override
    public void init(ServletConfig config) {
               try {
            Context  initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/MyDB");
            dbConnection = ds.getConnection();
            doableService= new DoableService(dbConnection);
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //set tasks active
        request.setAttribute("historyActive", "active");
        listHistory(request, response);
        // set the date format according to browser language
//        String acceptLanguageHeader = request.getHeader("Accept-Language");
//        if (acceptLanguageHeader != null && !acceptLanguageHeader.isEmpty()) {
//            String[] languages = acceptLanguageHeader.split(",");
//            if (languages.length > 0) {
//                browserLanguage = languages[0].trim();
//            }
//        }
        //read action and fulfill request
//        String action = readActionParameter(request);
//        switch (action){
//            case "list"-> {
//                request.setAttribute("dueDateSortingActive", "active");
//                listTasksSortedByDueDate(request, response);
//            }
//            case "listTasksSortedByPriority"-> {
//                request.setAttribute("prioritySortingActive", "active");
//                listTasksSortedByPriority(request, response);
//            }
//            case "listTasksSortedByStatus"-> {
//                request.setAttribute("statusSortingActive", "active");
//                listTasksSortedByStatus(request, response);
//            }
//        }
//    }
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = readActionParameter(request);
//        switch (action){
//           case "add"-> {
//               String description = request.getParameter("description");
//               String dueDateAsString= request.getParameter("dueDate");
//               SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//               GregorianCalendar dueDate= new GregorianCalendar();
//               try {
//                   java.util.Date date= new java.util.Date(formatter.parse(dueDateAsString).getTime());
//                   dueDate.setTime(date);
//               } catch (ParseException e) {
//                   throw new RuntimeException(e);
//               }
//               taskService.createTask(new Task(taskService.nextId(), description, dueDate, Priority.valueOf(request.getParameter("priority").replace(" ","_"))));
//               listTasksSortedByDueDate(request, response);
//           }
//           case "edit"->{
//               int id= Integer.parseInt(request.getParameter("taskId"));
//               String description = request.getParameter("description");
//               String dueDateAsString= request.getParameter("dueDate");
//               SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//               GregorianCalendar dueDate= new GregorianCalendar();
//               try {
//                   java.util.Date date= new java.util.Date(formatter.parse(dueDateAsString).getTime());
//                   dueDate.setTime(date);
//               } catch (ParseException e) {
//                   throw new RuntimeException(e);
//               }
//               String priorityAsString= request.getParameter("priority");
//               String statusAsString= request.getParameter("status");
//               taskService.updateTask(id,description, dueDate, Priority.valueOf(priorityAsString.replace(" ","_")),
//                       Status.valueOf(statusAsString.replace(" ","_")));
//               listTasksSortedByDueDate(request, response);
//           }
//       }
    }

    private void listHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Doable> doableList= doableService.findAll().stream()
                .filter(doable->doable.isHistory())
                //.filter(doable->doable.isHistory())
                //.map(doable -> doable.getDescription()+ "")
                .collect(Collectors.toList());
        request.setAttribute("doableList", doableList);
        request.getRequestDispatcher("jsps/history/list.jsp").forward(request, response);
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
