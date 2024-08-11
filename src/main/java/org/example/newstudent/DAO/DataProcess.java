package org.example.newstudent.DAO;

import org.example.newstudent.Dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public final class DataProcess implements Data {

    static String SAVE_STUDENT = "INSERT INTO student VALUES (?,?,?,?,?)";

    static String GET_STUDENT = "SELECT * FROM student where id = ?";

    static String UPDATE_STUDENT = "UPDATE student SET name = ?, email = ?, city = ?, level = ? WHERE id = ?";

    static String DELETE_STUDENT = "DELETE FROM student WHERE id = ?";

    @Override
    public boolean save(StudentDTO studentDTO, Connection connection) {
   try {
       var ps=connection.prepareStatement(SAVE_STUDENT);
       ps.setString(1,studentDTO.getId());
       ps.setString(2,studentDTO.getName());
       ps.setString(3,studentDTO.getEmail());
       ps.setString(4,studentDTO.getCity());
       ps.setString(5,studentDTO.getLevel());
       return ps.executeUpdate()!=0;
   } catch (SQLException e) {
       throw new RuntimeException(e);
   }
    }

    @Override
    public boolean update(String studentId, StudentDTO updateStudentDTO, Connection connection) {
        try {
            var ps=connection.prepareStatement(UPDATE_STUDENT);
            ps.setString(1,updateStudentDTO.getName());
            ps.setString(2,updateStudentDTO.getEmail());
            ps.setString(3,updateStudentDTO.getCity());
            ps.setString(4,updateStudentDTO.getLevel());
            ps.setString(5,studentId);
            return ps.executeUpdate()!=0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StudentDTO delete(String studentId, Connection connection) {
        return null;
    }

    @Override
    public StudentDTO get(String studentId, Connection connection) {
       var student=new StudentDTO();
        try{
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
          /*  writer.write(student.toString());*/

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return student;
    }
}
