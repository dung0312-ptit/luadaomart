package com.example.luadaomart.ultity;

import android.os.Environment;
import android.util.Log;

import com.example.luadaomart.model.Order;
import com.example.luadaomart.model.OrderDetail;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InvoiceGenerator {

    private List<OrderDetail> list;
    private Order order;

    public InvoiceGenerator(List<OrderDetail> list, Order order) {
        this.list = list;
        this.order = order;
    }

    public List<OrderDetail> getList() {
        return list;
    }

    public void setList(List<OrderDetail> list) {
        this.list = list;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void createInvoice () throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath,order.getTimestamps()+".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        DeviceRgb invoiceRGB = new DeviceRgb(51,204,51);
        DeviceRgb invoiceGray = new DeviceRgb(220,220,220);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date(order.getTimestamps())).trim();

        float colWidth[] ={140,140,140,140};
        Table t1 = new Table(colWidth);

        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell(1,2).add(new Paragraph("Invoice")).setBold().setFontSize(26f).setFontColor(invoiceRGB).setBorder(Border.NO_BORDER));
        //t1.addCell(new Cell().add(new Paragraph("")));

        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("Invoice No:")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph(order.getTimestamps()+"")).setBorder(Border.NO_BORDER));

        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("Invoice Date:")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph(dateString)).setBorder(Border.NO_BORDER));

        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("Customer Phone:")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph(order.getCusPhone())).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("Saler:")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph(order.getEmployeeName())).setBorder(Border.NO_BORDER));

        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph("Payment Method:")).setBorder(Border.NO_BORDER));
        t1.addCell(new Cell().add(new Paragraph(order.getMethod()==0?"Cash":"Credit Card")).setBorder(Border.NO_BORDER));

        document.add(t1);


        float colWidth2[]={62,162,112,112,112};
        Table t2 = new Table(colWidth2);

        t2.addCell(new Cell().add(new Paragraph("ID")).setFontColor(ColorConstants.WHITE).setBackgroundColor(invoiceRGB));
        t2.addCell(new Cell().add(new Paragraph("DES")).setFontColor(ColorConstants.WHITE).setBackgroundColor(invoiceRGB));
        t2.addCell(new Cell().add(new Paragraph("RATE")).setFontColor(ColorConstants.WHITE).setBackgroundColor(invoiceRGB));
        t2.addCell(new Cell().add(new Paragraph("QUAN")).setFontColor(ColorConstants.WHITE).setBackgroundColor(invoiceRGB));
        t2.addCell(new Cell().add(new Paragraph("PRICE")).setFontColor(ColorConstants.WHITE).setBackgroundColor(invoiceRGB));

        for(OrderDetail od : list) {
            t2.addCell(new Cell().add(new Paragraph(od.getId())).setBackgroundColor(invoiceGray));
            t2.addCell(new Cell().add(new Paragraph(od.getName())).setBackgroundColor(invoiceGray));
            t2.addCell(new Cell().add(new Paragraph(od.getPrice()+"")).setBackgroundColor(invoiceGray));
            t2.addCell(new Cell().add(new Paragraph(od.getAmount()+"")).setBackgroundColor(invoiceGray));
            t2.addCell(new Cell().add(new Paragraph(od.getTotal()+"")).setBackgroundColor(invoiceGray));
        }

        t2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        t2.addCell(new Cell().add(new Paragraph("TOTAL")).setBold().setFontColor(ColorConstants.WHITE).setBackgroundColor(invoiceRGB));
        t2.addCell(new Cell().add(new Paragraph(order.getTotalPrices()+"")).setBold().setFontColor(ColorConstants.WHITE).setBackgroundColor(invoiceRGB));

        document.add(new Paragraph("\n\n"));
        document.add(t2);
        document.close();

    }
}
