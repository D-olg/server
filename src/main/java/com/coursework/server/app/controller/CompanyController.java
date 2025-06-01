package com.coursework.server.app.controller;

import com.coursework.server.app.model.Company;
import com.coursework.server.app.model.User;
import com.coursework.server.app.service.CompanyService;
import com.coursework.server.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @GetMapping("/current")
    public ResponseEntity<Company> getCompanyForCurrentUser(Principal principal) {
        User user = userService.findByUsername(principal.getName());

        Company company = companyService.getCompanyByUserId(user.getId());
        return company != null ? ResponseEntity.ok(company) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateCompany(@PathVariable int id, @RequestBody Company updatedCompany, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        boolean success = companyService.updateUserCompany(updatedCompany);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
