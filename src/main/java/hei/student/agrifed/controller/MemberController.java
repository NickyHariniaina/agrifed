package hei.student.agrifed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import hei.student.agrifed.entity.Member;
import hei.student.agrifed.entity.dto.CreateMemberDto;
import hei.student.agrifed.exception.BadRequestException;
import hei.student.agrifed.exception.NotFoundException;
import hei.student.agrifed.service.MemberService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody CreateMemberDto createMemberDto) {
        try {
            Member member = memberService.save(createMemberDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(member);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
