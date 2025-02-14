package weolbu.assignment.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByName(MemberName name);

    boolean existsByEmail(MemberEmail email);

    boolean existsByPhoneNumber(MemberPhoneNumber phoneNumber);
}
