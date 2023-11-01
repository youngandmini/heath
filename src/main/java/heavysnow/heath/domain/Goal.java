package heavysnow.heath.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "goal")
public class Goal {

    @Id
    @GeneratedValue
    @Column(name = "goal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    private boolean isAchieved;

}
