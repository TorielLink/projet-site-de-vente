package clemclo.projet_site_vente.configurations;

import clemclo.projet_site_vente.services.DBUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final DBUserDetailsService dbUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(DBUserDetailsService dbUserDetailsService, PasswordEncoder passwordEncoder) {
        this.dbUserDetailsService = dbUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .requiresChannel(channel -> channel
                        .anyRequest().requiresSecure())

                .authorizeHttpRequests((authz) -> authz
                        // Pages accessibles à tous (inscription, accueil, etc.)
                        .requestMatchers("/", "/login", "/register", "/images/**", "/css/**", "/js/**")
                        .permitAll()

                        // Pages réservées aux rôles spécifiques
                        .requestMatchers("/protected/admin/**").hasRole("ADMIN")  // Accès réservé aux ADMIN
                        .requestMatchers("/dashboard", "/items/**").hasAnyRole("USER", "ADMIN") // Accessible aux utilisateurs connectés

                        // Toute autre requête nécessite une authentification
                        .anyRequest().authenticated()
                )
                // Configuration de la page de connexion
                .formLogin(form -> form
                        .loginPage("/login")  // URL de la page de connexion personnalisée
                        .defaultSuccessUrl("/dashboard", true)  // Redirection après connexion réussie
                        .failureUrl("/login?error=true") // Redirect to login page with error message
                        .permitAll()
                )
                // Configuration de la déconnexion
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // Redirection après déconnexion
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/dashboard") // Redirect to this page if the user is unauthorized
                )
                .httpBasic(withDefaults())
                .headers(headers -> headers
                        .httpStrictTransportSecurity()
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000))

                .sessionManagement().sessionFixation().migrateSession();

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(dbUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider)
                .build();
    }

    // TODO : potentiellement la source du warning avec les deux trucs
    @Bean
    public UserDetailsService userDetailsService() {
        // Configuration en mémoire pour les utilisateurs de test
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User.withUsername("TorielLink")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build());

        manager.createUser(User.withUsername("clem1119")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build());

        return manager;
    }
}
