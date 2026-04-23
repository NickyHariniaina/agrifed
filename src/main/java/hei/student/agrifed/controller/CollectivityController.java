package hei.student.agrifed.controller;

import java.util.List;

import hei.student.agrifed.entity.dto.AssignIdentityDto;
import hei.student.agrifed.entity.dto.CreateCollectivityDto;
import hei.student.agrifed.entity.dto.CreateMembershipFeeDto;
import hei.student.agrifed.exception.BadRequestException;
import hei.student.agrifed.exception.ConflictException;
import hei.student.agrifed.exception.NotFoundException;
import hei.student.agrifed.service.CollectivityService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collectivities")
@AllArgsConstructor
public class CollectivityController {

    private CollectivityService collectivityService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCollectivityById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(collectivityService.findCollectivityById(id));
        } catch (NotFoundException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createCollectivities(
            @RequestBody List<CreateCollectivityDto> body) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(collectivityService.createCollectivities(body));
        } catch (NotFoundException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        } catch (BadRequestException error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
        }
    }

    @PutMapping("/{id}/informations")
    public ResponseEntity<?> assignIdentity(
            @PathVariable Integer id,
            @RequestBody(required = false) AssignIdentityDto body) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(collectivityService.assignIdentity(id, body));
        } catch (BadRequestException error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
        } catch (NotFoundException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        } catch (ConflictException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error.getMessage());
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
        }
    }

    @GetMapping("/{id}/membershipFees")
    public ResponseEntity<?> findMembershipFeesById(
            @PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(collectivityService.findMembershipFeesById(id));
        } catch (NotFoundException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
        }
    }

    @PostMapping("/{id}/membershipFees")
    public ResponseEntity<?> createMembershipFees(
            @PathVariable Integer id,
            @RequestBody List<CreateMembershipFeeDto> createMembershipFeeDtos) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(collectivityService.saveMembershipFees(id, createMembershipFeeDtos));
        } catch (BadRequestException error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
        } catch (NotFoundException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> findTransactions(
            @PathVariable Integer id,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        try {
            return ResponseEntity.ok(collectivityService.findTransactions(id, from, to));
        } catch (BadRequestException error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
        } catch (NotFoundException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
        }
    }

    @GetMapping("/{id}/financialAccounts")
    public ResponseEntity<?> findFinancialAccounts(
            @PathVariable Integer id,
            @RequestParam(required = false) String at) {
        try {
            return ResponseEntity.ok(collectivityService.findFinancialAccounts(id, at));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
