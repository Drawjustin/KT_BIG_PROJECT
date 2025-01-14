package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "complaint_comment")
public class ComplaintComment extends baseEntity {

    @Id
    @GeneratedValue
    private Long complaintCommentSeq;

    private Long complaintSeq;
    private String complaintCommentContent;

}
