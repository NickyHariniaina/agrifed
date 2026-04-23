package hei.student.agrifed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import hei.student.agrifed.entity.Member;
import hei.student.agrifed.entity.dto.CreateMemberDto;
import hei.student.agrifed.exception.BadRequestException;
import hei.student.agrifed.exception.NotFoundException;
import hei.student.agrifed.service.MemberService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody List<CreateMemberDto> createMemberDtos) {
        try {
            List<Member> member = memberService.save(createMemberDtos);
            return ResponseEntity.status(HttpStatus.CREATED).body(member);
        } catch (BadRequestException error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
        } catch (NotFoundException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
        }
    }

}
