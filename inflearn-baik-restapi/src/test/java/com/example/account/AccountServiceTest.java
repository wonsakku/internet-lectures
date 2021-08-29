package com.example.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

	@Autowired AccountService accountService;
	@Autowired AccountRepository accountRepository;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	
	@Test
	public void findByUsername() {

		// given
		
		String email = "sakku@email.com";
		String password = "12345";
		
		Account account = Account.builder()
				.email(email)
				.password(password)
				.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
				.build();
				
		this.accountRepository.save(account);
		
		//when 
		UserDetailsService userDetailsService = (UserDetailsService) accountService;
		UserDetails userDetails = userDetailsService.loadUserByUsername("sakku@email.com");

		// then
		assertThat(userDetails.getPassword()).isEqualTo(password);
	}
	
	
	@Test(expected = UsernameNotFoundException.class)
	public void findByUsernameFail() {
		String email = "random@email.com";
		accountService.loadUserByUsername(email);
	}
	
	
	@Test
	public void findByUsernameFail2() {
		String email = "random@email.com";
		try {
			accountService.loadUserByUsername(email);
			fail("supposed to be failed");
		}catch (UsernameNotFoundException e) {
			assertThat(e instanceof UsernameNotFoundException);
			assertThat(e.getMessage().contains(email));
		}
	}
	
	
	@Test
	public void findByUsernameFail3() {
		
		// Expected
		String email = "random@email.com";
		expectedException.expect(UsernameNotFoundException.class);
		expectedException.expectMessage(Matchers.containsString(email));
		
		// When
		accountService.loadUserByUsername(email);
	}
	
}

















