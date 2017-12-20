package mh.example.gateway.demogateway;


import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;


@SpringBootApplication
@Controller
@EnableZuulProxy

//@EnableRedisHttpSession
//try another type of session here....
//@EnableSpringHttpSession
public class DemoGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGatewayApplication.class, args);
    }
    
    @RequestMapping("/proxyuser")
	@ResponseBody
	public Principal user(Principal user) {
		
		System.out.println(user.toString());
		return user;
	}

    
    
    
    @Configuration
    //@EnableWebSecurity
    @EnableOAuth2Sso
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
        	.authorizeRequests()
        	//Allow access to all static resources without authentication
        	.antMatchers("/","/**/*.html","/index.html", "/app.html","/login","/public/index.html").permitAll()
        	.anyRequest().authenticated()
        	.and()
				.csrf().csrfTokenRepository(csrfTokenRepository())
			.and()
				.addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class);
			System.out.println("added filters");
			
			
			  http
		      .formLogin()
		      .defaultSuccessUrl("/")
		      .loginPage("/login")
		      .permitAll()
		      .and()
		      .logout()
		      .logoutSuccessUrl("/logout")
		      .permitAll();
			  
//
			
//			http
//            .authorizeRequests()
//                .antMatchers("/public/**").permitAll();
        	//.anyRequest().authenticated()
        	
//        	.and()
//       
//				.csrf().csrfTokenRepository(csrfTokenRepository())
//			.and()
//				.addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class);
	
			//CookieCsrfTokenRepository.withHttpOnlyFalse()
			//http.httpBasic().disable();
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
}

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class WorkaroundRestTemplateCustomizer implements UserInfoRestTemplateCustomizer {

	@Override
	public void customize(OAuth2RestTemplate template) {
		template.setInterceptors(new ArrayList<>(template.getInterceptors()));
	}

	@Bean
	public static RefreshScope refreshScope() {
	    RefreshScope refresh = new RefreshScope();
	    refresh.setId("newapplication:1");
	    return refresh;
	}
	
	
}


