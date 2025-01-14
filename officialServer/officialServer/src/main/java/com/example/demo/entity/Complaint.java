package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "complaint")
public class Complaint extends baseEntity {

    @Id
    @GeneratedValue
    private Long complaintSeq;

    private Long userSeq;
    private Long memberSeq;
    private Long departmentSeq;

    private String complaintTitle;
    private String complaintContent;
    private String complaintFilePath;
}
