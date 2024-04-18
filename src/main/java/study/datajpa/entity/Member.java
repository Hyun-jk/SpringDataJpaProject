package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@ToString(of = {"id" , "username", "age"})
@NamedQuery( //JPA NamedQuery의 장점은 빌드시 오류를 잡기때문에 미리 오류를 확인할 수 있다.
        name="Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
//jpa에서 제공하는 fetch join
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long Id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="team_id")
    private Team team;

    //jpa는 기본적으로 default 생성자, 즉 파라미터 없는 생성자가 필요하다 -> NoArgsConstructor annotation으로 변경
//    protected  Member() {
//    }
    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }


}
