package heavysnow.heath.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
//@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "goal")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @Column(columnDefinition = "tinyint")
    private boolean isAchieved;

    @Builder    // 빌더 패턴으로 객체 생성
    public Goal(Member member, String content, boolean isAchieved) {
        this.member = member;
        this.content = content;
        this.isAchieved = isAchieved;
    }

    // 엔티티 내부 update 메서드
    public void update(boolean isAchieved){
        this.isAchieved = isAchieved;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getContent() {
        return content;
    }

    public boolean getIsAchieved() {
        return isAchieved;
    }
}
