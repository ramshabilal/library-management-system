package cs.nyuad.csuh3260.library;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SystemManagerTest {

    private SystemManager systemManager;
    private DatabaseManager dbManager;

    private void setup() {
        dbManager = mock(DatabaseManager.class);
        systemManager = new SystemManager(dbManager);
    }

    @Test
    public void testSingupSuccessfully() {
        setup();
        when(dbManager.getUsers()).thenReturn(new ArrayList<>());

        boolean status = systemManager.signup("a", "a", "a");
        verify(dbManager).addUser(any());
        assertTrue(status);
    }

    @Test
    public void testSingupWithExistingUsername() {
        setup();
        List<User> users = List.of(new User("a", "a", "a"));
        when(dbManager.getUsers()).thenReturn(users);

        boolean status = systemManager.signup("a", "a", "a");
        assertFalse(status);
    }

    @Test
    public void testLoginSuccessfully() {
        setup();
        List<User> users = List.of(new User("a", "a", "a"));
        when(dbManager.getUsers()).thenReturn(users);

        boolean status = systemManager.login("a", "a");
        assertTrue(status);
    }

    @Test
    public void testLoginFail() {
        setup();
        List<User> users = List.of(new User("a", "a", "a"));
        when(dbManager.getUsers()).thenReturn(users);

        boolean status = systemManager.login("b", "b");
        assertFalse(status);
    }

}
