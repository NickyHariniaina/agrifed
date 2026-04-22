package hei.student.agrifed.controller;

import java.util.List;

import hei.student.agrifed.entity.dto.CreateCollectivityDto;
import hei.student.agrifed.service.CollectivityService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @PostMapping
    public ResponseEntity<?> createCollectivities(
            @RequestBody List<CreateCollectivityDto> body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(collectivityService.createCollectivities(body));
    }
}
