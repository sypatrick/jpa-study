package jpabasic;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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
    @Column(name = "team_id")
    private Long teamId;

}
