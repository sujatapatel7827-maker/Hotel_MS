package com.hms.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvoiceGenerator {

    public static void generateInvoice(int bookingId, String guestName, String roomNo, double amount) {
        try {
            String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator;
            String fileName = "Invoice_B" + bookingId + "_" + System.currentTimeMillis() + ".pdf";
            File file = new File(desktopPath + fileName);

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("HOTEL MANAGEMENT SYSTEM")
                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Official Payment Receipt")
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            document.add(new Paragraph("Invoice No: INV-" + bookingId));

            Table table = new Table(2);
            table.addCell("Guest Name:");
            table.addCell(guestName);
            table.addCell("Room Number:");
            table.addCell(roomNo);
            table.addCell("Total Final Amount:");
            table.addCell(String.format("$%.2f", amount));

            document.add(table);
            document.add(new Paragraph("\n(Includes Room Charges + Extra Services)"));

            document.add(new Paragraph("\nThank you for staying with us!"));
            document.close();

            System.out.println("Invoice generated at: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
