package com.merchstore.services;

import com.merchstore.dtos.UserOutDto;
import com.merchstore.dtos.UserRegisterInDto;
import com.merchstore.dtos.UserRestoreDto;
import com.merchstore.dtos.UserUpdateDto;
import com.merchstore.models.User;

public interface UserService {

    void register(UserRegisterInDto registerInDto);

    UserOutDto getCurrentUser(String name);

    UserOutDto updateProfile(UserUpdateDto userUpdateDto, String username);

    void deleteCurrentUser(String username);

    void restoreAccount(UserRestoreDto dto);
}
