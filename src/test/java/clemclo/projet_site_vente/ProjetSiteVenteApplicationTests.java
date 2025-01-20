package clemclo.projet_site_vente;

import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProjetSiteVenteApplicationTests {

	@Test
	void contextLoads() {
	}


	@Autowired
	private UserRepository userRepository;

	@Test
	public void testFindByUsername() {
		UserEntity user = new UserEntity();
		user.setUsername("testuser");
		user.setPassword("testpassword");
		user.setRole("USER");

		userRepository.save(user);

		UserEntity fetchedUser = userRepository.findByUsername("testuser");
		assertNotNull(fetchedUser);
		assertEquals("testuser", fetchedUser.getUsername());
	}


}
