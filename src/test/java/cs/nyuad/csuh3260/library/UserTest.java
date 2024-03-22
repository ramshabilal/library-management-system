package cs.nyuad.csuh3260.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("John Doe", "johndoe", "john123");
    }

    @Test
    public void testUserCreation() {
        // user 1 is user created in setUp()
        String id = UUID.randomUUID().toString();
       User user2 = new User(id, "Tom Smith", "tomsmith", "tom123");
       User user3 = new User("Sara Johnson", "sarajohnson", "sara123");

        // Check that created users are not null
        assertNotNull(user);
        assertNotNull(user2);
        assertNotNull(user3);

        // Check that created user IDs are not null
        assertNotNull("User ID should not be null", user.getId());
        assertEquals(false, user.getId().isEmpty()); 

        assertNotNull("User ID should not be null", user2.getId());
        assertEquals(false, user2.getId().isEmpty());

        assertNotNull("User ID should not be null", user3.getId());
        assertEquals(false, user3.getId().isEmpty());

        // Check that created user IDs are unique
        assertNotEquals(user.getId(), user2.getId());
        assertNotEquals(user.getId(), user3.getId());
        assertNotEquals(user2.getId(), user3.getId());        
    }

    @Test
    public void testGetters() {
        assertEquals("John Doe", user.getName());
        assertEquals("johndoe", user.getUsername());
        assertEquals("john123", user.getPassword());
    }

    @Test
    public void testSetName() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("jane123");
        assertEquals("jane123", user.getPassword());
    }



}