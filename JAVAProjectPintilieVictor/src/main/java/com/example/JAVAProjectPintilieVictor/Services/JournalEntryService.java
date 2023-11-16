package com.example.JAVAProjectPintilieVictor.Services;

import com.example.JAVAProjectPintilieVictor.Entities.JournalEntry;
import com.example.JAVAProjectPintilieVictor.Entities.User;
import com.example.JAVAProjectPintilieVictor.Repositories.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public List<JournalEntry> getAllEntriesForUser(User user) {
        return journalEntryRepository.findByUser(user);
    }

    public JournalEntry saveJournalEntry(JournalEntry entry) {
        return journalEntryRepository.save(entry);
    }

    public void deleteJournalEntry(Long entryId) {
        journalEntryRepository.deleteById(entryId);
    }

    public JournalEntry getJournalEntryById(Long id) {
        return journalEntryRepository.findById(id).orElse(null);
    }
}
