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
@Entity
@NoArgsConstructor
//@SQLDelete(sql = "UPDATE block SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Table(name = "block")
public class Block extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_seq")
    private Long blockSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_seq", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;


    @Builder
    public Block(Team team, Member member) {
        this.team = team;
        this.member = member;
    }
}
