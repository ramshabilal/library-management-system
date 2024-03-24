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

    @Test
    public void testSingupSuccessfully() {
        DatabaseManager dbManager = mock(DatabaseManager.class);
        when(dbManager.getUsers()).thenReturn(new ArrayList<User>());
        systemManager = new SystemManager(dbManager);

        boolean status = systemManager.signup("a", "a", "a");
        verify(dbManager).addUser(any());
        assertTrue(status);
    }

    @Test
    public void testSingupWithExistingUsername() {
        DatabaseManager dbManager = mock(DatabaseManager.class);
        List<User> users = List.of(new User("a", "a", "a"));
        when(dbManager.getUsers()).thenReturn(users);
        systemManager = new SystemManager(dbManager);

        boolean status = systemManager.signup("a", "a", "a");
        assertFalse(status);
    }

    @Test
    public void testLoginSuccessfully() {
        DatabaseManager dbManager = mock(DatabaseManager.class);
        List<User> users = List.of(new User("a", "a", "a"));
        systemManager = new SystemManager(dbManager);
        when(dbManager.getUsers()).thenReturn(users);

        boolean status = systemManager.login("a", "a");
        assertTrue(status);
    }

    @Test
    public void testLoginFail() {
        DatabaseManager dbManager = mock(DatabaseManager.class);
        List<User> users = List.of(new User("a", "a", "a"));
        systemManager = new SystemManager(dbManager);
        when(dbManager.getUsers()).thenReturn(users);

        boolean status = systemManager.login("b", "b");
        assertFalse(status);
    }

}
