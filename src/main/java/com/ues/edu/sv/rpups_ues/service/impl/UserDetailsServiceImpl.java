package com.ues.edu.sv.rpups_ues.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ues.edu.sv.rpups_ues.auth.models.AuthUser;
import com.ues.edu.sv.rpups_ues.model.entity.Usuario;
import com.ues.edu.sv.rpups_ues.model.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository userRepository;

    public UserDetailsServiceImpl(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * @Override
     * 
     * @Transactional(readOnly = true)
     * public UserDetails loadUserByUsername(String username) throws
     * UsernameNotFoundException {
     * Usuario user = userRepository.findByUsername(username)
     * .orElseThrow(() -> new UsernameNotFoundException("The username:" + username +
     * " does not exist"));
     * List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
     * authorities.add(new SimpleGrantedAuthority(user.getRol().getCodigo()));
     * if (authorities.isEmpty())
     * throw new UsernameNotFoundException("the user has no assigned roles");
     * 
     * return new AuthUser(user.getIdUsuario(), user.getUsername(),
     * user.getPassword(), true, true, true, true,
     * authorities);
     * }
     */

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        Usuario user = userRepository.findByUsernameOrCorreoInstitucional(input, input)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario: " + input));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRol().getCodigo()));
        if (authorities.isEmpty())
            throw new UsernameNotFoundException("El usuario no tiene roles asignados");

        return new AuthUser(user.getIdUsuario(), user.getUsername(), user.getPassword(), true, true, true, true,
                authorities);
    }
}
