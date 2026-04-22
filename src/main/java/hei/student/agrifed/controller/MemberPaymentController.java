package hei.student.agrifed.controller;

import java.util.List;

import hei.student.agrifed.entity.dto.CreateMemberPaymentDto;
import hei.student.agrifed.exception.BadRequestException;
import hei.student.agrifed.exception.NotFoundException;
import hei.student.agrifed.service.MemberPaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberPaymentController {
    private final MemberPaymentService memberPaymentService;

    public MemberPaymentController(MemberPaymentService memberPaymentService) {
        this.memberPaymentService = memberPaymentService;
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<?> createPayments(
            @PathVariable String id,
            @RequestBody(required = false) List<CreateMemberPaymentDto> body) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(memberPaymentService.createPayments(id, body));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
