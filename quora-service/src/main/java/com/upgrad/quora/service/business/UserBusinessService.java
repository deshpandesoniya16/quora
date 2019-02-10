package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is UserBusinessService class for handling the business layer
 */
@Service
public class UserBusinessService {


    //Created instance of UserDao class
    @Autowired
    private UserDao userDao;

    //Created instance of PasswordCryptographyProvider class
    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    //Controller calls this signup method
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

        final UserEntity userByUserName = userDao.getUserByUserName(userEntity.getUserName());
        final UserEntity userByEmail = userDao.getUserByEmail(userEntity.getEmail());

        //Checking the value of getUserByUserName() to throw SignUpRestrictedException
        if(userByUserName!=null){
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }
        //Checking the value of getUserByEmail() to throw SignUpRestrictedException
        if(userByEmail!=null){
            throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
        }

        //If the information is provided by a non-existing user, then save the user information in the database with encrypted password and call createUser() present in dao layer
        String[] encryptedPassword = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedPassword[0]);
        userEntity.setPassword(encryptedPassword[1]);
        return userDao.createUser(userEntity);
    }
}
