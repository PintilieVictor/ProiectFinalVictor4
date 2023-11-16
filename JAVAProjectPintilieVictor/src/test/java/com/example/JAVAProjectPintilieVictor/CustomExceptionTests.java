package com.example.JAVAProjectPintilieVictor;

import com.example.JAVAProjectPintilieVictor.Exceptions.UserNotFoundException;
import com.example.JAVAProjectPintilieVictor.Exceptions.UsernameAlreadyExistsException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CustomExceptionTests {

    @Test
    public void testUsernameAlreadyExistsException() {
        try {
            throw new UsernameAlreadyExistsException("Username is already taken.");
        } catch (UsernameAlreadyExistsException e) {
            assertEquals("Username is already taken.", e.getMessage());
        }
    }

    @Test
    public void testUserNotFoundException() {
        try {
            throw new UserNotFoundException("User not found.");
        } catch (UserNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
        }
    }
}

