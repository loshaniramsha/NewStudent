package org.example.newstudent;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.newstudent.Dto.StudentDTO;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(urlPatterns = "/student")
public class StudentController extends HttpServlet {

    Connection connection;
    static String SAVE_STUDENT = "INSERT INTO student VALUES (?,?,?,?,?)";

    static String GET_STUDENT = "SELECT * FROM student where id = ?";

    @Override
    public void init() throws ServletException {
        try {
            var driverClass=getServletContext().getInitParameter("driver-class");
            var dbUrl=getServletContext().getInitParameter("dbURL");
            var userName=getServletContext().getInitParameter("dbUserName");
            var password=getServletContext().getInitParameter("dbPassword");
            System.out.println(driverClass+" "+dbUrl+" "+userName+" "+password);

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
       var student=new StudentDTO();
       var  studentId=req.getParameter("id");

       try(var writer=resp.getWriter()){
           var ps=connection.prepareStatement(GET_STUDENT);
           ps.setString(1,studentId);
           var rs=ps.executeQuery();
           while(rs.next()){
               student.setId(rs.getString("id"));
               student.setName(rs.getString("name"));
               student.setEmail(rs.getString("email"));
               student.setCity(rs.getString("city"));
               student.setLevel(rs.getString("level"));
           }
           writer.write(student.toString());

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo:Save student
        if (!req.getContentType().startsWith("application/json")) {
            resp.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

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


        //save student to db
        try (var writer = resp.getWriter()){
            PreparedStatement ps = connection.prepareStatement(SAVE_STUDENT);

            Jsonb jsonb = JsonbBuilder.create();
            StudentDTO studentDTO = jsonb.fromJson(req.getReader(), StudentDTO.class);
            ps.setString(1, studentDTO.getId());
            ps.setString(2, studentDTO.getName());
            ps.setString(3, studentDTO.getEmail());
            ps.setString(4, studentDTO.getCity());
            ps.setString(5, studentDTO.getLevel());
            ps.executeUpdate();

            writer.write("Student saved successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    }



    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
