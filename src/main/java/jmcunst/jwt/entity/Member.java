package jmcunst.jwt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String uid;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;
    private String email;
    private String profileUrl;
    private String department;
    private String contact;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


    @Builder
    public Member(String uid, String password, String name, String email, String profileUrl, String department, String contact, Role role){
        this.uid = uid;
        this.password = password;
        this.name = name;
        this.email = email;
        this.profileUrl = profileUrl;
        this.department = department;
        this.contact = contact;
        this.role = role;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public Member updateProfile(String name, String profileUrl){
        this.name = name;
        this.profileUrl = profileUrl;
        return this;
    }
}
