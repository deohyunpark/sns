package com.snsapplication.project.sns.repository;

import com.snsapplication.project.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    //username이 null일수도 있으니까 Optional
    Optional<UserEntity> findByUserName(String userName);
}
