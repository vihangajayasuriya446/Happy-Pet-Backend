package com.example.happypet.service.custom;

import lk.pubudu.app.dto.UserDTO;
import lk.pubudu.app.service.SuperService;

public interface UserService extends SuperService {

    void createNewUser(UserDTO userDTO);
}
