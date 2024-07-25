package pe.com.CitasMedicas.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;


import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;

@Data
@Entity
@EqualsAndHashCode(of = "idConsultorio")
@Table(name = "especialidad")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsultorio;

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(name = "foto")
    private String foto;

    @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Medicos> medicos= new HashSet<>();;


}
