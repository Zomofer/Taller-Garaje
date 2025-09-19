/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.uts.vehicleapp;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/vehicles")
public class VehicleServlet extends HttpServlet {

    @Resource(lookup = "jdbc/garageDB")
    private DataSource dataSource;

//    @Override
//    public void init() throws ServletException {
//        try {
//            Context initContext = new InitialContext();
//            dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/garageDB");
//        } catch (Exception e) {
//            throw new ServletException(e);
//        }
//    }
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Vehicle> vehicles = new ArrayList<>();
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vehicles")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getInt("id"), rs.getString("brand"), rs.getString("model"), rs.getInt("yearr"),
                        rs.getString("owner")));
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        request.setAttribute("vehicles", vehicles);
        request.getRequestDispatcher("/vehicles.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        int year = Integer.parseInt(request.getParameter("year"));
        String owner = request.getParameter("owner");
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement( 
            "INSERT INTO vehicles (brand, model, yearr, owner) VALUES(?, ?, ?, ?)")) {
 stmt.setString(1, brand);
            stmt.setString(2, model);
            stmt.setInt(3, year);
            stmt.setString(4, owner);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new ServletException(e);
        }
        response.sendRedirect("vehicles");
    }
}
