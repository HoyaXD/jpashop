package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> memberV1(){
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(/*collect.size(),*/ collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        //private int count;
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    //회원 api
    @PostMapping("api/v1/members")
    public CreateMemberResposne saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        // api 요청에 따라 dto 해야됨
        //API를 할 때는 외부에 노출이 하면 안된다.
        //api스펙이 바꿔버린 경우다
        return new CreateMemberResposne(id);
    }

    @PostMapping("api/v2/members")
    public CreateMemberResposne saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);

        return new CreateMemberResposne(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberReponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        //수정일때는 가급적이면 변경감지를 사용해야된다.
        memberService.update(id, request.getName());
        Member findeMember = memberService.findOne(id);

        return new UpdateMemberReponse(findeMember.getId(), findeMember.getName());
    }

    @Data
    static class UpdateMemberRequest {

        private String name;

    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberReponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        //dto를 보면 딱 이름만 받고 싶구나
        @NotEmpty
        private String name;
        //member에다가 넣으면 값이 바뀌기에 api스펙에 맞춰사 dto를 만들면 유지보수에 좋다
        //api는 요청이 들어오건 나가건 전부다 절대 Entity를 사용하지 않고 dto를 생성해서 받는다.
    }

    @Data
    static class CreateMemberResposne {

        private Long id;

        public CreateMemberResposne(Long id) {
            this.id = id;
        }
    }
}
