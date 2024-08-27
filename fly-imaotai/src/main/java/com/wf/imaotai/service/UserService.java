package com.wf.imaotai.service;

import com.wf.imaotai.entity.User;

import java.util.List;

public interface UserService {
    List<User> list(User user);
    User getUserByMobile(Long mobile);

    List<User> selectReservationUser();
    int update(User user);


}
