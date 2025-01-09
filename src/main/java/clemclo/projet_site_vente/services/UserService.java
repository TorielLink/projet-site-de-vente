package clemclo.projet_site_vente.services;

import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerUser(String username, String rawPassword, String city) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Login déjà utilisé !");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setCity(city);
        user.setRole("USER");
        return userRepository.save(user);
    }

    public UserEntity updateRole(Long userId, String newRole) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));
        user.setRole(newRole);
        return userRepository.save(user);
    }

    public UserEntity getUserById(Long userId) {
        return null;
    }

    public boolean deleteUser(Long ig) {
        return true;
    }

    public List<UserEntity> getAllUsers() {
        return null;
    }
}

