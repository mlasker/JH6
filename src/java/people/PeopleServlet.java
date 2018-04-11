package people;

import connectionpool.ConnectionPool;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PeopleServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ConnectionPool connectionPool = new ConnectionPool("test", 5, 1);
        Connection connection = connectionPool.getConnection();

        PersonCollection pc = new PersonCollection();

        String submit = request.getParameter("submit");
        try {
            if (submit != null) {
                switch (submit) {
                    case "Add Person":
                        System.out.println("add person selected");
                        pc.insert(connection, request);
                        break;
                    case "Delete":
                        System.out.println("delete selected");
                        pc.delete(connection, request);
                        break;
                    case "Edit":
                        System.out.println("edit selected");
                        pc.update(connection, request);
                        break;
                    case "Drop Table":
                        System.out.println("Dropping table....");
                        pc.drop(connection);
                        break;
                    case "Create Table":
                        pc.createTable(connection);
                        break;
                    case "CREATE TEST TABLE": 
                        pc.createTable(connection);
                        pc.insertTest(connection);
                        break;
                }
            }

            pc.fillTable(connection);
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        request.setAttribute("pc", pc.getPersonCollection());
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
