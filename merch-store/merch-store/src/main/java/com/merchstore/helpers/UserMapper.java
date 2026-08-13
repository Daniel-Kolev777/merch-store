package com.merchstore.helpers;

import com.merchstore.dtos.user.UserOutDto;
import com.merchstore.dtos.user.UserRegisterInDto;
import com.merchstore.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User fromRegisterInDtoToUser(UserRegisterInDto registerInDto){
        return new User(
                registerInDto.getUsername(),
                registerInDto.getEmail(),
                registerInDto.getPassword()
        );
    }

    public UserOutDto fromUserToOutDto(User user){
        return new UserOutDto(user.getUsername(),
                user.getEmail(),
                user.getRole());
    }
}
