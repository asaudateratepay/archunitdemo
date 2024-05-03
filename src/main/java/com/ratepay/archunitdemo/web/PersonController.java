package com.ratepay.archunitdemo.web;

import com.ratepay.archunitdemo.dto.PersonDto;
import com.ratepay.archunitdemo.persistence.PersonRepository;
import com.ratepay.archunitdemo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/people")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;






    @GetMapping
    public List<PersonDto> listPeople() {
        return personService.listPeople();
    }

}
