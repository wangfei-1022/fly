package com.wf.imaotai.service;

import com.wf.imaotai.entity.User;
import com.wf.imaotai.model.request.UserRequest;

import java.util.List;

public interface UserService {
    List<User> getList(UserRequest userRequest);
    User getUserByMobile(String mobile);

    List<User> selectReservationUser();

    List<User> selectReservationUserByMinute(int minute);

    int update(User user);

    int delete(User user);

    void updateUserMinuteBatch();

}
