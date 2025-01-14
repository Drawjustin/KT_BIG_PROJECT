package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "block")
public class Block extends baseEntity {

    @Id
    @GeneratedValue
    private Long blockSeq;

    private Long memberSeq;
    private Long departmentSeq;

}
