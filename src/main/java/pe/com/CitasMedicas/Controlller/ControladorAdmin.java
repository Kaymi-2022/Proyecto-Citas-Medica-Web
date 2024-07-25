package pe.com.CitasMedicas.Controlller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import pe.com.CitasMedicas.model.Especialidad;
import pe.com.CitasMedicas.model.HistorialCitados;
import pe.com.CitasMedicas.model.Horarios;
import pe.com.CitasMedicas.model.Medicos;
import pe.com.CitasMedicas.model.Rol;
import pe.com.CitasMedicas.model.Usuario;
import pe.com.CitasMedicas.respository.RolRepository;
import pe.com.CitasMedicas.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class ControladorAdmin {

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioServiceImpl userService;

    @Autowired
    private HistorialCitasServiceImpl historialServiceimpl;

    @Autowired
    private EspecialidadServiceInpl especialidadServiceInpl;

    @Autowired
    private MedicoServiceImpl medicoServiceImpl;

    @Autowired
    private HorariosServiceImpl HorariosServiceImpl;

    @Autowired
    private EstadoServiceImpl estadoServiceImpl;

    @Autowired
    private UsuarioServiceImpl usuarioServiceImpl;

    @Autowired
    private RolRepository rolRepository;

    String mensaje = null;

    // Método que se ejecuta al entrar a la URL /table-principal
    @GetMapping("/table-principal")
    public String tableadmin(Model model, @AuthenticationPrincipal User user) {
        var users = userService.findAllUsersWithRoleNames();
        var usersadmin = userService.countUsersWithAdminRole("ADMIN");
        var userspatient = userService.countUsersWithAdminRole("PATIENT");
        var usersdoctor = userService.countUsersWithAdminRole("DOCTOR");
        model.addAttribute("usuariosConRoles", users);
        model.addAttribute("totaldoadmin", usersadmin);
        model.addAttribute("totalpatient", userspatient);
        model.addAttribute("totaldoctor", usersdoctor);
        model.addAttribute("usuario", userService.getNombre(user.getUsername()));
        log.info("usuario que hizo login:" + user);
        return "table-usuarios";
    }

    // Método que se ejecuta al entrar a la URL /table-citados
    @GetMapping("/table-citados")
    public String tableCitados(Model model, @AuthenticationPrincipal User user) {
        var historial = historialServiceimpl.listarHistorialPaciente();
        model.addAttribute("citas", historial);
        model.addAttribute("usuario", userService.getNombre(user.getUsername()));
        log.info("usuario que hizo login:" + user);
        return "table-citados";
    }

    // Método que se ejecuta al entrar a la URL /guardarUsuario
    @PostMapping("/user/guardarUsuario")
    public String guardarUsuario(Usuario usuario, Errors errores, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            mensaje = "error";
            return "redirect:/home";
        } else {
            userService.guardar(usuario);
            mensaje = "success";
        }
        flash.addFlashAttribute("mensaje", mensaje);
        return "redirect:/login";
    }

    // Método que se ejecuta al entrar a la URL /eliminarUsuario
    @GetMapping("/eliminarUsuario")
    public String eliminarUsuario(Usuario usuario, Errors errores, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            mensaje = "error";
        } else {
            userService.eliminar(usuario);
            mensaje = "success";
        }
        flash.addFlashAttribute("mensaje", mensaje);
        return "redirect:/table-principal";
    }

    // Método que se ejecuta al entrar a la URL /tabla-consultorios
    @GetMapping("/table-consultorios")
    public String tableConsultorio(Especialidad especialidad, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("consultorios", especialidadServiceInpl.listarConsultorios());
        model.addAttribute("usuario", userService.getNombre(user.getUsername()));
        return "table-consultorios";
    }

    @PostMapping("/guardarConsultorio")
    public String guardarConsultorio(@RequestParam(name = "file", required = false) MultipartFile file,
            Especialidad especialidad, BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("mensaje", "Error en la validación de los datos");
            log.info("Error al guardar: " + result.getAllErrors());
            return "redirect:/table-consultorios";
        }

        // Chequear si el consultorio existe
        if (especialidadServiceInpl.existeConsultorio(especialidad.getNombre()) != null) {
            flash.addFlashAttribute("mensaje", "El consultorio ya existe");
            return "redirect:/table-consultorios";
        }

        if (file != null && !file.isEmpty()) {
            String ruta = "E://CitasMedicas/src/main/resources/static/img/fotos/";

            try {
                byte[] bytes = file.getBytes();
                Path rutaAbsoluta = Paths.get(ruta + file.getOriginalFilename());

                // Asegurar que el directorio exista
                Files.createDirectories(rutaAbsoluta.getParent());

                Files.write(rutaAbsoluta, bytes);
                especialidad.setFoto(file.getOriginalFilename());
            } catch (IOException e) {
                log.error("Error al guardar la foto: ", e);
                flash.addFlashAttribute("mensaje", "Error al guardar la foto");
                return "redirect:/table-consultorios";
            }
        }

        try {
            especialidadServiceInpl.guardar(especialidad);
            flash.addFlashAttribute("mensaje", "Consultorio registrado con éxito");
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad de datos: ", e);
            flash.addFlashAttribute("mensaje", "Error de integridad de datos");
        } catch (Exception e) {
            log.error("Error al guardar el consultorio: ", e);
            flash.addFlashAttribute("mensaje", "Error al guardar el consultorio");
        }

        return "redirect:/table-consultorios";
    }

    // Método que se ejecuta al entrar a la URL /eliminar Consultorio
    @GetMapping("/eliminarConsultorio")
    public String eliminarConsultorio(Especialidad especialidad, BindingResult errors, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            flash.addFlashAttribute("mensaje", "error");
            log.info("Error al eliminar" + errors.getAllErrors());
            return "redirect:/table-consultorios";
        }
        especialidadServiceInpl.eliminar(especialidad);
        flash.addFlashAttribute("mensaje", "success");
        return "redirect:/table-consultorios";
    }

    // Método que se ejecuta al entrar a la URL /tabla doctores
    @GetMapping("/table-doctores")
    public String tableDoctores(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("consultorios", especialidadServiceInpl.listarConsultorios());
        model.addAttribute("medicos", medicoServiceImpl.listarMedicos());
        model.addAttribute("usuario", userService.getNombre(user.getUsername()));
        return "table-doctores";
    }

    // Método que se ejecuta al entrar a la URL /guardar Medico
    @PostMapping("/guardarMedico")
    public String tableDoctores(@RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "idConsultorio") Long idConsultorio,
            Medicos medicos, BindingResult result,
            RedirectAttributes flash) {

        if (result.hasErrors()) {

            flash.addFlashAttribute("error", "Hay errores en el formulario");
            log.info("Error al guardar: " + idConsultorio + " " + result.getAllErrors());
            return "redirect:/table-doctores";
        }

        if (medicoServiceImpl.encontrarMedicoPorNombre(medicos.getNombre()) != null) {
            flash.addFlashAttribute("mensaje", "El médico ya existe");
            return "redirect:/table-doctores";
        }

        // Obtener el consultorio por su ID
        Optional<Especialidad> consultorioOpt = especialidadServiceInpl.obtenerConsultorioPorId(idConsultorio);
        if (!consultorioOpt.isPresent()) {
            flash.addFlashAttribute("mensaje", "error");
            return "redirect:/table-doctores";
        }

        Especialidad especialidad = consultorioOpt.get();
        medicos.setEspecialidad(especialidad);

        if (file != null && !file.isEmpty()) {
            String ruta = "E://CitasMedicas/src/main/resources/static/img/fotos/";

            try {
                byte[] bytes = file.getBytes();
                Path rutaAbsoluta = Paths.get(ruta + file.getOriginalFilename());

                // Asegurarse de que el directorio exista
                Files.createDirectories(rutaAbsoluta.getParent());

                Files.write(rutaAbsoluta, bytes);
                medicos.setFoto(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                log.info("Error al guardar: " + e.getMessage());
                flash.addFlashAttribute("mensaje", "error");
                return "redirect:/table-doctores";
            }
        }

        medicoServiceImpl.guardar(medicos);
        flash.addFlashAttribute("mensaje", "success");
        return "redirect:/table-doctores";
    }

    // Método que se ejecuta al entrar a la URL /eliminar Medico
    @GetMapping("/eliminarMedico")
    public String eliminarMedico(Medicos medicos, RedirectAttributes flash, BindingResult errors) {

        if (errors.hasErrors()) {
            flash.addFlashAttribute("mensaje", "error");
            log.info("Error al eliminar" + errors.getAllErrors());
            return "redirect:/table-doctores";
        }
        medicoServiceImpl.eliminar(medicos);
        flash.addFlashAttribute("mensaje", "success");
        return "redirect:/table-doctores";
    }

    // Método que se ejecuta al entrar a la URL /tabla-horarios
    @GetMapping("/table-horarios")
    public String tableHorarios(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("medicos", medicoServiceImpl.listarMedicos());
        model.addAttribute("estadosCita", estadoServiceImpl.listarEstados());
        model.addAttribute("horarios", HorariosServiceImpl.listarHorarios());
        model.addAttribute("usuario", userService.getNombre(user.getUsername()));
        return "table-horarios";
    }

    // Método que se ejecuta al entrar a la URL /guardar Horario
    @PostMapping("/admin/guardarHorario")
    public String guardarHorario(Horarios horario, BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("mensaje", "Hay errores en el formulario");
            log.info("Error al guardar: " + result.getAllErrors());
            return "redirect:/table-horarios";
        }

        // Verificar si ya existe un horario con el mismo medico_id, dia y time
        if (HorariosServiceImpl.existsByMedicoAndDiaAndTime(horario.getMedicos(), horario.getDia(),
                horario.getTime())) {
            flash.addFlashAttribute("mensaje",
                    "Ya existe un horario registrado para este médico en la fecha y hora seleccionadas");
            return "redirect:/table-horarios";
        }

        try {
            HorariosServiceImpl.guardarHorario(horario);
            flash.addFlashAttribute("mensaje", "Horario registrado con éxito");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flash.addFlashAttribute("mensaje", "Ocurrió un error al registrar el horario");
            return "redirect:/table-horarios";
        }

        return "redirect:/table-horarios";
    }

    // Metodo que se ejecuta al entrar a la URL /eliminarHorario
    @GetMapping("/eliminarHorario")
    public String eliminarHorario(Horarios horario, RedirectAttributes flash, BindingResult errors) {
        if (errors.hasErrors()) {
            flash.addFlashAttribute("mensaje", "error");
            log.info("Error al eliminar" + errors.getAllErrors());
            return "redirect:/table-horarios";
        }
        HorariosServiceImpl.eliminarHorario(horario);
        flash.addFlashAttribute("mensaje", "success");
        return "redirect:/table-horarios";
    }

    // Método que se ejecuta al entrar a la URL /eliminarCitado
    @GetMapping("/eliminarCitado")
    public String eliminarCitado(RedirectAttributes flash, HistorialCitados historialCitados, Errors errores) {
        if (errores.hasErrors()) {
            flash.addFlashAttribute("situacion", "error");
        } else {
            historialServiceimpl.eliminar(historialCitados);
            flash.addFlashAttribute("situacion", "success");
        }
        return "redirect:/table-citados";
    }

    // Método que se ejecuta al entrar a la URL /guardarPaciente
    @PostMapping("/guardarPaciente")
    public String guardarPaciente(@Valid HistorialCitados historialCitados, Errors errores, Model model,
            @AuthenticationPrincipal User user) {
        String respuesta = null;
        if (errores.hasErrors()) {
            respuesta = "error";
            log.info("Error al eliminar" + errores.getAllErrors());
            model.addAttribute("situacion", respuesta);
        } else {
            historialServiceimpl.guardar(historialCitados);
            respuesta = "success";
            model.addAttribute("situacion", respuesta);

        }
        var historial = historialServiceimpl.listarHistorialPaciente();
        model.addAttribute("citas", historial);
        model.addAttribute("usuario", userService.getNombre(user.getUsername()));
        return "table-citados";
    }

    // Método que se ejecuta al entrar a la URL /tablas graficas
    @GetMapping("/charts")
    public String getTotalCitadosPorEspecialidad(Model model, @AuthenticationPrincipal User user) {
        // Datos de citados por especialidad
        List<HistorialCitasServiceImpl.EspecialidadTotalDTO> especialidadData = historialServiceimpl
                .getTotalCitadosByEspecialidad();

        // Datos de especialidad
        List<String> especialidadLabels = especialidadData.stream()
                .map(HistorialCitasServiceImpl.EspecialidadTotalDTO::getEspecialidad)
                .collect(Collectors.toList());
        // Datos de total
        List<Long> especialidadTotals = especialidadData.stream()
                .map(HistorialCitasServiceImpl.EspecialidadTotalDTO::getTotal)
                .collect(Collectors.toList());

        // Datos de situación por consultorio
        List<HistorialCitasServiceImpl.SituacionCountDTO> situacionData = historialServiceimpl.getSituacionCount();

        // Datos de situación
        List<String> situacionLabels = situacionData.stream()
                .map(HistorialCitasServiceImpl.SituacionCountDTO::getSituacion)
                .collect(Collectors.toList());

        // Datos de total
        List<Long> situacionTotals = situacionData.stream()
                .map(HistorialCitasServiceImpl.SituacionCountDTO::getTotal)
                .collect(Collectors.toList());

        // Obtener datos por día
        List<HistorialCitasServiceImpl.DiaCountDTO> diaData = historialServiceimpl.getCountByDay();

        // Crear etiquetas de fechas en formato "día/mes/año"
        List<String> fechaDiaLabels = diaData.stream()
                .map(diaCount -> diaCount.getDay() + "/" + diaCount.getMonth() + "/" + diaCount.getYear())
                .collect(Collectors.toList());

        // Crear lista de totales
        List<Long> fechaDiaTotals = diaData.stream()
                .map(HistorialCitasServiceImpl.DiaCountDTO::getCount)
                .collect(Collectors.toList());

        // Agregar datos al modelo
        model.addAttribute("especialidadLabels", especialidadLabels);
        model.addAttribute("especialidadTotals", especialidadTotals);
        model.addAttribute("situacionLabels", situacionLabels);
        model.addAttribute("situacionTotals", situacionTotals);
        model.addAttribute("fechaLabels", fechaDiaLabels);
        model.addAttribute("fechaTotals", fechaDiaTotals);
        model.addAttribute("usuario", userService.getNombre(user.getUsername()));
        return "charts";
    }

    @PostMapping("/register")
    public String registrarUsuario(Usuario usuario, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("mensaje", "errorValidacion");
            return "redirect:/login";
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Buscar el rol "PATIENT" y asignarlo al usuario
        Rol patientRole = rolRepository.findByNombre("PATIENT");
        if (patientRole == null) {
            redirectAttributes.addFlashAttribute("mensaje", "errorRolNoEncontrado");
            return "redirect:/login";
        }
        usuario.addRole(patientRole);

        String respuesta;

        try {
            Usuario usuarioRegistrado = usuarioServiceImpl.registrarUsuario(usuario);
            if (usuarioRegistrado != null) {
                respuesta = "successRegister";
            } else {
                respuesta = "errorRegister";
            }
        } catch (UsuarioExistenteException e) {
            redirectAttributes.addFlashAttribute("mensaje", "error Usuario Existente");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "error General");
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("mensaje", respuesta);
        return "redirect:/login";
    }
}
