package gradmatic;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 * STUDENT RECORD
 * 
 * To use, simply add the line:
 * StudentRecord <name> = new StudentRecord(<studentID>);
 * 
 * This will store all relevant information for the student:
 * - Student information
 * - Section information
 * - All grades of the student in each grade
 * 
 * To retrieve the data within the class (example, student last name):
 * <nameofvariable>.<nameoftable>.<datacolumn>
 * i.e.
 * record.student.studentLN;
 */
public class StudentRecord {
    Section section = new Section();
    Student student = new Student();
    ArrayList<Subject> subject = new ArrayList<Subject>();
    ArrayList<Grade> grade = new ArrayList<Grade>();

    public StudentRecord(int studentID) {
        Connection connection = null;
        try {
            ResultSet rs;

            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:gm.db");
            Statement s = connection.createStatement();
            s.setQueryTimeout(5);

            // Store all student info
            student = StudentInfo.getStudent(s, studentID);

            // Store section info
            section = SectionInfo.getSection(s, student.sectionID);

            // // Subjects
            // rs = s.executeQuery("select distinct subjectID from Grades where studentID = " + studentID);
            // while (rs.next()) {
            //     subject.add(SubjectInfo.getSubject(s, rs.getInt("subjectID")));
            // }

            // Grades and Subjects
            rs = s.executeQuery("select * from Grades where studentID = " + studentID + " order by subjectID ASC");
            while (rs.next()) {
                int temp_id = rs.getInt("subjectID");;
                for (int j = 1; j <= 4; j++) {
                    System.out.println(j);
                    grade.add(GradeInfo.getGrade(s, studentID, temp_id, j));
                }
                subject.add(SubjectInfo.getSubject(s, temp_id));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}
