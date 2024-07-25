package pe.com.CitasMedicas.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Data
@EqualsAndHashCode(of = "idMedico")
@Table(name = "medicos")
public class Medicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedico;

    @Column(name = "nombre")
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(name = "foto_medico")
    private String foto;

    @ManyToOne
    @JoinColumn(name = "especialida_id")
    @NotNull(message = "El consultorio no puede ser nulo")
    private Especialidad especialidad;

}
