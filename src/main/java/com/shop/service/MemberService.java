package com.shop.service;

import com.shop.domain.member.entity.Member;
import com.shop.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// If an error occurs in the service layer responsible for the business logic,
// the @Transactional annotation can be used to roll back the changed data to before the logic was performed.
@Slf4j
@Transactional
@RequiredArgsConstructor // Create Constructor and DI -> @NonNull or private final variable
@Service // Service Layer Annotation
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 사용자 회원 가입
     * @return 회원 가입 완료 후 해당 사용자의 정보 반환
     */
    public Member join(Member Member) { // FIXME: Member Entity -> MemberFormRequestDto로 변환 필요
        validateDuplicateMember(Member);
        return memberRepository.save(Member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}
