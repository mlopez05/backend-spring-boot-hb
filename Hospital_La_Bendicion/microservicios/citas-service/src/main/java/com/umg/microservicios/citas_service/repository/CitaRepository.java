package com.umg.microservicios.citas_service.repository;
 
import com.umg.microservicios.citas_service.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
 
import java.time.LocalDateTime;
import java.util.List;
 
public interface CitaRepository extends JpaRepository<Cita, Long> {
 
    List<Cita> findByPacienteId(Long pacienteId);
 
    @Query("SELECT c FROM Cita c WHERE c.estado = 'PROGRAMADA' " +
           "AND c.fechaHora >= :desde AND c.fechaHora < :hasta")
    List<Cita> findCitasParaRecordatorio(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);
}