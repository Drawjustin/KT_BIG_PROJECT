package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
//@SQLDelete(sql = "UPDATE complaint_comment SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Table(name = "complaint_comment")
public class ComplaintComment extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_comment_seq")
    private Long complaintCommentSeq;

    // Complaint와의 관계 (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_seq", nullable = false)
    private Complaint complaint;

    @Column(name = "complaint_comment_content", columnDefinition = "TEXT", nullable = false)
    private String complaintCommentContent;

    @Builder
    public ComplaintComment(Complaint complaint, String complaintCommentContent) {
        this.complaint = complaint;
        this.complaintCommentContent = complaintCommentContent;
    }

}
