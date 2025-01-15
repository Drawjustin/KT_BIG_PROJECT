package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "complaint_comment")
public class ComplaintComment extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complaintCommentSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaintSeq")
    private Complaint complaint;
    private String complaintCommentContent;

    @Builder
    public ComplaintComment(Complaint complaint, String complaintCommentContent) {
        this.complaint = complaint;
        this.complaintCommentContent = complaintCommentContent;
    }
}
