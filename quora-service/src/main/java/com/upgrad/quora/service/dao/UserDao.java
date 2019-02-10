package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * This is UserDao class for handling the data layer
 */
@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    //If the information is provided by a non-existing user, then save the user information in the database
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return  userEntity;
    }

    //To check If the username provided already exists in the current database
    public UserEntity getUserByUserName(final String userName) {
       try{
        return entityManager.createNamedQuery("userByUserName",UserEntity.class).setParameter("userName",userName).getSingleResult();
       }catch (NoResultException nre){
           return null;
       }
    }

    //To check If the email provided already exists in the current database
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }
}
