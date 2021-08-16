package tads.eaj.ufrn.roupaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tads.eaj.ufrn.roupaapp.model.Roupa;
import tads.eaj.ufrn.roupaapp.repository.RoupaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoupaService {

    RoupaRepository repository;

    @Autowired
    public void setRepository(RoupaRepository repository) {
        this.repository = repository;
    }

    public List<Roupa> findAll(){
        return  repository.findAllByExcluidoIsNull();
    }


    public void save(Roupa r){
        repository.save(r);
    }

    public Roupa findById(Long id){
        return repository.getById(id);
    }
}
