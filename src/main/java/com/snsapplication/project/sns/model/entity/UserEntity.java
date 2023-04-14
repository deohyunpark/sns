package com.snsapplication.project.sns.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
public class UserEntity {
    @Id
    private Integer id;
    @Column
    private String userName;
    @Column
    private String password;
}
