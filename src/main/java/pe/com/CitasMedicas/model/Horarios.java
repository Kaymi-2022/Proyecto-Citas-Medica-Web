package pe.com.CitasMedicas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "horarios")
public class Horarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El día no puede ser nulo")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dia;

    @NotNull(message = "La hora no puede ser nula")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "idestadoCita")
    @NotNull(message = "El estado de la cita no puede ser nulo")
    private EstadoCita idestadoCita;

    @ManyToOne
    @JoinColumn(name = "medico_id",nullable = false)
    @NotNull(message = "El medico no puede ser nulo")
    private Medicos medicos;

}
