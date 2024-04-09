package com.cardapi.card.config;

import com.cardapi.card.model.User;
import com.cardapi.card.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class LoadUser {
    private final UserRepository userRepository;
    private final ApplicationConfigs applicationConfigs;

    public LoadUser(UserRepository userRepository, ApplicationConfigs applicationConfigs) {
        this.userRepository = userRepository;
        this.applicationConfigs = applicationConfigs;
    }
    @PostConstruct
    private void saveUsers(){
        var userOne=new User();
        userOne.setEmail("userOne@gmail.com");
        userOne.setRole(applicationConfigs.getAdmin());
        userOne.setPassword("1224aunnd");

        var userTwo=new User();
        userTwo.setEmail("userTwo@gmail.com");
        userTwo.setRole(applicationConfigs.getMember());
        userTwo.setPassword("567jslll");

        var userThree=new User();
        userThree.setEmail("userThree@gmail.com");
        userThree.setRole(applicationConfigs.getAdmin());
        userThree.setPassword("6788988djdkkd");

        var userFour=new User();
        userFour.setEmail("userFour@gmail.com");
        userFour.setRole(applicationConfigs.getMember());
        userFour.setPassword("909098fhhd");

        var userList=new ArrayList<User>();
        userList.add(userOne);
        userList.add(userTwo);
        userList.add(userThree);
        userList.add(userFour);

        userRepository.saveAll(userList);




    }
}
