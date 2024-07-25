package pe.com.CitasMedicas.model;

import lombok.Data;
import javax.validation.constraints.NotNull;
import jakarta.persistence.*;


@Entity
@Data
@Table(name = "estadoCita")
public class EstadoCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idestadoCita;

    @NotNull
    private String estado;
}
