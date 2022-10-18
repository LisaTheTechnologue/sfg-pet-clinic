package fsoft.spring.services.map;

import fsoft.spring.model.Vet;
import fsoft.spring.services.CrudService;
import fsoft.spring.services.VetService;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class VetServiceMap extends AbstractMapService<Vet, Long> implements VetService {

    @Override
    public Set<Vet> findAll() {
        return this.findAll();
    }

    @Override
    public void deleteById(Long id) {
        this.deleteById(id);
    }

    @Override
    public void delete(Vet object) {
        this.delete(object);
    }

    @Override
    public Vet save(Vet object) {
        return this.save(object);
    }

    @Override
    public Vet findById(Long id) {
        return this.findById(id);
    }
}
