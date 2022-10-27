package fsoft.spring.sfgpetclinic.controllers;

import fsoft.spring.sfgpetclinic.model.Owner;
import fsoft.spring.sfgpetclinic.model.Pet;
import fsoft.spring.sfgpetclinic.model.PetType;
import fsoft.spring.sfgpetclinic.services.OwnerService;
import fsoft.spring.sfgpetclinic.services.PetService;
import fsoft.spring.sfgpetclinic.services.PetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController controller;

    Set<Owner> owners;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        owners = new HashSet<>();
        owners.add(Owner.builder().id(1l).build());
        owners.add(Owner.builder().id(2l).build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void findOwners() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"))
                .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }

    @Test
    void processFindFormReturnMany() throws Exception {
        when(ownerService.findAllByLastNameLike(anyString()))
                .thenReturn(Arrays.asList(Owner.builder().id(1l).build(),
                        Owner.builder().id(2l).build()));

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attribute("selections", hasSize(2)));
    }

    @Test
    void processFindFormReturnOne() throws Exception {
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(Arrays.asList(Owner.builder().id(1l).build()));

        mockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
    }

    @Test
    void processFindFormEmptyReturnMany() throws Exception {
        when(ownerService.findAllByLastNameLike(anyString()))
                .thenReturn(Arrays.asList(Owner.builder().id(1l).build(),
                        Owner.builder().id(2l).build()));

        mockMvc.perform(get("/owners")
                        .param("lastName",""))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attribute("selections", hasSize(2)));;
    }

    @Test
    void displayOwner() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(Owner.builder().id(1l).build());

        mockMvc.perform(get("/owners/123"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attribute("owner", hasProperty("id", is(1l))));
    }


    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }

    @Test
    void processCreationForm() throws Exception {
        when(ownerService.save(ArgumentMatchers.any())).thenReturn(Owner.builder().id(1l).build());

        mockMvc.perform(post("/owners/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService).save(ArgumentMatchers.any());
    }

//    @Test
//    void initUpdateOwnerForm() throws Exception {
//        when(ownerService.findById(anyLong())).thenReturn(Owner.builder().id(1l).build());
//
//        mockMvc.perform(get("/owners/1/edit"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
//                .andExpect(model().attributeExists("owner"));
//
//        verifyNoInteractions(ownerService);
//    }

    @Test
    void processUpdateOwnerForm() throws Exception {
        when(ownerService.save(ArgumentMatchers.any())).thenReturn(Owner.builder().id(1l).build());

        mockMvc.perform(post("/owners/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService).save(ArgumentMatchers.any());
    }

    @ExtendWith(MockitoExtension.class)
    static
    class PetControllerTest {

        @Mock
        PetService petService;

        @Mock
        OwnerService ownerService;

        @Mock
        PetTypeService petTypeService;

        @InjectMocks
        PetController petController;

        MockMvc mockMvc;

        Owner owner;
        Set<PetType> petTypes;

        @BeforeEach
        void setUp() {
            owner = Owner.builder().id(1l).build();

            petTypes = new HashSet<>();
            petTypes.add(PetType.builder().id(1L).name("Dog").build());
            petTypes.add(PetType.builder().id(2L).name("Cat").build());

            mockMvc = MockMvcBuilders
                    .standaloneSetup(petController)
                    .build();
        }

        @Test
        void initCreationForm() throws Exception {
            when(ownerService.findById(anyLong())).thenReturn(owner);
            when(petTypeService.findAll()).thenReturn(petTypes);

            mockMvc.perform(get("/owners/1/pets/new"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("owner"))
                    .andExpect(model().attributeExists("pet"))
                    .andExpect(view().name("pets/createOrUpdatePetForm"));
        }

        @Test
        void processCreationForm() throws Exception {
            when(ownerService.findById(anyLong())).thenReturn(owner);
            when(petTypeService.findAll()).thenReturn(petTypes);

            mockMvc.perform(post("/owners/1/pets/new"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/owners/1"));

            verify(petService).save(any());
        }

        @Test
        void initUpdateForm() throws Exception {
            when(ownerService.findById(anyLong())).thenReturn(owner);
            when(petTypeService.findAll()).thenReturn(petTypes);
            when(petService.findById(anyLong())).thenReturn(Pet.builder().id(2L).build());

            mockMvc.perform(get("/owners/1/pets/2/edit"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("owner"))
                    .andExpect(model().attributeExists("pet"))
                    .andExpect(view().name("pets/createOrUpdatePetForm"));
        }

        @Test
        void processUpdateForm() throws Exception {
            when(ownerService.findById(anyLong())).thenReturn(owner);
            when(petTypeService.findAll()).thenReturn(petTypes);

            mockMvc.perform(post("/owners/1/pets/2/edit"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/owners/1"));

            verify(petService).save(any());
        }

        @Test
        void populatePetTypes() {
            //todo impl
        }

        @Test
        void findOwner() {
            //todo impl
        }

        @Test
        void initOwnerBinder() {
            //todo impl
        }
    }
}