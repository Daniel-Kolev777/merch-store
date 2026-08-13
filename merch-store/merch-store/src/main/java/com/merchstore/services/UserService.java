package com.merchstore.services;

import com.merchstore.dtos.user.UserOutDto;
import com.merchstore.dtos.user.UserRegisterInDto;
import com.merchstore.dtos.user.UserRestoreDto;
import com.merchstore.dtos.user.UserUpdateDto;
import com.merchstore.models.User;

public interface UserService {

    void register(UserRegisterInDto registerInDto);

    UserOutDto getCurrentUser(String name);

    UserOutDto updateProfile(UserUpdateDto userUpdateDto, String username);

    void deleteCurrentUser(String username);

    void restoreAccount(UserRestoreDto dto);

    User getUserByUsername(String username);
}
