package com.example.complaint_letter_generator.controller;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.complaint_letter_generator.model.ComplaintRequest;
import com.example.complaint_letter_generator.service.GeminiService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Controller
public class ComplaintController {

    @Autowired
    private GeminiService geminiService;

    @GetMapping("/")
    public String showForm(@NotNull Model model) {
        model.addAttribute("complaintRequest", new ComplaintRequest());
        return "index";
    }

    @PostMapping("/generate")
    public String generateLetter(@ModelAttribute @NotNull ComplaintRequest complaintRequest, @NotNull Model model) {
        String generatedLetter = geminiService.generateComplaintLetter(
                complaintRequest.getProblemDescription(),
                complaintRequest.getTone()
        );
        model.addAttribute("letter", generatedLetter);
        return "result";
    }

    @PostMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(@RequestParam("letter") String letterText) {
        try {
            // 1. Create PDF in memory
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph(letterText));
            document.close();

            // 2. Convert to byte array and build response
            byte[] pdfBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "generated_letter.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
    }




}
