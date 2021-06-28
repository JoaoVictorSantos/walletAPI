package com.wallet.util;

import com.wallet.entity.User;
import com.wallet.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Util {

    public static UserService staticService;

    public Util(UserService service){
        Util.staticService = service;
    }

    public static Long getAuthenticatedUserId() {
        try{
            Optional<User> optionalUser = staticService.findByEmail(SecurityContextHolder.getContext()
                    .getAuthentication().getName());
            if(optionalUser.isPresent()){
                return optionalUser.get().getId();
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }
}
