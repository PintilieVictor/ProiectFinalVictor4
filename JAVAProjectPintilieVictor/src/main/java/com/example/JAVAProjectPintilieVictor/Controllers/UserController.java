package com.example.JAVAProjectPintilieVictor.Controllers;

import com.example.JAVAProjectPintilieVictor.Entities.JournalEntry;
import com.example.JAVAProjectPintilieVictor.Entities.User;
import com.example.JAVAProjectPintilieVictor.Services.JournalEntryService;
import com.example.JAVAProjectPintilieVictor.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/user-account")
    public String showUserAccount(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            if (user != null) {
                model.addAttribute("user", user);
                List<JournalEntry> journalEntries = journalEntryService.getAllEntriesForUser(user);
                model.addAttribute("journalEntries", journalEntries);

                int entryCount = journalEntries.size(); // Retrieve the count of journal entries
                model.addAttribute("entryCount", entryCount); // Add entry count to the model
                // Get total number of users
                Long totalUsers = userService.getTotalUsers();
                model.addAttribute("totalUsers", totalUsers);
                return "user-account";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/edit-user")
    public String showEditUserForm(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        return "edit-user";
    }

    @PostMapping("/edit-user")
    public String editUser(@ModelAttribute("user") User updatedUser, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User existingUser = userService.findByUsername(username);

            if (existingUser.getUsername().equals(username)) {
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setPassword(updatedUser.getPassword());

                userService.saveUser(existingUser);
            }
        }
        return "redirect:/user-account";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }



    @GetMapping("/journals")
    public String showJournals(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            List<JournalEntry> journalEntries = journalEntryService.getAllEntriesForUser(user);
            model.addAttribute("journalEntries", journalEntries);
        }
        return "journals";
    }

    @GetMapping("/add-journal")
    public String showAddJournalForm(Model model) {
        model.addAttribute("journalEntry", new JournalEntry());
        return "add-journal";
    }

    @PostMapping("/add-journal")
    public String addJournal(@ModelAttribute("journalEntry") JournalEntry journalEntry, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            journalEntry.setUser(user);

            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            journalEntry.setDate(date);

            journalEntryService.saveJournalEntry(journalEntry);
        }
        return "redirect:/journals";
    }

    @GetMapping("/edit-journal/{entryId}")
    public String showEditJournalForm(@PathVariable Long entryId, Model model) {
        JournalEntry journalEntry = journalEntryService.getJournalEntryById(entryId);
        model.addAttribute("journalEntry", journalEntry);
        return "edit-journal";
    }

    @PostMapping("/edit-journal")
    public String editJournal(@ModelAttribute("journalEntry") JournalEntry updatedJournalEntry, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);

            JournalEntry originalJournalEntry = journalEntryService.getJournalEntryById(updatedJournalEntry.getId());

            if (originalJournalEntry.getUser().equals(user)) {
                originalJournalEntry.setTitle(updatedJournalEntry.getTitle());
                originalJournalEntry.setContent(updatedJournalEntry.getContent());

                journalEntryService.saveJournalEntry(originalJournalEntry);
            }
        }
        return "redirect:/journals";
    }

    @GetMapping("/delete-journal/{entryId}")
    public String deleteJournal(@PathVariable Long entryId, Principal principal) {
        JournalEntry journalEntry = journalEntryService.getJournalEntryById(entryId);
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            if (journalEntry.getUser().equals(user)) {
                journalEntryService.deleteJournalEntry(entryId);
            }
        }
        return "redirect:/journals";
    }

}
