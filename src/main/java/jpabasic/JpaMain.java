package jpabasic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] arg){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("patrick");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
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
        try{
            Member member = new Member();
            member.setUserName("B");

            System.out.println("=========");
            em.persist(member);
            System.out.println("=========");
            System.out.println("member_Id() = " + member.getId());

            tx.commit();

        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
