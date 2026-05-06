package hei.student.agrifed.service;

import hei.student.agrifed.entity.Collectivity;
import hei.student.agrifed.entity.CollectivityStatistic;
import hei.student.agrifed.entity.dto.AssignIdentityDto;
import hei.student.agrifed.entity.dto.CollectivityOverallStatisticDTO;
import hei.student.agrifed.exception.BadRequestException;
import hei.student.agrifed.exception.NotFoundException;
import hei.student.agrifed.repository.CollectivityRepository;
import hei.student.agrifed.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final CollectivityRepository collectivityRepository;

    public StatisticsService(StatisticsRepository statisticsRepository, CollectivityRepository collectivityRepository) {
        this.statisticsRepository = statisticsRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<CollectivityOverallStatisticDTO> getCollectivityOverallStatistics(LocalDate from, LocalDate to) {

        if (from == null || to == null) {
            throw new BadRequestException("Both 'from' and 'to' dates are required");
        }

        if (from.isAfter(to)) {
            throw new BadRequestException("'from' date cannot be after 'to' date");
        }

        List<CollectivityStatistic> stats = statisticsRepository.getStatistics(from, to);

        if (stats == null || stats.isEmpty()) {
            throw new NotFoundException("No statistics found for the given period");
        }

        List<Collectivity> collectivities = collectivityRepository.findAll();

        if (collectivities == null || collectivities.isEmpty()) {
            throw new NotFoundException("No collectivities found");
        }

        Map<String, Collectivity> collectivityMap = collectivities.stream()
                .collect(Collectors.toMap(Collectivity::getId, c -> c));

        List<CollectivityOverallStatisticDTO> result = new ArrayList<>();

        for (CollectivityStatistic stat : stats) {

            Collectivity collectivity = collectivityMap.get(stat.getId());

            if (collectivity == null) {
                continue;
            }

            AssignIdentityDto identity = new AssignIdentityDto(
                    collectivity.getNumber(),
                    collectivity.getName()
            );

            CollectivityOverallStatisticDTO dto = new CollectivityOverallStatisticDTO();
            dto.setCollectivityInformation(identity);
            dto.setNewMembersNumber(stat.getNewMembersNumber());
            dto.setOverallMemberCurrentDuePercentage(stat.getOverallMemberCurrentDuePercentage());

            result.add(dto);
        }

        if (result.isEmpty()) {
            throw new NotFoundException("No matching data found between statistics and collectivities");
        }

        return result;
    }
}
