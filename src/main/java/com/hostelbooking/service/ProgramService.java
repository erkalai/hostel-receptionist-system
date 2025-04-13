package com.hostelbooking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostelbooking.entity.Program;
import com.hostelbooking.repository.ProgramRepository;

@Service
public class ProgramService {

    @Autowired
    private ProgramRepository programRepository;

    public List<Program> findAllPrograms() {
        return programRepository.findAll();
    }

    public Program findProgramById(Long id) {
        return programRepository.findById(id).orElseThrow(() -> new RuntimeException("Program not found"));
    }

    public void saveProgram(Program program) {
        programRepository.save(program);
    }

    public void deleteProgram(Long id) {
        programRepository.deleteById(id);
    }
}
