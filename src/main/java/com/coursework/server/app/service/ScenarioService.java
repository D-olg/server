package com.coursework.server.app.service;
import com.coursework.server.app.model.Scenario;
import com.coursework.server.app.repository.ScenarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;

    public ScenarioService(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @Transactional
    public Scenario save(Scenario scenario) {
        return scenarioRepository.save(scenario);
    }

    public Scenario findById(Integer id) {
        return scenarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Scenario not found"));
    }

    // Other methods as needed
}
