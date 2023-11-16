package com.example.JAVAProjectPintilieVictor.Controllers;

import com.example.JAVAProjectPintilieVictor.Entities.JournalEntry;
import com.example.JAVAProjectPintilieVictor.Entities.User;
import com.example.JAVAProjectPintilieVictor.Services.JournalEntryService;
import com.example.JAVAProjectPintilieVictor.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/entries")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String listEntries(Model model) {
        // Retrieve and display journal entries for the current user
        User currentUser = getCurrentUser();
        List<JournalEntry> entries = journalEntryService.getAllEntriesForUser(currentUser);
        model.addAttribute("entries", entries);
        return "entry/list";
    }

    @GetMapping("/create")
    public String createEntryForm(Model model) {
        model.addAttribute("entry", new JournalEntry());
        return "entry/create";
    }

    @PostMapping("/create")
    public String createEntry(@ModelAttribute("entry") JournalEntry entry) {
        // Save the new journal entry
        User currentUser = getCurrentUser();
        entry.setUser(currentUser);
        journalEntryService.saveJournalEntry(entry);
        return "redirect:/entries";
    }

    @PostMapping("/delete/{entryId}")
    public String deleteEntry(@PathVariable Long entryId) {
        // Delete a journal entry
        journalEntryService.deleteJournalEntry(entryId);
        return "redirect:/entries";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userService.findByUsername(userDetails.getUsername());
        }
        return null;
    }
}





