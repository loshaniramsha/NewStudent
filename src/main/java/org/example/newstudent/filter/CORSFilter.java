package org.example.newstudent.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebFilter(urlPatterns = "/*")

public class CORSFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        var origin=req.getHeader("Origin");
        var configOrigin=getServletContext().getInitParameter("origin");
      if (origin.contains(configOrigin)){
          res.setHeader("Access-Control-Allow-Origin",origin);
          res.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE");
          res.setHeader("Access-Control-Allow-Headers","Content-Type");
          res.setHeader("Access-Control-Allow-Credentials","true");
      }
      chain.doFilter(req,res);
    }
}
