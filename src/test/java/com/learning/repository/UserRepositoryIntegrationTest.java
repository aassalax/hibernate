package com.learning.repository;

import com.learning.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        User bob = new User();
        bob.setUserName("Boby");
        bob.setFirstName("Bob");
        bob.setLastName("User");
        userRepository.save(bob);

        User alice = new User();
        alice.setUserName("Aliiice");
        alice.setFirstName("Alice");
        alice.setLastName("User");
        userRepository.save(alice);
    }


    @Test
    public void testFindByUserName_UserExists() {
        User foundUser = userRepository.findByUserName("Boby");

        assertEquals("Boby", foundUser.getUserName());
        assertEquals("Bob", foundUser.getFirstName());
        assertEquals("User", foundUser.getLastName());
    }

    @Test
    public void testFindByUserName_UserDoesNotExist() {
        User foundUser = userRepository.findByUserName("nonExistingUser");

        assertNull(foundUser);
    }
}