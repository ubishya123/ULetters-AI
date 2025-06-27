package com.example.complaint_letter_generator.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.complaint_letter_generator.model.ComplaintRequest;
import com.example.complaint_letter_generator.service.GeminiService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

}
