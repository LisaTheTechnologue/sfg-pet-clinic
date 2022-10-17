package fsoft.spring.services;

import fsoft.spring.model.Owner;
import fsoft.spring.model.Pet;

import java.util.Set;

public interface PetService {
    Pet findById(Long id);
    Pet save(Pet owner);
    Set<Pet> findAll();
}
