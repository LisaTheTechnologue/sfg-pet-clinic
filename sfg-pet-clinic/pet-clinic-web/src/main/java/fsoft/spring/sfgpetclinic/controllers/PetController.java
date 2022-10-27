package fsoft.spring.sfgpetclinic.controllers;

import fsoft.spring.sfgpetclinic.model.Owner;
import fsoft.spring.sfgpetclinic.model.Pet;
import fsoft.spring.sfgpetclinic.model.PetType;
import fsoft.spring.sfgpetclinic.services.OwnerService;
import fsoft.spring.sfgpetclinic.services.PetService;
import fsoft.spring.sfgpetclinic.services.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Created by jt on 9/22/18.
 */
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

    private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

    private final PetService petService;
    private final OwnerService ownerService;
    private final PetTypeService petTypeService;
    @Autowired
    public PetController(PetService petService, OwnerService ownerService, PetTypeService petTypeService) {
        this.petService = petService;
        this.ownerService = ownerService;
        this.petTypeService = petTypeService;
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/pets/new")
    public String initCreationForm(@PathVariable("ownerId") Long ownerId, Owner owner, Model model) {
        Pet pet = new Pet();

        pet.setOwner(owner);
        owner.getPets().add(pet);
        model.addAttribute("pet", pet);
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/pets/new")
    public String processCreationForm(@ModelAttribute("owner") Owner owner,
                                      @Valid @ModelAttribute("pet") Pet pet,
                                      @PathVariable String ownerId,
                                      BindingResult result, ModelMap model) {
        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null){
            result.rejectValue("name", "duplicate", "already exists");
        }

        if (result.hasErrors()) {
            model.put("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            pet.setOwner(owner);
            petService.save(pet);
            owner.getPets().add(pet);
            return "redirect:/owners/" + owner.getId();
        }
    }

    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(@PathVariable("ownerId") Long ownerId,
                                 @PathVariable("petId") Long petId, Model model) {
        model.addAttribute("pet", petService.findById(petId));
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/pets/{petId}/edit")
    public String processUpdateForm(@ModelAttribute("owner") Owner owner,
                                    @Valid @ModelAttribute("pet") Pet pet,
                                    @PathVariable String petId,
                                    BindingResult result, Model model, @PathVariable String ownerId) {
        if (result.hasErrors()) {
            pet.setOwner(owner);
            model.addAttribute("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            pet.setOwner(owner);
            petService.save(pet);
            owner.getPets().add(pet);
            return "redirect:/owners/" + owner.getId();
        }
    }

}
