package tads.eaj.ufrn.roupaapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tads.eaj.ufrn.roupaapp.model.Roupa;

import java.util.List;

public interface RoupaRepository extends JpaRepository<Roupa, Long> {
    public List<Roupa> findAllByExcluidoIsNull();
}
