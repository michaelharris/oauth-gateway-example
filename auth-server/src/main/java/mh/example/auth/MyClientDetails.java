package mh.example.auth;

import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class MyClientDetails extends ClientDetailsUserDetailsService  {

	public MyClientDetails(ClientDetailsService clientDetailsService) {
		super(clientDetailsService);
		// TODO Auto-generated constructor stub
	}
	

}
