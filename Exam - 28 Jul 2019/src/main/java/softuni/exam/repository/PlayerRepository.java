package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Player;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    Set<Player> findByTeamNameOrderById(String teamName);

    Set<Player> findBySalaryIsAfterOrderBySalaryDesc(BigDecimal bigDecimal);

}
