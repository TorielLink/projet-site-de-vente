package clemclo.projet_site_vente.configurations;

import clemclo.projet_site_vente.services.DBUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SecurityConfigTest {

    @Mock
    private DBUserDetailsService dbUserDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private SecurityConfig securityConfig;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(securityConfig).build();
    }

    @Test
    @WithMockUser(username = "TorielLink")
    public void userShouldNotBeAbleToAccessAdminPage_WhenAccessingProtectedAdminPage() throws Exception {
        // Test que l'utilisateur avec le rôle USER ne peut pas accéder à une page réservée aux ADMIN
        mockMvc.perform(get("/protege/admin/secret"))
                .andExpect(status().isForbidden());  // Devrait retourner un 403 (Forbidden)
    }

    @Test
    @WithMockUser(username = "clem1119", roles = {"ADMIN"})
    public void adminShouldBeAbleToAccessAdminPage_WhenAccessingProtectedAdminPage() throws Exception {
        // Test que l'utilisateur avec le rôle ADMIN peut accéder à la page protégée par ADMIN
        mockMvc.perform(get("/protege/admin/secret"))
                .andExpect(status().isOk());  // Devrait retourner un 200 (OK)
    }

    @Test
    @WithMockUser(username = "TorielLink")
    public void userShouldBeAbleToAccessUserPage_WhenAccessingProtectedUserPage() throws Exception {
        // Test que l'utilisateur avec le rôle USER peut accéder à une page réservée aux USER
        mockMvc.perform(get("/protege/user/profile"))
                .andExpect(status().isOk());  // Devrait retourner un 200 (OK)
    }
}
