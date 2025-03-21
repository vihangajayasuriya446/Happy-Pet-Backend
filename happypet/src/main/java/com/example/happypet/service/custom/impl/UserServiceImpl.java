package com.example.happypet.service.custom.impl;

import lk.pubudu.app.dto.UserDTO;
import lk.pubudu.app.entity.User;
import lk.pubudu.app.repository.UserRepository;
import lk.pubudu.app.service.custom.UserService;
import lk.pubudu.app.service.util.Transformer;
import lk.pubudu.app.util.EMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Transformer transformer;
    private final EMailSender eMailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Transformer transformer, EMailSender eMailSender) {
        this.userRepository = userRepository;
        this.transformer = transformer;
        this.eMailSender = eMailSender;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createNewUser(UserDTO userDTO) {
        Optional<User> user = userRepository.findById(userDTO.getEmail());
        if (user.isPresent()) throw new DuplicateKeyException("A user is already exists with this email address");
        userRepository.save(transformer.toUserEntity(userDTO));
        eMailSender.sendMail(userDTO.getEmail(), "Confirmation", "User: ".concat(userDTO.getName()).concat(" has successfully saved to the database"));
    }
}