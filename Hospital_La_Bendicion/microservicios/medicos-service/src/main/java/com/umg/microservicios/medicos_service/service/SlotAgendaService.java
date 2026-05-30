package com.umg.microservicios.medicos_service.service;

import com.umg.microservicios.medicos_service.model.Medico;
import com.umg.microservicios.medicos_service.model.SlotAgenda;
import com.umg.microservicios.medicos_service.repository.MedicoRepository;
import com.umg.microservicios.medicos_service.repository.SlotAgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
 
import java.time.LocalDate;
import java.util.List;
 
@Service
@RequiredArgsConstructor
public class SlotAgendaService {

    private final SlotAgendaRepository slotRepository;
    private final MedicoRepository medicoRepository;
 
    public List<SlotAgenda> obtenerDisponibilidad(Integer medicoId, LocalDate desde) {
        medicoRepository.findById(medicoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médico no encontrado con id: " + medicoId));
 
        LocalDate fechaBase = (desde != null) ? desde : LocalDate.now();
 
        return slotRepository
                .findByMedicoIdAndEstadoAndFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(
                        medicoId, "DISPONIBLE", fechaBase);
    }
 
    @Transactional
    public SlotAgenda reservarSlot(Integer slotId) {
        SlotAgenda slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Slot no encontrado con id: " + slotId));
 
        if ("OCUPADO".equals(slot.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El slot ya está ocupado. Seleccione otro horario.");
        }
 
        slot.setEstado("OCUPADO");
        return slotRepository.save(slot);
    }
 
    @Transactional
    public SlotAgenda liberarSlot(Integer slotId) {
        SlotAgenda slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Slot no encontrado con id: " + slotId));
 
        slot.setEstado("DISPONIBLE");
        return slotRepository.save(slot);
    }

    @Transactional
    public List<SlotAgenda> crearSlots(Integer medicoId, List<SlotAgenda> slots) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médico no encontrado con id: " + medicoId));
 
        slots.forEach(s -> {
            s.setMedico(medico);
            s.setEstado("DISPONIBLE");
        });
 
        return slotRepository.saveAll(slots);
    }    
}
