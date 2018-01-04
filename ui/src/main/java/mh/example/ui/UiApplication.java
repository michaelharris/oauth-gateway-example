package mh.example.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class UiApplication {

	// @GetMapping(value = "/{path:[^\\.]*}")
	// public String redirect() {
	// return "forward:/";
	// }

	// In the below - the user is null?!



	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}
	//
	// @Override
	// public void configure(HttpSecurity http) throws Exception {
	// // @formatter:off
	// http
	// .logout().logoutSuccessUrl("/").and()
	// .authorizeRequests()
	// .antMatchers("/index.html", "/app.html", "/", "/login").permitAll()
	// .anyRequest().authenticated()
	// .and()
	// .csrf()
	// .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	// // @formatter:on
	// }

}
