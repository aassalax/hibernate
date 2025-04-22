package com.learning.repository;

import com.learning.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testUpdateUser() {
        User bob = userRepository.findByUserName("Boby");
        bob.setFirstName("Bobby");
        userRepository.save(bob);

        User foundUser = userRepository.findByUserName("Boby");
        assertEquals("Bobby", foundUser.getFirstName());
    }

    @Test
    public void testDeleteUser() {
        User bob = userRepository.findByUserName("Boby");
        userRepository.delete(bob);

        User foundUser = userRepository.findByUserName("Boby");
        assertNull(foundUser);
    }

    @Test
    @Disabled
    public void testPreLoadAnnotation(){
        User bob = userRepository.findByUserName("Boby");
        assertEquals("Bob User", bob.getFullName());
    }

    @Test
    @Disabled
    public void testPreLoadAnnotation_2(){
        User bob = userRepository.findByUserName("Boby");
        Integer bobId = bob.getId();

        Optional<User> foundUser = userRepository.findById(bobId); //Find by id but still not work !
        assertTrue(foundUser.isPresent());
        assertEquals("Bob User", foundUser.get().getFullName());
    }
}