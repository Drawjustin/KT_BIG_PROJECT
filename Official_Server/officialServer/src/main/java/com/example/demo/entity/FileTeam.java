package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import com.example.demo.entity.embeddable.FileTeamId;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
//@SQLDelete(sql = "UPDATE file_team SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Table(name="file_team")
public class FileTeam extends baseEntity {

    @EmbeddedId
    private FileTeamId fileTeamId;

    @MapsId("fileSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_seq")
    private File file;

    @MapsId("teamSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_seq")
    private Team team;

    @Builder
    public FileTeam(FileTeamId fileTeamId, File file, Team team) {
        this.fileTeamId = fileTeamId;
        this.file = file;
        this.team = team;
    }
}
