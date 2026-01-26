package com.daniel.catalog.services;

import com.daniel.catalog.dto.RoleDTO;
import com.daniel.catalog.dto.UserDTO;
import com.daniel.catalog.dto.UserInsertDTO;
import com.daniel.catalog.entities.Role;
import com.daniel.catalog.entities.User;
import com.daniel.catalog.repositories.RoleRepository;
import com.daniel.catalog.repositories.UserRepository;
import com.daniel.catalog.services.Exceptions.DatabaseException;
import com.daniel.catalog.services.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository UserService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {

        Page<User> list = UserService.findAll(pageable);

        return list.map(UserDTO::new);

    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {

        Optional<User> obj = UserService.findById(id);

        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return new UserDTO(entity);

    }

    @Transactional
    public UserDTO insert(UserInsertDTO userInsertDTO) {

        User user = new User();

        copyDtoToEntity(userInsertDTO, user);

        user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));

        user = UserService.save(user);

        return new UserDTO(user);

    }

    @Transactional
    public UserDTO update(Long id, UserDTO UserDTO) {

        User entity = UserService.getReferenceById(id);

        copyDtoToEntity(UserDTO, entity);

        UserService.save(entity);

        return new UserDTO(entity);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        if (!UserService.existsById(id)) {
            throw new ResourceNotFoundException("Resource Not Found!");
        }
        try {
            UserService.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity Violation!");
        }

    }

    private void copyDtoToEntity(UserDTO UserDTO, User User) {

        User.setFirstName(UserDTO.getFirstName());
        User.setLastName(UserDTO.getLastName());
        User.setEmail(UserDTO.getEmail());

        User.getRoles().clear();

        for(RoleDTO roleDTO : UserDTO.getRoles()){
            Role entity = roleRepository.getReferenceById(roleDTO.getId());

            User.getRoles().add(entity);

        }

    }


}
