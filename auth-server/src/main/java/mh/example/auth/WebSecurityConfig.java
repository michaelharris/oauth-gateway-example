package mh.example.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Configuration
@EnableWebSecurity
@Order(-20)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	@Override		
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	
	@Autowired
	@Qualifier("clientDetailsService")
	private ClientDetailsService oauth2ClientDetailsService;
	
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {					
		auth.inMemoryAuthentication()
			.withUser("steve").password("password").roles("END_USER")
		.and()
			.withUser("admin").password("admin").roles("ADMIN")
			.and()
			.withUser("user").password("user").roles("USER");	
		
		 auth
         .userDetailsService(clientDetailsUserDetailsService());
	}
	
	UserDetailsService clientDetailsUserDetailsService() {
	    final ClientDetailsUserDetailsService service = new ClientDetailsUserDetailsService(oauth2ClientDetailsService);

	    return service;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {			
		http
		
			.formLogin().permitAll()
		.and()
			.requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
		.and()
			.authorizeRequests().anyRequest().authenticated();
			
//		.and().csrf().csrfTokenRepository(csrfTokenRepository())
//			.and()
//				.addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class);;
	}	
	
	private Filter csrfHeaderFilter() {
		
		System.out.println("calling header filter method");
		
		return new OncePerRequestFilter() {
			
			
			
			@Override
			protected void doFilterInternal(HttpServletRequest request,
					HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
				
				this.setBeanName("MH CSRF BEAN NAME");
				System.out.println("XSRF Filter!");
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
						.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					if (cookie == null || token != null && !token.equals(cookie.getValue())) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
}
