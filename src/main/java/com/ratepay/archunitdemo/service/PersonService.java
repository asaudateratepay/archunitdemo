package com.ratepay.archunitdemo.service;

import com.ratepay.archunitdemo.dto.PersonDto;
import com.ratepay.archunitdemo.persistence.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;



    public List<PersonDto> listPeople() {
        return personRepository.findAll().stream().map(p -> new PersonDto(p.getId(), p.getName())).toList();
    }

}
