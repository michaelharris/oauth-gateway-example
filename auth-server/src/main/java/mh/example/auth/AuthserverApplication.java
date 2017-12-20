package mh.example.auth;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;


@SpringBootApplication
@EnableResourceServer
@Controller
//@EnableRedisHttpSession
@SessionAttributes("authorizationRequest")
public class AuthserverApplication {

	@RequestMapping("/user")
	@ResponseBody
	public Principal user(Principal user) {
		return user;
	}
	@Autowired
	@Qualifier("clientDetailsService")
	private ClientDetailsService oauth2ClientDetailsService;
	
	@RequestMapping(value = "/user2", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getInfo(OAuth2Authentication auth) {
		return auth.getPrincipal();
		
		//auth.getUserAuthentication().g
	   // return oauth2ClientDetailsService.getUser(auth); // Returns User Object
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}
	
	
	@Bean
	public static RefreshScope refreshScope() {
	    RefreshScope refresh = new RefreshScope();
	    refresh.setId("newapplication:1");
	    return refresh;
	}
	
		
	
}



