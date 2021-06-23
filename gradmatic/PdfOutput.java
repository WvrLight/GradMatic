package gradmatic;

import java.util.ArrayList;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import gradmatic.StudentInfo;
import gradmatic.SectionInfo;
import gradmatic.GradeInfo;
import gradmatic.SubjectInfo;

public class PdfOutput {
    public static final String DEST = "./key_value_table.pdf";

    public static void main(String[] args) throws Exception {
        // File file = new File(DEST);
        // file.getParentFile().mkdirs();

        StudentRecord test = new StudentRecord(122);
        System.out.println(test.student.studentLN);
        System.out.println(test.section.sectionName);
        System.out.println(test.subject.get(0).subjectName);
        System.out.println(test.grade.get(0).studentPeriodGrade);
        System.out.println(test.grade.get(1).studentPeriodGrade);
        System.out.println(test.grade.get(2).studentPeriodGrade);
        System.out.println(test.grade.get(3).studentPeriodGrade);

        // new PdfOutput().manipulatePdf(DEST);
    }

    // protected void manipulatePdf(String dest) throws IOException {

    //     PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
    //     Document document = new Document(pdf);
    //     PdfFont regular = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
    //     PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

    //     document.add(createTable(rohit, bold, regular));
    //     document.add(createTable(bruno, bold, regular));

    //     document.close();
    // }

    // private static Table createTable(UserObject user, PdfFont titleFont, PdfFont defaultFont) {
    //     Table table = new Table(UnitValue.createPercentArray(2));
    //     table.setWidth(UnitValue.createPercentValue(30)).setMarginBottom(10);

    //     table.addHeaderCell(new Cell().setFont(titleFont).add(new Paragraph("Key")));
    //     table.addHeaderCell(new Cell().setFont(titleFont).add(new Paragraph("Value")));

    //     table.addCell(new Cell().setFont(titleFont).add(new Paragraph("Name")));
    //     table.addCell(new Cell().setFont(defaultFont).add(new Paragraph(user.getName())));

    //     table.addCell(new Cell().setFont(titleFont).add(new Paragraph("Id")));
    //     table.addCell(new Cell().setFont(defaultFont).add(new Paragraph(user.getId())));

    //     table.addCell(new Cell().setFont(titleFont).add(new Paragraph("Reputation")));
    //     table.addCell(new Cell().setFont(defaultFont).add(new Paragraph(String.valueOf(user.getReputation()))));

    //     table.addCell(new Cell().setFont(titleFont).add(new Paragraph("Job title")));
    //     table.addCell(new Cell().setFont(defaultFont).add(new Paragraph(user.getJobtitle())));

    //     return table;
    // }

    private static class StudentRecord {
        Section section = new Section();
        Student student = new Student();
        // Subject[] subject = null;
        // Grade[] grade = null;
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

                // g_count = getCount(s, studentID, "select count(*) as count from Grades where studentID = " + studentID);
                // System.out.println(g_count);
                // grade = new Grade[g_count];

                // s_count = getCount(s, studentID, "select count(distinct subjectID) as count from Grades where studentID = " + studentID);
                // System.out.println(s_count);
                // subject = new Subject[s_count];

                // Store all student info
                student = StudentInfo.getStudent(s, studentID);
                section = SectionInfo.getSection(s, student.sectionID);


                // rs = s.executeQuery("select * from Subjects where studentID = " + studentID);
                // while (rs.next()) {
                //     subject[loop] = SubjectInfo.getSubject(s, rs.getInt("subjectID"));
                //     loop++;
                // }

                rs = s.executeQuery("select distinct subjectID from Grades where studentID = " + studentID);
                while (rs.next()) {
                    subject.add(SubjectInfo.getSubject(s, rs.getInt("subjectID")));
                }

                PreparedStatement ps = connection.prepareStatement("select * from Grades where studentID = ? and subjectID = ?");
                ps.setString(1, "" + studentID);
                int loop = 0;
                for (int i = 0; i < subject.size(); i++) {
                    ps.setString(2, "" + subject.get(i).subjectID);
                    rs = ps.executeQuery();
                    for (int j = 1; j <= 4; j++) {
                        if (rs.next()) {
                            grade.add(GradeInfo.getGrade(s, studentID, subject.get(i).subjectID, j));
                            loop++;
                        }
                    }
                }

                System.out.println(grade.size());
            }
            catch (SQLException e)
            {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println(e.getMessage());
            }
            finally {
                try {
                    if (connection != null)
                        connection.close();
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }

        static int getCount(Statement s, int studentID, String sql) {
            int count = 0;
            try {
                ResultSet rs = s.executeQuery(sql);
                if (rs.next()) {
                    count = rs.getInt("count");
                }
            }
            catch (SQLException e) {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println(e.getMessage());
            }

            return count;
        }
    }
}