package org.example.newstudent.Controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.newstudent.DAO.DataProcess;
import org.example.newstudent.Dto.StudentDTO;
import org.example.newstudent.Util.Utilprocess;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/student")
       /* initParams = {
                       @WebInitParam(name = "driver-class",value = "com.mysql.cj.jdbc.Driver"),
                       @WebInitParam(name = "dbURL",value = "jdbc:mysql://localhost:3306/javaee?createDatabaseIfNotExist=true"),
                       @WebInitParam(name = "dbUserName",value = "root"),
                       @WebInitParam(name = "dbPassword",value = "loshani@123")}*/

public class StudentController extends HttpServlet {

    Connection connection;
    static String SAVE_STUDENT = "INSERT INTO student VALUES (?,?,?,?,?)";

    static String GET_STUDENT = "SELECT * FROM student where id = ?";

    static String UPDATE_STUDENT = "UPDATE student SET name = ?, email = ?, city = ?, level = ? WHERE id = ?";

    static String DELETE_STUDENT = "DELETE FROM student WHERE id = ?";

    @Override
    public void init() throws ServletException {
        try {

            var driverClass=getServletContext().getInitParameter("driver-class");
            var dbUrl=getServletContext().getInitParameter("dbURL");
            var userName=getServletContext().getInitParameter("dbUserName");
            var password=getServletContext().getInitParameter("dbPassword");


            // Get config from servlet
       /*   var driverClass=getServletConfig().getInitParameter("driver-class");
            var dbUrl=getServletConfig().getInitParameter("dbURL");
            var userName=getServletConfig().getInitParameter("dbUserName");
            var password=getServletConfig().getInitParameter("dbPassword");*/

           /* System.out.println(driverClass+" "+dbUrl+" "+userName+" "+password);*/

            Class.forName(driverClass);
            this.connection= DriverManager.getConnection(dbUrl,userName,password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      /* var student=new StudentDTO();*/
       var  studentId=req.getParameter("id");
       var dataprocess=new DataProcess();

       try(var writer=resp.getWriter()){
           var student=dataprocess.get(studentId,this.connection);
           System.out.println(student);
           resp.setContentType("application/json");
           var jsonb= JsonbBuilder.create();
           jsonb.toJson(student,writer);
       }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo:Save student
        if (!req.getContentType().startsWith("application/json")) {
            resp.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
        //save student to db
        try (var writer = resp.getWriter()){
           /* PreparedStatement ps = connection.prepareStatement(SAVE_STUDENT);*/

            Jsonb jsonb = JsonbBuilder.create();  //json buinder
            StudentDTO studentDTO = jsonb.fromJson(req.getReader(), StudentDTO.class);
            studentDTO.setId(Utilprocess.generateId());
            var savedata=new DataProcess();
            if (savedata.save(studentDTO, this.connection)) {
                writer.write("Student saved successfully");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                writer.write("Student not saved");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (JsonbException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().startsWith("application/json")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
     try(var writer=resp.getWriter()){
         var studentId=req.getParameter("id");
         Jsonb jsonb=JsonbBuilder.create();
         var DataProcess=new DataProcess();
         var updatedStudent=jsonb.fromJson(req.getReader(),StudentDTO.class);
         if(DataProcess.update(studentId,updatedStudent,connection)){
             resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
         }
         else {
             writer.write("Student not updated");
             resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
         }
     }catch (JsonbException e){
         resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
         e.printStackTrace();
     }
 /*       try {
            var ps= this.connection.prepareStatement(UPDATE_STUDENT);
          *//* var id= req.getParameter("id");*//*
           Jsonb jsonb=JsonbBuilder.create();
           var updatedStudent=jsonb.fromJson(req.getReader(),StudentDTO.class);
           ps.setString(1,updatedStudent.getName());
           ps.setString(2,updatedStudent.getEmail());
           ps.setString(3,updatedStudent.getCity());
           ps.setString(4,updatedStudent.getLevel());
           ps.setString(5,updatedStudent.getId());

           if (ps.executeUpdate() !=0){
               resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
               resp.getWriter().write("Student updated successfully");
           }
           else {
               resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
               resp.getWriter().write("Student not updated");

           }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var ps=this.connection.prepareStatement(DELETE_STUDENT);
            var id= req.getParameter("id");
            ps.setString(1,id);
            if (ps.executeUpdate() !=0){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                resp.getWriter().write("Student deleted successfully");
            }
            else {
                resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                resp.getWriter().write("Student not deleted");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}











  /*         var ps=connection.prepareStatement(GET_STUDENT);
           ps.setString(1,studentId);
           var rs=ps.executeQuery();
           while(rs.next()){
               student.setId(rs.getString("id"));
               student.setName(rs.getString("name"));
               student.setEmail(rs.getString("email"));
               student.setCity(rs.getString("city"));
               student.setLevel(rs.getString("level"));
           }
           writer.write(student.toString());*/



/* String id=UUID.randomUUID().toString();*/
/*Jsonb ObjectArray*/
       /* Jsonb jsonb= JsonbBuilder.create();
        List<StudentDTO> studentDTOList=jsonb.fromJson(req.getReader(),new ArrayList<StudentDTO>(){}.getClass().getGenericSuperclass());
        studentDTOList.forEach(System.out::println);*/

/* Jsonb Object*/
     /*   String id= UUID.randomUUID().toString();
        Jsonb jsonb= JsonbBuilder.create();
       StudentDTO studentDTO=jsonb.fromJson(req.getReader(),StudentDTO.class);
       studentDTO.setId(id);
        System.out.println(studentDTO);*/

/*data persist*/

   /*     String id= UUID.randomUUID().toString();
        Jsonb jsonb= JsonbBuilder.create();
        StudentDTO studentDTO=jsonb.fromJson(req.getReader(), StudentDTO.class);
       // studentDTO.setId(id);
        System.out.println(studentDTO);

      try {
          var ps=connection.prepareStatement(SAVE_STUDENT);
          ps.setString(1,studentDTO.getId());
          ps.setString(2,studentDTO.getName());
          ps.setString(3,studentDTO.getEmail());
          ps.setString(4,studentDTO.getCity());
          ps.setString(5,studentDTO.getLevel());
          if (ps.executeUpdate() !=0){
              resp.getWriter().write("Student saved successfully");
          }
          else {
              resp.getWriter().write("Student not saved");
          }
      } catch (SQLException e) {
          throw new RuntimeException(e);
      }*/


  /* ps.setString(1, studentDTO.getId());
            ps.setString(2, studentDTO.getName());
            ps.setString(3, studentDTO.getEmail());
            ps.setString(4, studentDTO.getCity());
            ps.setString(5, studentDTO.getLevel());
            ps.executeUpdate();*/



    /*    BufferedReader reader=req.getReader();
        StringBuilder sb=new StringBuilder();
        var writer=resp.getWriter();
        reader.lines().forEach(line->sb.append(line+"\n"));
        System.out.println(sb);
        writer.write(sb.toString());
        writer.close();*/


// JSON mainpulate with parson
      /*  JsonReader reader= Json.createReader(req.getReader());
        var jArray =reader.readArray();
        for (int i=0;i<jArray.size();i++){
            JsonObject jsonObject=jArray.getJsonObject(i);
            System.out.println(jsonObject.getString("name"));

        }*/

/*object*/
/*   JsonObject jsonObject=reader.readObject();*/
/*   System.out.println(jsonObject.getString("email"));*/