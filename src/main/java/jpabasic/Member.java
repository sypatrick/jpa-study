package jpabasic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Entity 가 붙은 클래스는 JPA가 관리 -> JPA를 사용해서 테이블과 매핑할 클래스는 필수
 *  1. 기본 생성자 필수
 *  2. final, enum, interface, inner 클래스 사용 못함.
 *  3. 저장할 필드에 final 사용하면 안됨.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String userName;

//    @Column(name = "team_id")
//    private Long teamId;
    /**
     * 밑의 어노테이션을 붙여주면 연관관계 매핑, 객체지향 모델링이다.
     */
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker locker;
}
