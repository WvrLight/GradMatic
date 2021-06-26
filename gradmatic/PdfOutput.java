package gradmatic;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import java.io.File;
import java.io.IOException;

public class PdfOutput {
    static String dest;

    /**
     * PDF OUTPUT
     * 
     * To use, simply use the line located in the example main function. The first value is the type of table, and the second is the ID to be retrieved.
     * Values allowed:
     * 0 - student file
     * 1 - section file
     * 
     * This will create either a section or student file that contains all the relevant information needed.
     * 
     * 
     */
    public static void main(String[] args) throws Exception {
        new PdfOutput().manipulatePdf(0, 920);
    }

    protected void manipulatePdf(int tableType, int ID) throws IOException {
        if (tableType == 0) dest = "./records/student_" + ID + ".pdf";
        else dest = "./records/section_" + ID;

        File file = new File(dest);
        file.getParentFile().mkdirs();

        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdf);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

        if (tableType == 0) document.add(studentTable(document, ID, bold, regular));
        else document.add(sectionTable(document, ID, bold, regular));

        document.close();
    }

    private static Table studentTable(Document document, int studentID, PdfFont titleFont, PdfFont defaultFont) {
        StudentRecord record = new StudentRecord(studentID);

        createText(document, "STUDENT RECORD", TextAlignment.CENTER, titleFont);
        createText(document, "\n", TextAlignment.CENTER, defaultFont);
        createText(document,
                "Name: " + record.student.studentLN + ", " + record.student.studentFN + " " + record.student.studentMI,
                TextAlignment.LEFT, defaultFont);
        createText(document, "Section: " + record.section.sectionName, TextAlignment.LEFT, defaultFont);
        createText(document, "\n", TextAlignment.CENTER, defaultFont);

        Table table = new Table(UnitValue.createPercentArray(6)).useAllAvailableWidth();
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // Headers
        table.addHeaderCell(tableCell("Student ID", titleFont));
        table.addHeaderCell(tableCell("1st Quarter", titleFont));
        table.addHeaderCell(tableCell("2nd Quarter", titleFont));
        table.addHeaderCell(tableCell("3rd Quarter", titleFont));
        table.addHeaderCell(tableCell("4th Quarter", titleFont));
        table.addHeaderCell(tableCell("Average", titleFont));

        // Print out list of subjects and grades for the student
        for (int i = 1; i <= record.subject.size(); i++) {
            double ave = 0;
            table.addCell(tableCell(record.subject.get(i - 1).subjectName, defaultFont));
            for (int j = 0; j < 4; j++) {
                ave += record.grade.get(j * i).studentPeriodGrade;
                table.addCell(tableCell("" + record.grade.get(j * i).studentPeriodGrade, defaultFont));
            }

            table.addCell(tableCell("" + ave / 4, defaultFont));
        }

        return table;
    }

    private static Table sectionTable(Document document, int sectionID, PdfFont titleFont, PdfFont defaultFont) {
        SectionRecord record = new SectionRecord(sectionID);

        createText(document, "SECTION RECORD", TextAlignment.CENTER, titleFont);
        createText(document, "\n", TextAlignment.CENTER, defaultFont);
        createText(document, "Section Name: " + record.section.sectionName, TextAlignment.LEFT, defaultFont);
        createText(document, "\n", TextAlignment.CENTER, defaultFont);

        Table table = new Table(UnitValue.createPercentArray(6)).useAllAvailableWidth();
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // Headers
        table.addHeaderCell(tableCell("Student ID", titleFont));
        table.addHeaderCell(tableCell("Last Name", titleFont));
        table.addHeaderCell(tableCell("First Name", titleFont));
        table.addHeaderCell(tableCell("Middle Initial", titleFont));
        table.addHeaderCell(tableCell("Grade Average", titleFont));
        table.addHeaderCell(tableCell("Passed, Failed", titleFont));

        // Print out list of subjects and grades for the student
        for (int i = 0; i < record.studentgrades.size(); i++) {
            table.addCell(tableCell("" + record.studentgrades.get(i).student.studentID, defaultFont));
            table.addCell(tableCell("" + record.studentgrades.get(i).student.studentLN, defaultFont));
            table.addCell(tableCell("" + record.studentgrades.get(i).student.studentFN, defaultFont));
            table.addCell(tableCell("" + record.studentgrades.get(i).student.studentMI, defaultFont));
            double gwa = record.studentgrades.get(i).student.studentGWA;
            table.addCell(tableCell("" + gwa, defaultFont));
            String result;
            if (gwa >= 75.0) result = "Passed";
            else result = "Failed";
            table.addCell(tableCell(result, defaultFont));
        }

        return table;
    }

    private static Paragraph createText(Document document, String text, TextAlignment align, PdfFont font) {
        Paragraph p = new Paragraph(text);
        p.setTextAlignment(align);
        p.setFont(font);
        document.add(p);

        return p;
    }

    private static Cell tableCell(String text, PdfFont font) {
        return new Cell().setFont(font).add(new Paragraph(text).setTextAlignment(TextAlignment.CENTER));
    }
}