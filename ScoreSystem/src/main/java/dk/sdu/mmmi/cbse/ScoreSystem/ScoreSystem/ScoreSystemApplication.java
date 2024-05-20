package dk.sdu.mmmi.cbse.ScoreSystem.ScoreSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ScoreSystemApplication {

	private Long accumulatedScore = 0L;

	public static void main(String[] args) {
		SpringApplication.run(ScoreSystemApplication.class, args);
	}

	@GetMapping("/points/add")
	public Long incrementScore(@RequestParam(value = "value") Long value) {
		accumulatedScore += value;
		return accumulatedScore;
	}

	@GetMapping("/points")
	public Long getCurrentScore() {
		return accumulatedScore;
	}
}