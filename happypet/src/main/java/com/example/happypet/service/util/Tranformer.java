package com.example.happypet.service.util;

import lk.pubudu.app.dto.UserDTO;
import lk.pubudu.app.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Transformer {

    private final ModelMapper mapper;

    @Autowired
    public Transformer(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public User toUserEntity(UserDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }

    public UserDTO toUserDTO(User userEntity) {
        return mapper.map(userEntity, UserDTO.class);
    }
}
