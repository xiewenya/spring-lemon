package com.naturalprogrammer.spring.lemon.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalprogrammer.spring.lemon.LemonService;
import com.naturalprogrammer.spring.lemon.domain.AbstractUser;

/**
 * Authentication success handler for sending the response
 * to the client after successful authentication. This would replace
 * the default handler of Spring Security
 * 
 * @author Sanjay Patel
 */
public class AuthenticationSuccessHandler
	extends SimpleUrlAuthenticationSuccessHandler {
	
	private static final Log log = LogFactory.getLog(AuthenticationSuccessHandler.class);
	
    private ObjectMapper objectMapper;    
    private LemonService<?,?> lemonService;
    
	public AuthenticationSuccessHandler(ObjectMapper objectMapper, LemonService<?, ?> lemonService) {
		
		this.objectMapper = objectMapper;
		this.lemonService = lemonService;
		
		log.info("Created");
	}

	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request,
    		HttpServletResponse response,
            Authentication authentication)
    throws IOException, ServletException {

        // Instead of handle(request, response, authentication),
		// the statements below are introduced
    	response.setStatus(HttpServletResponse.SC_OK);
    	response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    	// get the current-user
    	AbstractUser<?,?> currentUser = lemonService.userForClient();

    	// write current-user data to the response  
    	response.getOutputStream().print(
    			objectMapper.writeValueAsString(currentUser));

    	// as done in the base class
    	clearAuthenticationAttributes(request);
        
        log.debug("Authentication succeeded for user: " + currentUser);        
    }
}
