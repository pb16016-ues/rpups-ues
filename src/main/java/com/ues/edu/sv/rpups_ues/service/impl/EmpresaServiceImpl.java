package com.ues.edu.sv.rpups_ues.service.impl;

import com.ues.edu.sv.rpups_ues.model.entity.Empresa;
import com.ues.edu.sv.rpups_ues.model.repository.EmpresaRepository;
import com.ues.edu.sv.rpups_ues.service.EmpresaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empresa> findById(Long idEmpresa) {
        return empresaRepository.findById(idEmpresa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findByNombreComercial(String nombreComercial) {
        return empresaRepository.findByNombreComercialContainingIgnoreCase(nombreComercial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findByNombreLegal(String nombreLegal) {
        return empresaRepository.findByNombreLegalContainingIgnoreCase(nombreLegal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findByRubroIdRubro(Long idRubro) {
        return empresaRepository.findByIdRubro(idRubro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> findByUserCreador(Long idUsuario) {
        return empresaRepository.findByIdUserCreador(idUsuario);
    }

    @Override
    @Transactional
    public Empresa save(Empresa empresa) {
        if (empresa != null && (empresa.getEstadoActivo() == null || !empresa.getEstadoActivo())) {
            empresa.setEstadoActivo(true);
        }
        Empresa savedEmpresa = empresaRepository.save(empresa);
        if (savedEmpresa == null) {
            throw new RuntimeException("Error al guardar la empresa");
        }
        return savedEmpresa;
    }

    @Override
    @Transactional
    public void desactiveById(Long idEmpresa) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("No existe la empresa con id: " + idEmpresa));
        empresa.setEstadoActivo(false);
        empresaRepository.save(empresa);
    }
}