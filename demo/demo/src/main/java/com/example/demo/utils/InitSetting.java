package com.example.demo.utils;

import com.example.demo.entity.*;
import com.example.demo.entity.embeddable.FileTeamId;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InitSetting {
    private final InitService initService;

    @PostConstruct
    public void init(){initService.init();}

    @Component
    static class InitService{
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init(){
            InitOranization();
            InitDepartment();
            InitTeam();
            InitAdmin();
            InitUser();
            InitChatBox();
            InitChat();
            InitFile();
            InitTelecom();
            InitFileTeam();
            InitMember();
            InitBlock();
            InitComplaint();
            InitComplaintComment();
        }

        private void InitTeam() {
            Department department = em.find(Department.class,1L);
            Team team = Team.builder()
                    .teamName("BackEnd팀")
                    .department(department)
                    .build();
            em.persist(team);
        }


        private void InitOranization() {
            Organization organization = Organization.builder()
                    .organizationName("성남시청")
                    .build();
            em.persist(organization);
        }

        private void InitDepartment() {
            Organization organization = em.find(Organization.class,1L);
            Department department = Department.builder()
                    .departmentName("KT")
                    .organization(organization)
                    .build();
            em.persist(department);
        }

        private void InitTelecom() {
            Telecom teleContent = Telecom.builder()
                    .telecomContent("teleContent")
                    .telecomCount((byte) 2)
                    .telecomFilePath("C:\\HOME\\BACK\\IWANT")
                    .isComplain(false)
                    .build();
            em.persist(teleContent);
        }

        private void InitMember() {
            Member member = Member.builder()
                    .memberId("guswhd903")
                    .memberName("현종")
                    .memberPassword("1234")
                    .memberEmail("guswhd903@naver.com")
                    .build();
            em.persist(member);
        }

        private void InitFileTeam() {
            File file = em.find(File.class, 1L);
            Team team = em.find(Team.class,1L);

            FileTeamId fileTeamId = FileTeamId.builder()
                    .fileSeq(file.getFileSeq())
                    .teamSeq(team.getTeamSeq())
                    .build();

            if (em.find(FileTeam.class, fileTeamId) != null) return;

            FileTeam fileTeam = FileTeam.builder()
                    .fileTeamId(fileTeamId)
                    .file(file)
                    .team(team)
                    .build();
            em.persist(fileTeam);
        }

        private void InitFile() {
            Admin admin = em.find(Admin.class, 1L);
            File file = File.builder()
                    .admin(admin)
                    .fileContent("fileContent")
                    .filePath("filePath")
                    .fileTitle("fileTitle")
                    .fileType("fileType")
                    .build();
            em.persist(file);
        }

        private void InitComplaintComment() {
            Complaint complaint = em.find(Complaint.class, 1L);
            ComplaintComment complaintComment = ComplaintComment.builder()
                    .complaint(complaint)
                    .complaintCommentContent("complaintCommentContent")
                    .build();
            em.persist(complaintComment);
        }

        private void InitComplaint() {
            Team team = em.find(Team.class,1L);
            Member member = em.find(Member.class, 1L);
            Complaint complaint = Complaint.builder()
                    .complaintContent("complaintContent")
                    .complaintTitle("complainTitle")
                    .complaintFilePath("complainFilePath")
                    .team(team)
                    .member(member)
                    .build();
            em.persist(complaint);
        }

        private void InitChatBox() {
            ChatBox chatBox = ChatBox.builder()
                    .chatBoxTitle("chatBoxTitle")
                    .build();
            em.persist(chatBox);
        }

        private void InitChat() {
            ChatBox chatBox = em.find(ChatBox.class, 1L);
            User user = em.find(User.class, 1L);
            Chat chat = Chat.builder()
                    .chatBox(chatBox)
                    .chatContent("chatContent")
                    .chatFilepath("chatFilePath")
                    .chatQuestion("chatQuestion")
                    .user(user)
                    .build();
            em.persist(chat);
        }

        private void InitBlock() {
            Member member = em.find(Member.class, 1L);
            Team team = em.find(Team.class,1L);
            Block build = Block.builder()
                    .member(member)
                    .team(team)
                    .build();
            em.persist(build);
        }


        private void InitAdmin() {
            Admin admin1 = Admin.builder()
                    .adminId("adminID")
                    .adminPassword("1234")
                    .build();
            em.persist(admin1);
        }


        public void InitUser(){
            Team team = em.find(Team.class,1L);
            User user = User.builder()
                    .userPassword("1234")
                    .userId("guswhd903@naver.com")
                    .team(team)
                    .userRole("test")
                    .userNumber("010-1234-5678")
                    .userName("guswhd")
                    .userEmail("guswhd903@naver.com")
                    .build();
            em.persist(user);
        }


    }
}
