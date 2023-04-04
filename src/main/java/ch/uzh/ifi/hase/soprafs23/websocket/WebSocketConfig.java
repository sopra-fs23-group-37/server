package ch.uzh.ifi.hase.soprafs23.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
    
    private static final String WEBSOCKET_PREFIX = "/topic";
    private static final String WEBSOCKET_SUFFIX = "/websocket";
    private static final String DESTINATION_PREFIX = "/app";
    private static final String ORIGIN_LOCALHOST = "http://localhost:3000";
    private static final String ORIGIN_PROD = "https://sopra-fs23-group-37-client.oa.r.appspot.com/";

    @Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker(WEBSOCKET_PREFIX);
        config.setApplicationDestinationPrefixes(DESTINATION_PREFIX);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(WEBSOCKET_SUFFIX)
            .setAllowedOrigins(ORIGIN_LOCALHOST, ORIGIN_PROD)
            .withSockJS();
	}

}
