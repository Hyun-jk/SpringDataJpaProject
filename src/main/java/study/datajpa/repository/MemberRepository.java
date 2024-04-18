package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findByUsername(@Param("username") String username);

    //Spring DataJpa namedQuery 역시 빌드할 때 오류를 다 잡아준다. 권장!!!
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //DTO 조회
    @Query("select new study.datajpa.dto.MemberDto(m.Id,m.username,t.name) from Member m join m.team t")
    List<MemberDto> findmemberDto();

    //Collections으로 조회
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); //컬렉션

    Member findMemberByUsername(String username); //단건

    Optional<Member> findOptioanlByUsername(String username); //단건 Optional

    //join이 복잡해지면 그대로 사용하기 성능이 나오지않는다. 그래서 countQuery따로 설정해서 사용
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);



    //update를 할 때
    @Modifying(clearAutomatically = true) //벌크성 수정 쿼리시 영속성 컨테스트를 클리어하고 다시 다른 쿼리를 진행해야한다.
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
        //jpql 사용안하고 Spring dataJPA에서 fetch Join 하는 방법
    List<Member> findAll();


    //query 짜고 fetch join 추가하는 방법
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //이름으로 query 작성 후 fetch join 추가하는 방법
    //@EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    //jpa에서 제공하는 fetchjoin named entity graph 실행
    List<Member> findEntityGraphByUsername(@Param("username") String username);


    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
