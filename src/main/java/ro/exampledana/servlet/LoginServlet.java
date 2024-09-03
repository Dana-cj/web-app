package ro.exampledana.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import ro.exampledana.entity.User;
import ro.exampledana.service.UserService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 2L;
    private Connection dbConnection;
    public UserService userService;

    @Override
    public void init(ServletConfig config) {
        try {
            Context initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/MyDB");
            dbConnection = ds.getConnection();
            userService=new UserService(dbConnection);
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = readActionParameter(request);
        switch (action) {
            //case "home"->response.getWriter().println("Username: "+username);//request.getRequestDispatcher("jsps/home.jsp").forward(request,response);
            case "login" -> {
                request.getRequestDispatcher("/jsps/login/login.jsp").forward(request, response);
            }
            case "createAccount"->request.getRequestDispatcher("/jsps/createAccount/createAccount.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = readActionParameter(request);
        switch (action) {
        case "login" -> {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

//        String newUsername = req.getParameter("newUsername");
//        String newPassword = req.getParameter("newPassword");
//        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));

            if (username != null && password != null) {
                //if (username.equals("admin") && password.equals("admin")||username.equals("dana") && password.equals("dana")) {
                if (userService.findUser(username) != null && userService.checkPassword(username, password)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    response.sendRedirect(request.getContextPath() + "/tasks");
                } else {
                    request.setAttribute("error", "Invalid username or password!");
                    request.getRequestDispatcher("/jsps/login/login.jsp").forward(request, response);
                }
            }
        }
        case "createAccount"->{
            String newUsername = request.getParameter("newUsername");
            String newPassword = request.getParameter("newPassword");
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));

            if(newUsername!=null&&newPassword!=null) {
                if (userService.findUser(newUsername)==null) {
                    userService.createUser(new User(newUsername,hashedNewPassword));
                    request.setAttribute("accountCreated", "Thanks for signing up. Your account has been created. Please click on the link below to access the Log in page!");
                    //request.setAttribute("action", "login");
                    request.getRequestDispatcher("/jsps/createAccount/accountCreated.jsp").forward(request, response);
                    //response.sendRedirect(request.getContextPath()+"/login");
                } else {
                    request.setAttribute("errorNewAccount", "This username is not available. Try another one!");
                    request.getRequestDispatcher("/jsps/createAccount/createAccount.jsp").forward(request, response);
                }
            }
        }
    }
//        if(newUsername!=null&&newPassword!=null) {
//            //userService.create(newUsername, newPassword);
//            if (userService.findUser(newUsername)==null) {
//                userService.createUser(new User(newUsername,hashedNewPassword));
//                req.setAttribute("accountCreated", "Thanks for signing up. Your account has been created");
//                req.getRequestDispatcher("/jsps/login/login.jsp").forward(req, resp);
//            } else {
//                req.setAttribute("errorNewAccount", "This username is not available. Try another one!");
//                req.getRequestDispatcher("/jsps/login/login.jsp").forward(req, resp);
//            }
//        }
    }

    private static String readActionParameter(HttpServletRequest request) {
        String action= request.getParameter("action");
        action= (action==null)? "login":action;
        return action;
    }
}
