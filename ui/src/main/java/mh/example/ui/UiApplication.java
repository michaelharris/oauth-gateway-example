package mh.example.ui;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class UiApplication {

	// @GetMapping(value = "/{path:[^\\.]*}")
	// public String redirect() {
	// return "forward:/";
	// }

	// In the below - the user is null?!

	@RequestMapping("/uiuser")
	@ResponseBody
	public Map<String, String> user(Principal user) {
		return Collections.singletonMap("name", user.getName());
	}

	@RequestMapping("/uiuser2")
	@ResponseBody
	public Principal user2(Principal user) {

		return user;
	}

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
