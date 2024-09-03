//package ro.exampledana.servlet;
//
//import jakarta.servlet.ServletConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.mindrot.jbcrypt.BCrypt;
//import ro.exampledana.entity.User;
//import ro.exampledana.service.UserService;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.io.Serial;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//@WebServlet(urlPatterns = {"/createAccount"})
//public class CreateAccountServlet extends HttpServlet {
//    @Serial
//    private static final long serialVersionUID = 3L;
//    private Connection dbConnection;
//    public UserService userService;
//
//    @Override
//    public void init(ServletConfig config) {
//        try {
//            Context initialContext = new InitialContext();
//            Context envContext = (Context) initialContext.lookup("java:/comp/env");
//            DataSource ds = (DataSource) envContext.lookup("jdbc/MyDB");
//            dbConnection = ds.getConnection();
//            userService=new UserService(dbConnection);
//        } catch (NamingException | SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.getRequestDispatcher("/jsps/createAccount/createAccount.jsp").forward(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String newUsername = req.getParameter("newUsername");
//        String newPassword = req.getParameter("newPassword");
//        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
//
//        if(newUsername!=null&&newPassword!=null) {
//            if (userService.findUser(newUsername)==null) {
//                userService.createUser(new User(newUsername,hashedNewPassword));
//                req.setAttribute("accountCreated", "Thanks for signing up. Your account has been created.");
//                req.getRequestDispatcher("/jsps/createAccount/createAccount.jsp").forward(req, resp);
//                //resp.sendRedirect(req.getContextPath()+"/login");
//            } else {
//                req.setAttribute("errorNewAccount", "This username is not available. Try another one!");
//                req.getRequestDispatcher("/jsps/createAccount/createAccount.jsp").forward(req, resp);
//            }
//        }
//    }
//}
