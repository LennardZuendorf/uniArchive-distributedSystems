package de.berlin.htw.control;

import de.berlin.htw.entity.dao.UserRepository;
import de.berlin.htw.entity.dto.UserEntity;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class UserController {

    @Inject
    UserRepository repository;


    public boolean validateBalance(Float total, Integer userId) {
        UserEntity user = repository.findUserById(userId);
        return user.getBalance() >= total;
    }

    public void lowerBalance(Float deduction, Integer userId) {
        UserEntity user = repository.findUserById(userId);
        user.setBalance(user.getBalance() - deduction);
        repository.updateUser(user);
    }
}
