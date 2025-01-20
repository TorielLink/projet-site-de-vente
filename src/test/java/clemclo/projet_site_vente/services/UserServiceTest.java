package clemclo.projet_site_vente.services;

import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldRegisterNewUser() {
        // Arrange
        String username = "testUser";
        String rawPassword = "password";
        String city = "Paris";
        String encodedPassword = "hashedPassword";

        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        // Mock the behavior
        when(userRepository.findByUsername(username)).thenReturn(null);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserEntity result = userService.registerUser(username, rawPassword, city);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals(city, result.getCity());
        assertEquals("USER", result.getRole());

        // Verify interactions
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    void registerUser_ShouldThrowException_WhenUsernameExists() {
        // Arrange
        String username = "existingUser";
        String rawPassword = "password";
        String city = "Paris";

        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        // Mock le comportement du repository
        when(userRepository.findByUsername(username)).thenReturn(new UserEntity());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.registerUser(username, rawPassword, city)
        );

        // Vérifie le message de l'exception
        assertEquals("Login déjà utilisé !", exception.getMessage());

        // Vérifie que save() n'est jamais appelé
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void updateRole_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        Long userId = 123L;
        String newRole = "ADMIN";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.updateRole(userId, newRole)
        );
        assertEquals("Utilisateur non trouvé !", exception.getMessage());

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void deleteUser_ShouldReturnTrue_WhenUserExists() {
        // Arrange
        Long userId = 123L;

        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        boolean result = userService.deleteUser(userId);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).deleteById(userId);
    }

}
