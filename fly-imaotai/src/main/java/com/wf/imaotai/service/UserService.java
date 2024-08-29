package com.wf.imaotai.service;

import com.wf.imaotai.entity.User;
import com.wf.imaotai.model.request.UserRequest;

import java.util.List;

public interface UserService {
    List<User> getList(UserRequest userRequest);
    User getUserByMobile(Long mobile);

    List<User> selectReservationUser();

    int update(User user);


}
