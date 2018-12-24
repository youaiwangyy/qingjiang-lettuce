package com.qingjiang.lettuce;

import com.google.common.collect.Sets;
import com.qingjiang.lettuce.domain.User;
import com.qingjiang.lettuce.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

@Slf4j
public class UserServiceTest extends LettuceApplicationTests {

    @Autowired private UserService userService;

    @Test
    public void getUserById() {
        User user = userService.getUserById(100);
        log.info("user:{}", user);

    }

    @Test
    public void getAll() {
        Set<Integer> ids = Sets.newHashSet();
        ids.add(1001);
        ids.add(1002);
        ids.add(1003);
        Map<Integer, User> all = userService.getUsers(ids);
        log.info("all:{}", all);
    }

    @Test
    public void getUser() {
        Map<Integer, User> users = userService.getUsers(Sets.newHashSet(100, 101));
        log.info("users:{}", users);
    }

    @Test
    public void addUser() {
        User user = new User(100, "qingjiang", true);
        User user2 = new User(101, "kaka", false);
        User user3 = new User(102, "jerey", false);

        boolean bool = userService.addUser(user, user2, user3);
        log.info("bool:{}", bool);

    }



    @Test
    public void testLog() {
        log.info("info");
        log.debug("debug");
        log.error("error");
    }



}
