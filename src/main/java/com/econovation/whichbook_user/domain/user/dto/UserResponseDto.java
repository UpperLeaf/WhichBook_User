package com.econovation.whichbook_user.domain.user.dto;

import com.econovation.whichbook_user.domain.user.User;
import lombok.Data;

@Data
public class UserResponseDto {
    String email;
    String nickname;
    Long userId;

    public static UserResponseDto of(User user){
        if(user == null)
            return null;
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setNickname(user.getNickname());
        userResponseDto.setUserId(user.getId());
        return userResponseDto;
    }
}
