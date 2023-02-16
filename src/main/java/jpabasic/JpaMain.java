package jpabasic;

import jpabasic.inheritanceMapping.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
 *
 *  직접 쿼리를 보고 싶다면 flush로 싱크를 맞춰주고 (db에 쿼리날림)
 *  1차 캐시 지워주고 밑에 다시 실행. (쿼리를 안보여주는 이유는 영속성 컨텍스트에서 나왔던 1차캐시에서 가져오기 때문)
 *  em.flush();
 *  em.clear();
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
            Movie movie = new Movie();
            movie.setDirector("a");
            movie.setActor("b");
            movie.setName("반지의 제왕");
            movie.setPrice(40000);

            em.persist(movie);

            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println("movie = " + findMovie);

            tx.commit();

        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
