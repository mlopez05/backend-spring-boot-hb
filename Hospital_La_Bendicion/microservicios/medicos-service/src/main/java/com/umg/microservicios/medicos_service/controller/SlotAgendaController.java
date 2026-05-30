package com.umg.microservicios.medicos_service.controller;

import com.umg.microservicios.medicos_service.model.SlotAgenda;
import com.umg.microservicios.medicos_service.service.SlotAgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
 
@RestController
@RequiredArgsConstructor
@RequestMapping("api/medico")
public class SlotAgendaController {

    private final SlotAgendaService slotService;
 
    @GetMapping("/{id}/disponibilidad")
    public ResponseEntity<?> obtenerDisponibilidad(
            @PathVariable Integer id,
            @RequestParam(name = "fecha_inicio", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio) {
 
        List<SlotAgenda> slots = slotService.obtenerDisponibilidad(id, fechaInicio);
 
        List<Map<String, Object>> slotsDTO = slots.stream().map(s -> Map.<String, Object>of(
                "slot_id",    s.getId(),
                "fecha",      s.getFecha().toString(),
                "hora_inicio", s.getHoraInicio().toString(),
                "hora_fin",   s.getHoraFin().toString()
        )).toList();
 
        return ResponseEntity.ok(Map.of(
                "medico_id",        id,
                "slots_disponibles", slotsDTO
        ));
    }
 
    @PostMapping("/slots/{slotId}/reservar")
    public ResponseEntity<?> reservarSlot(@PathVariable Integer slotId) {
        SlotAgenda slot = slotService.reservarSlot(slotId);
        return ResponseEntity.ok(Map.of(
                "slot_id", slot.getId(),
                "estado",  slot.getEstado()
        ));
    }
 
    @PostMapping("/medicos/slots/{slotId}/liberar")
    public ResponseEntity<?> liberarSlot(@PathVariable Integer slotId) {
        SlotAgenda slot = slotService.liberarSlot(slotId);
        return ResponseEntity.ok(Map.of(
                "slot_id", slot.getId(),
                "estado",  slot.getEstado()
        ));
    }
 
    @PostMapping("/{id}/slots")
    public ResponseEntity<?> crearSlots(
            @PathVariable Integer id,
            @RequestBody List<SlotAgenda> slots) {
 
        List<SlotAgenda> creados = slotService.crearSlots(id, slots);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message",       "Slots creados exitosamente",
                "total_creados", creados.size()
        ));
    }    
}
