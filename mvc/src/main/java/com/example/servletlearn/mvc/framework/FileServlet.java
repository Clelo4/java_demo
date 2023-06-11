package com.example.servletlearn.mvc.framework;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(urlPatterns = { "/favicon.ico", "/static/*" })
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext ctx = request.getServletContext();
        // RequestURI包含ContextPath,需要去掉:
        String urlPath = request.getRequestURI().substring(ctx.getContextPath().length());
        String filePath = ctx.getRealPath(urlPath);
        System.err.println("doGet filepath " + filePath);

        if (filePath == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path path = Paths.get(filePath);
        if (!path.toFile().isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String mime = Files.probeContentType(path);
        if (mime == null) {
            mime = "application/octet-stream";
        }
        response.setContentType(mime);
        OutputStream output = response.getOutputStream();
        try (InputStream input = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            input.transferTo(output);
        }
        output.flush();
    }
}
