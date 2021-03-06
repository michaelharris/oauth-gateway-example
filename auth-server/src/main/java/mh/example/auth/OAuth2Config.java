package mh.example.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 * based on an example by Filip Lindby
 *
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
		.authenticationManager(this.authenticationManager)					
		//.tokenStore());	
	.tokenStore(new InMemoryTokenStore());
		
	
		//invalidate the user session in the authserver as soon as a token is granted 
		// Described: https://github.com/spring-guides/tut-spring-security-and-angular-js/tree/master/oauth2-logout
		  endpoints.addInterceptor(new HandlerInterceptorAdapter() {
		    @Override
		    public void postHandle(HttpServletRequest request,
		        HttpServletResponse response, Object handler,
		        ModelAndView modelAndView) throws Exception {
		      if (modelAndView != null
		          && modelAndView.getView() instanceof RedirectView) {
		        RedirectView redirect = (RedirectView) modelAndView.getView();
		        String url = redirect.getUrl();
		        if (url.contains("code=") || url.contains("error=")) {
		          HttpSession session = request.getSession(false);
		          if (session != null) {
		            session.invalidate();
		          }
		        }
		      }
		    }
		  });
		}
	
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer)
			throws Exception {
		oauthServer
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient("ui1")
			.secret("ui1-secret")
			.authorities("ROLE_TRUSTED_CLIENT")
			.authorizedGrantTypes("authorization_code", "refresh_token")
			.scopes("ui1.read")
			.autoApprove(true)		
		.and()
			.withClient("ui2")
			.secret("ui2-secret")
			.authorities("ROLE_TRUSTED_CLIENT")
			.authorizedGrantTypes("authorization_code", "refresh_token")
			.scopes("ui2.read", "ui2.write")
			.autoApprove(true)
		.and()
			.withClient("mobile-app")			
			.authorities("ROLE_CLIENT")
			.authorizedGrantTypes("implicit", "refresh_token")
			.scopes("read")
			.autoApprove(true)
		.and()
			.withClient("customer-integration-system")	
			.secret("1234567890")
			.authorities("ROLE_CLIENT")
			.authorizedGrantTypes("client_credentials")
			.scopes("read")
			.autoApprove(true);
	}

	
		
//	@Bean
//	public JdbcTokenStore tokenStore() {
//		return new JdbcTokenStore(dataSource);
//	}
	
//	@Bean
//	public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
//	    return new RedisTokenStore(redisConnectionFactory);
//	}
}
