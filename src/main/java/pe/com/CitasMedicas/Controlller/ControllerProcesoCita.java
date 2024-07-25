package pe.com.CitasMedicas.Controlller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import pe.com.CitasMedicas.model.*;
import pe.com.CitasMedicas.servicio.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@Slf4j
public class ControllerProcesoCita {
    @Autowired
    EspecialidadServiceInpl especialidadServiceInpl;

    @Autowired
    MedicoServiceImpl medicoServiceImpl;

    @Autowired
    HorariosServiceImpl horarioServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioServiceImpl userDetailsService;

    @Autowired
    HistorialCitasServiceImpl historialCitasServiceImpl;

    @Autowired
    EstadoServiceImpl estadoServiceImpl;


    @GetMapping("/inicioCita")
    public String pagina1(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("usuario", userDetailsService.getNombre(user.getUsername()));
        return "paginaInicioCita";
    }

    @GetMapping("/user/plantillaConsultorios")
    public String seleccionarEspecialidad(@RequestParam("valor") String valor, Model model,
                                          @AuthenticationPrincipal User user) {
        model.addAttribute("usuario", userDetailsService.getNombre(user.getUsername()));
        var listaconsultorios = especialidadServiceInpl.listarConsultorios();
        model.addAttribute("consultorios", listaconsultorios);
        model.addAttribute("valor", valor);
        return "paginaConsultorios";
    }

    @GetMapping("/user/plantillaMedicos")
    public String seleccionarConsultorio(@Valid Especialidad especialidad, Model model,
                                         @AuthenticationPrincipal User user) {
        model.addAttribute("usuario", userDetailsService.getNombre(user.getUsername()));
        model.addAttribute("medicos", medicoServiceImpl.encontrarMedicoPorConsultorio(especialidad.getIdConsultorio()));
        model.addAttribute("nombreConsultorio", especialidad.getNombre());
        return "paginaMedicos";
    }

    @GetMapping("/user/obtenerHorariosCalendario")
    public String getCalendar(@RequestParam(name = "consultorio") String consultorio, Medicos medico, Model model,
                              @AuthenticationPrincipal User user) throws JsonProcessingException {
        model.addAttribute("usuario", userDetailsService.getNombre(user.getUsername()));
        List<Horarios> horarios = horarioServiceImpl.obtenerHorarioPorIdDoctor(medico.getIdMedico());

        // Si la lista de horarios es nula o vacía, inicializarla como una lista vacía
        if (horarios == null || horarios.isEmpty()) {
            horarios = Collections.emptyList();
        }

        // Convertir los horarios en un formato más simple para JavaScript
        List<Map<String, String>> horariosList = horarios.stream().map(horario -> Map.of(
                "id", horario.getId().toString(),
                "date", horario.getDia().toString(),
                "time", horario.getTime().toString(),
                "status", horario.getIdestadoCita().getEstado())).collect(Collectors.toList());

        // Convertir la lista a JSON
        String horariosJson = objectMapper.writeValueAsString(horariosList);
        log.info("Horarios JSON: {}", horariosJson);

        model.addAttribute("medico", medico.getNombre());
        model.addAttribute("idmedico", medico.getIdMedico());
        model.addAttribute("consultorio", consultorio);
        model.addAttribute("horariosMedico", horarios);
        model.addAttribute("horarios", horariosJson);
        model.addAttribute("baseUrl", "/user/reservarCita");

        return "paginaCalendario";
    }

    @GetMapping("/user/PaginaCitasPaciente")
    public String paginaCitasPaciente(Model model, @AuthenticationPrincipal User user) {
        Usuario usuario = userDetailsService.getUsuario(user.getUsername());
        model.addAttribute("citas", historialCitasServiceImpl.getHistorialCitas(usuario.getId_usuario()));
        return "paginaHistorialCitas";
    }

    @GetMapping("/user/PaginaCitasPacienteActivas")
    public String paginaCitasPacienteActivas(Model model, @AuthenticationPrincipal User user) {

        try {
            Usuario usuario = userDetailsService.getUsuario(user.getUsername());
            model.addAttribute("citas", historialCitasServiceImpl.getHistorialCitasActivos(usuario.getId_usuario(), "reservado"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "paginaCitasPaciente";
    }

    @GetMapping("/user/reservarCita")
    public String reservarCita(@RequestParam(name = "idestado") String idestado,
                               @RequestParam(name = "idhorario") String idhorario,
                               Model model, @AuthenticationPrincipal User user) {

        // Convertir el idhorario a Long
        Long idhorarioLong = Long.parseLong(idhorario);
        Long idEstado = Long.parseLong(idestado);

        // Obtener el horario por ID
        Optional<Horarios> horarioMedico = horarioServiceImpl.obtenerHorarioPorId(idhorarioLong);
        Usuario usuario = userDetailsService.getUsuario(user.getUsername());

        if (horarioMedico.isPresent()) {
            Horarios horario = horarioMedico.get();
            EstadoCita estadoCita = new EstadoCita();
            estadoCita.setIdestadoCita(idEstado);

            // Actualizar el estado de la cita
            horario.setIdestadoCita(estadoCita);
            HistorialCitados historialCitados = new HistorialCitados();
            historialCitados.setId(null);
            historialCitados.setFecha(horario.getDia());
            historialCitados.setHora(horario.getTime());
            historialCitados.setMedico(horario.getMedicos().getNombre());
            historialCitados.setConsultorio(horario.getMedicos().getEspecialidad().getNombre());
            historialCitados.setSituacion("reservado");
            historialCitados.setActividad("Consulta Medica");
            historialCitados.setPaciente(usuario.getNombre());
            historialCitados.setDni(usuario.getDni());
            historialCitados.setEmail(usuario.getCorreo());
            historialCitados.setIdHorario(idhorario);
            historialCitados.setIdpaciente(usuario.getId_usuario());
            historialCitasServiceImpl.guardar(historialCitados);
            String mensaje = "Estimado/a " + usuario.getNombre() + " " + usuario.getApellido() + ",\n\n" +
                    "Su cita ha sido reservada con los siguientes detalles:\n" +
                    "Fecha: " + horario.getDia() + "\n" +
                    "Hora: " + horario.getTime() + "\n" +
                    "Médico: " + horario.getMedicos().getNombre() + "\n" +
                    "Consultorio: " + horario.getMedicos().getEspecialidad().getNombre() + "\n" +
                    "Gracias por usar nuestro servicio.\n\n" +
                    "Atentamente,\n" +
                    "Dpto de Citas Médicas";

            CorreoRequest correoRequest = new CorreoRequest();
            correoRequest.setDestinatario(usuario.getCorreo());
            correoRequest.setAsunto("Confirmación de Reservación de Cita");
            correoRequest.setMensaje(mensaje);

            model.addAttribute("citas", historialCitasServiceImpl.getHistorialCitas(usuario.getId_usuario()));
            model.addAttribute("idhorario", horario.getId());
            model.addAttribute("fecha", horario.getDia());
            model.addAttribute("hora", horario.getTime());
            model.addAttribute("medico", horario.getMedicos().getNombre());
            model.addAttribute("consultorio", horario.getMedicos().getEspecialidad().getNombre());
            model.addAttribute("situacion", horario.getIdestadoCita().getEstado());
            model.addAttribute("horario", horarioMedico.get());
            model.addAttribute("apellido", usuario.getApellido());
            model.addAttribute("nombre", usuario.getNombre());
            model.addAttribute("dni", usuario.getDni());
            model.addAttribute("email", usuario.getCorreo());
            model.addAttribute("idestado", horario.getIdestadoCita());
            model.addAttribute("correoRequest", correoRequest);
            return "paginaReporteCita";
        } else {
            // Manejar el caso en que no se encuentre el horario
            model.addAttribute("error", "No se encontró el horario solicitado.");
            return "redirect:/user/PaginaCitasPaciente";
        }
    }

    @GetMapping("/user/paginaReporteCita")
    public String verCita() {
        return "redirest:/user/reservarCita";
    }

    @GetMapping("/user/cancelarCita")
    public String cancelarCita(@RequestParam Long id, @RequestParam Long idHorario,
                               RedirectAttributes redirectAttributes) {

        try {

            Horarios horario = horarioServiceImpl.obtenerHorario(idHorario);
            EstadoCita estadoCitaCancelado = estadoServiceImpl.encontrarEstado(1L); // Assuming 1L means available or cancelled

            // Set the new EstadoCita to the horario
            horario.setIdestadoCita(estadoCitaCancelado);

            // Save the updated horario
            horarioServiceImpl.guardarHorario(horario);
            // Update the estado of the HistorialCitados
            historialCitasServiceImpl.actualizarEstado(id, "Anulado"); // Estado 0 representa cancelado
            redirectAttributes.addFlashAttribute("mensaje", "Cita cancelada exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
        return "redirect:/user/PaginaCitasPacienteActivas";
    }

}
