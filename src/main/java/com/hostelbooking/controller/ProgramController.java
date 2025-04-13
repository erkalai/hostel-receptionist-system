package com.hostelbooking.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hostelbooking.entity.Program;
import com.hostelbooking.entity.User;
import com.hostelbooking.repository.ProgramRepository;
import com.hostelbooking.service.ProgramService;
import com.hostelbooking.service.UserService;

@Controller
@RequestMapping("/programs")
public class ProgramController {

    @Autowired
    private ProgramService programService;

    @Autowired
    private ProgramRepository programRepository;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String showPrograms(Model model, Principal principal) {
    	 List<Program> programs = programRepository.findAllOrderedByStarred();
    		User user = userService.findUserByEmail(principal.getName());

			model.addAttribute("userName", user);
    	 
    	    model.addAttribute("programs", programs); // Adding programs list to the model
    	    model.addAttribute("program", new Program()); // For creating a new Program
    	    return "common/program";
    }

    // Handle create/update request
    @PostMapping("/create")
    public String saveProgram(@ModelAttribute Program program) {
        programService.saveProgram(program);
        return "redirect:/programs";
    }

    // Display the form for editing a program
    @GetMapping("/edit/{id}")
    public String editProgram(@PathVariable Long id, Model model, Principal principal) {
        Program program = programService.findProgramById(id);
        model.addAttribute("program", program);
        model.addAttribute("programs", programService.findAllPrograms());
        
        User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);
        
        return "common/program";
    }
    
    
    @PostMapping({"/edit/{id}"})
    public String saveProgram(@ModelAttribute Program program, @PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            // Update an existing program
            program.setId(id);
        }
        programService.saveProgram(program);
        model.addAttribute("successMessage", "Program saved successfully!");
        return "redirect:/programs";
    }


    // Handle delete request
    @GetMapping("/delete/{id}")
    public String deleteProgram(@PathVariable Long id) {
        programService.deleteProgram(id);
        return "redirect:/programs"; // Redirect to the program list after deletion
    }
}
