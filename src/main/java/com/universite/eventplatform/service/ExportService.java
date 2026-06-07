package com.universite.eventplatform.service;

import com.universite.eventplatform.dto.InscriptionDTO;
import com.universite.eventplatform.entity.Event;
import com.universite.eventplatform.exception.ResourceNotFoundException;
import com.universite.eventplatform.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final EventRepository eventRepository;
    private final InscriptionService inscriptionService;

    public byte[] exportPdf(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé: " + eventId));
        List<InscriptionDTO> inscriptions = inscriptionService.getByEvent(eventId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, java.awt.Color.BLUE);
        Paragraph title = new Paragraph("Liste des participants - " + event.getTitre(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        Paragraph info = new Paragraph("Date: " + event.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                + " | Lieu: " + event.getLieu()
                + " | Total: " + inscriptions.size() + " inscriptions");
        info.setSpacingAfter(15);
        document.add(info);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell("N\u00b0");
        table.addCell("Nom");
        table.addCell("Email");
        table.addCell("Statut");

        int i = 1;
        for (InscriptionDTO ins : inscriptions) {
            table.addCell(String.valueOf(i++));
            table.addCell(ins.getEtudiantNom());
            table.addCell(ins.getEtudiantEmail());
            table.addCell(ins.getStatut().name());
        }
        document.add(table);
        document.close();

        return out.toByteArray();
    }

    public byte[] exportExcel(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé: " + eventId));
        List<InscriptionDTO> inscriptions = inscriptionService.getByEvent(eventId);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Participants - " + event.getTitre());

        Row headerRow = sheet.createRow(0);
        String[] headers = {"N\u00b0", "Nom", "Email", "Date Inscription", "Statut"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        int rowNum = 1;
        for (InscriptionDTO ins : inscriptions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum - 1);
            row.createCell(1).setCellValue(ins.getEtudiantNom());
            row.createCell(2).setCellValue(ins.getEtudiantEmail());
            row.createCell(3).setCellValue(ins.getDateInscription() != null
                    ? ins.getDateInscription().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "");
            row.createCell(4).setCellValue(ins.getStatut().name());
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try { workbook.write(out); workbook.close(); } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la g\u00e9n\u00e9ration Excel", e);
        }
        return out.toByteArray();
    }
}
