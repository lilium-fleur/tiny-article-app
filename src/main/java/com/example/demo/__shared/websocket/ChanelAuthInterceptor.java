package com.example.demo.__shared.websocket;

import com.example.demo.__shared.jwt.JwtTokenUtil;
import com.example.demo.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class ChanelAuthInterceptor implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetails customUserDetails;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())
        || StompCommand.SEND.equals(accessor.getCommand())) {
            List<String> authorization = accessor.getNativeHeader("Authorization");
            if(authorization != null && !authorization.isEmpty()){
                String token = authorization.get(0);

                if(token != null && token.startsWith("Bearer ")){
                    token = token.substring(7);

                    try{
                        String username = jwtTokenUtil.getUsername(token);
                        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

                        if(userDetails != null){
                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(auth);
                             accessor.setUser(auth);
                        }
                    } catch(Exception e){
                        throw new MessagingException("Unauthorized");
                    }
                }
            }
        }
        return message;
    }
}
