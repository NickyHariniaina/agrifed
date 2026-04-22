package hei.student.agrifed.controller;

import java.util.List;

import hei.student.agrifed.entity.Collectivity;
import hei.student.agrifed.entity.dto.CreateCollectivityDto;
import hei.student.agrifed.service.CollectivityService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collectivities")
@AllArgsConstructor
public class CollectivityController {

    private CollectivityService collectivityService;

    /**
     * POST /collectivities
     *
     * Crée une liste de collectivités selon les conditions d'ouverture (consignes A + spec v0.0.1).
     *
     * @return 201 avec la liste des collectivités créées (membres hydratés)
     * @throws hei.student.agrifed.exception.BadRequestException (400)
     *         — approbation fédérale absente, structure incomplète,
     *           moins de 10 membres, moins de 5 membres seniors.
     * @throws hei.student.agrifed.exception.NotFoundException (404)
     *         — un membre référencé est introuvable en base.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Collectivity> createCollectivities(
            @RequestBody List<CreateCollectivityDto> dtos) {
        return collectivityService.createCollectivities(dtos);
    }
}
