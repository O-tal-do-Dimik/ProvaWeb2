package com.example.webprova2.Service;

import com.example.webprova2.Model.Pet;
import com.example.webprova2.Repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PetService {

    PetRepository repository;

    @Autowired
    public void setRepository(PetRepository repository) {
        this.repository = repository;
    }

    public List<Pet> findAll(){
        return repository.findAll();
    }

    public void save(Pet a){
        repository.save(a);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public List<Pet> listAll(){
        return repository.findAllByDeletedIsNull();
    }
    public Pet findById(Long id){
        return repository.getById(id);
    }

    public void deletedtrue(Long id){
        repository.deleteById(id);
    }

}
