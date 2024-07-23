package com.example.servlet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/items/*")
public class ItemServlet extends HttpServlet {
    private List<Item> items = new ArrayList<>();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            out.print(gson.toJson(items));
        } else {
            String[] splits = pathInfo.split("/");
            int id = Integer.parseInt(splits[1]);
            for (Item item : items) {
                if (item.getId() == id) {
                    out.print(gson.toJson(item));
                    break;
                }
            }
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        Item item = gson.fromJson(request.getReader(), Item.class);
        items.add(item);
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(item));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();
        String[] splits = pathInfo.split("/");
        int id = Integer.parseInt(splits[1]);
        Item updatedItem = gson.fromJson(request.getReader(), Item.class);

        for (Item item : items) {
            if (item.getId() == id) {
                item.setName(updatedItem.getName());
                item.setDescription(updatedItem.getDescription());
                break;
            }
        }

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(updatedItem));
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();
        String[] splits = pathInfo.split("/");
        int id = Integer.parseInt(splits[1]);
        items.removeIf(item -> item.getId() == id);

        PrintWriter out = response.getWriter();
        out.print("{\"message\":\"Item deleted\"}");
        out.flush();
    }
}
