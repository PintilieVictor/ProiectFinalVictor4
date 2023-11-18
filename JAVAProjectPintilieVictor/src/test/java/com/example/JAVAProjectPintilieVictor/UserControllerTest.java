package com.example.JAVAProjectPintilieVictor;

import com.example.JAVAProjectPintilieVictor.Controllers.UserController;
import com.example.JAVAProjectPintilieVictor.Entities.JournalEntry;
import com.example.JAVAProjectPintilieVictor.Entities.User;
import com.example.JAVAProjectPintilieVictor.Services.JournalEntryService;
import com.example.JAVAProjectPintilieVictor.Services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JournalEntryService journalEntryService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Principal principal;

    @Mock
    private Model model;


    @Test
    public void testShowUserAccountRedirectsToLoginWhenPrincipalIsNull() {
        // Arrange
        when(principal.getName()).thenReturn(null);

        // Act
        String viewName = userController.showUserAccount(model, principal);

        // Assert
        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testShowUserAccountReturnsUserAccountPageWhenPrincipalIsNotNull() {
        // Arrange
        User user = new User();
        when(principal.getName()).thenReturn("testUsername");
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(journalEntryService.getAllEntriesForUser(user)).thenReturn(Collections.emptyList());

        // Act
        String viewName = userController.showUserAccount(model, principal);

        // Assert
        assertEquals("user-account", viewName);
        verify(model).addAttribute(eq("user"), eq(user));
    }

    @Test
    public void testEditUserUpdatesUserInfoAndRedirectsToUserAccount() {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("newPassword");

        when(principal.getName()).thenReturn(existingUser.getUsername());
        when(userService.findByUsername(existingUser.getUsername())).thenReturn(existingUser);

        // Act
        String viewName = userController.editUser(updatedUser, principal);

        // Assert
        assertEquals("redirect:/user-account", viewName);
        verify(userService).saveUser(existingUser);
        assertEquals("updatedUser", existingUser.getUsername());
        assertEquals("newPassword", existingUser.getPassword());
    }

    @Test
    public void testShowJournals() {
        Model model = mock(Model.class);
        Principal principal = mock(Principal.class);
        User user = new User();
        user.setUsername("username");

        List<JournalEntry> journalEntries = new ArrayList<>();
        journalEntries.add(new JournalEntry());
        when(principal.getName()).thenReturn("username");
        when(userService.findByUsername("username")).thenReturn(user);
        when(journalEntryService.getAllEntriesForUser(user)).thenReturn(journalEntries);

        String viewName = userController.showJournals(model, principal);

        verify(model).addAttribute(eq("journalEntries"), anyList());
        assertEquals("journals", viewName);
    }

    @Test
    public void testShowAddJournalForm() {
        Model model = mock(Model.class);

        String viewName = userController.showAddJournalForm(model);

        verify(model).addAttribute(eq("journalEntry"), any(JournalEntry.class));
        assertEquals("add-journal", viewName);
    }

    @Test
    public void testAddJournal() {
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setTitle("New Title");
        journalEntry.setContent("New Content");

        User user = new User();
        user.setUsername("username");

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username"); // Replace with an actual username
        when(userService.findByUsername("username")).thenReturn(user);
        when(journalEntryService.saveJournalEntry(any(JournalEntry.class))).thenReturn(journalEntry);

        // Act
        String viewName = userController.addJournal(journalEntry, principal);

        // Assert
        verify(journalEntryService).saveJournalEntry(journalEntry);
        assertEquals("redirect:/journals", viewName);
        assertEquals(user, journalEntry.getUser());
    }


    @Test
    public void testDeleteJournal() {
        Long entryId = 1L;
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setId(entryId);

        when(journalEntryService.getJournalEntryById(entryId)).thenReturn(journalEntry);
        when(principal.getName()).thenReturn("username");

        String viewName = userController.deleteJournal(entryId, principal);

        verify(journalEntryService).deleteJournalEntry(entryId);
        assertEquals("redirect:/journals", viewName);
    }

    @Test
    public void testEditJournal() {
        JournalEntry updatedJournalEntry = new JournalEntry();
        updatedJournalEntry.setId(1L);
        updatedJournalEntry.setTitle("Updated Title");
        updatedJournalEntry.setContent("Updated Content");

        JournalEntry originalJournalEntry = new JournalEntry();
        originalJournalEntry.setId(1L);
        originalJournalEntry.setUser(new User()); // Replace with an actual user
        originalJournalEntry.setTitle("Original Title");
        originalJournalEntry.setContent("Original Content");

        when(journalEntryService.getJournalEntryById(1L)).thenReturn(originalJournalEntry);
        when(principal.getName()).thenReturn("username"); // Replace with an actual username
        when(userService.findByUsername("username")).thenReturn(new User()); // Replace with an actual user

        String viewName = userController.editJournal(updatedJournalEntry, principal);

        verify(journalEntryService).saveJournalEntry(originalJournalEntry);
        assertEquals("redirect:/journals", viewName);
        assertEquals("Updated Title", originalJournalEntry.getTitle());
        assertEquals("Updated Content", originalJournalEntry.getContent());
    }
}



