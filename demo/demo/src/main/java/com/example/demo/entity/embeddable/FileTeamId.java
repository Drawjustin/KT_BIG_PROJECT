package com.example.demo.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FileTeamId implements Serializable {

    @Column(name = "file_seq")
    private Long fileSeq;

    @Column(name = "department_seq")
    private Long teamSeq;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileTeamId that = (FileTeamId) o;
        return Objects.equals(fileSeq, that.fileSeq) &&
                Objects.equals(teamSeq, that.teamSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileSeq, teamSeq);
    }
    @Builder
    public FileTeamId(Long fileSeq, Long teamSeq) {
        this.fileSeq = fileSeq;
        this.teamSeq = teamSeq;
    }
}
