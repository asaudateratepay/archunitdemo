package com.ratepay.archunitdemo.persistence;

import com.ratepay.archunitdemo.persistence.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<PersonEntity, UUID> {

}
