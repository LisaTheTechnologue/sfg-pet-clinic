package fsoft.spring.sfgpetclinic.services;

import fsoft.spring.sfgpetclinic.model.Owner;

public interface OwnerService extends CrudService<Owner, Long> {
    Owner findByLastName(String lastName);
}
