package pe.com.CitasMedicas.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CorreoRequest {

    private String destinatario;
    private String asunto;
    private String mensaje;
}
