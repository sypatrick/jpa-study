package jpabasic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * 생성
 *  - EntityManager.persist() : 영속성 컨텍스트에 저장
 *  - 트랜잭션 커밋되는 시점에 create
 *
 * 조회
 *  - 1차캐시 (@id, Entity, snapshot)
 *  - id : 기본키
 *  - snapshot : 영속성 컨텍스트에 최초로 1차 캐시에 저장된 순간을 저장
 *
 * 수정
 *  - 엔티티 수정시 .set() 만 해줘도 update 쿼리가 날아감. (JPA의 변경감지, Dirty Checking)
 *  - 수정 순서
 *      1. flush()
 *      2. 엔티티와 스냅샷 비교
 *      3. Update sql 생성
 *      4. flush
 *      5. commit
 *  - 트랜잭션 커밋되는 시점에 update
 *  - flush 발생하면
 *   1. 변경 감지(dirty check)
 *   2. 수정된 엔티티를 쓰기지연 sql저장소에 등록
 *   3. 쓰기 지연 sql저장소의 쿼리를 데이터베이스에 동기화 (트랜잭션 commit하면 db에 반영)
 *
 *  삭제
 *  em.remove()
 */
public class JpaMain {
    public static void main(String[] arg){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("patrick");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /**
         *  1. 테이블은 외래 키로 조인을 사용해서 연관된 테이블을 찾는다.
         *  2. 객체는 참조를 사용해서 연관된 객체를 찾는다.
         *
         *  밑의 방식은 객체를 테이블에 맞춰서 구현한 것.
         *  연관 관계가 없기 때문에 계속 db에서 데이터를 가져와야 함.
         *  즉, 객체를 테이블에 맞추어 데이터 중심으로 모델링 하면, 협력관계를 만들 수 없다.
         */
        try{
            Team team = new Team();
            team.setName("A");
            em.persist(team);

            Member member = new Member();
            member.setUserName("memberA");
            member.setTeam(team); // 객체지향 모델링
            em.persist(member);
            Member findMember = em.find(Member.class, member.getId());
            /**
             * 양방향 연관관계
             * mappedBy 가 중요.
             * 객체와 테이블이 관계를 맺는 차이
             * 1. 객체는 단방향 연관관계가 2개 (참조가 Member, Team에 각각 있음)
             * 2. 테이블은 양방향 연관관계 1개 (FK 하나로 Member, Team 연관관계가 끝)
             *
             * 연관관계의 주인은 mappedBy 사용할 수 없고 등록이나 수정 할 수 없다. 조회만 가능.
             * FK가 있는 곳을 주인으로 설정하는 것이 좋다.
             * 만약 Team을 주인으로 정해서 수정을 하게 되면 Member 테이블로 쿼리가 날라간다.
             * 복잡해지면 정말 헷갈리게 된다.
             *
             * 결국은 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정해야함.
             * flush(), clear()하지 않은 상태라면 List에 add하지 않는다면 1차캐시에 값이 없음. JPA가 읽어들일 수 없다.
             *
             * 단방향 매핑만으로도 이미 매핑관계를 완료.
             * 단방향 매핑을 잘 하고 양방향은 필요할 때 추가해도 된다.(테이블에 영향을 주지 않기 때문)
             * 양방향 매핑은 반대 방향으로 조회 기능이 추가된 것 뿐.
             */
            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println("m.getUserName() = " + m.getUserName());
            }

            tx.commit();

        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
