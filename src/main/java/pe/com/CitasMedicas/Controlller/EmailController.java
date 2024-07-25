package pe.com.CitasMedicas.Controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.com.CitasMedicas.model.CorreoRequest;
import pe.com.CitasMedicas.servicio.EmailServiceImpl;
import pe.com.CitasMedicas.servicio.HistorialCitasServiceImpl;


@Controller
@RequestMapping
public class EmailController {

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    HistorialCitasServiceImpl historialCitasService;

    CorreoRequest correoRequest=new CorreoRequest();

    @PostMapping("/user/enviarEmail")
    public String enviarCorreo(CorreoRequest correoRequest,RedirectAttributes redirectAttributes) {
        try {
            emailService.enviarCorreo(correoRequest);
            redirectAttributes.addFlashAttribute("mensaje", "Correo enviado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al enviar el correo: " + e.getMessage());
            
        }
        return "redirect:/user/PaginaCitasPaciente";
    }
    
}
