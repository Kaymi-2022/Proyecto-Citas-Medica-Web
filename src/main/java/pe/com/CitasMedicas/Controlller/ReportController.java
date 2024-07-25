package pe.com.CitasMedicas.Controlller;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import java.nio.file.Path;

@Controller
public class ReportController {

    @GetMapping("/user/imprimirReporte")
    public void generateReport(HttpServletResponse response, HttpServletRequest request,
            @RequestParam("fecha") String fecha,
            @RequestParam("hora") String hora,
            @RequestParam("medico") String medico,
            @RequestParam("nombreCompleto") String nombreCompleto,
            @RequestParam("consultorio") String consultorio){

        try {
            System.out.println(rutaReportes());
            JasperReport oReporte = (JasperReport) JRLoader
                    .loadObject(new File(rutaReportes() + "RepCitaMedica.jasper"));

            // Parámetros del reporte
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("fecha", fecha);
            parametros.put("hora", hora);
            parametros.put("medico", medico);
            parametros.put("nombreCompleto", nombreCompleto);
            parametros.put("consultorio", consultorio);

            byte[] bytes = JasperRunManager.runReportToPdf(oReporte, parametros, new JREmptyDataSource());

            response.setContentType("application/pdf");
            OutputStream out = response.getOutputStream();
            out.write(bytes);

            // Crear los headers de la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=RepCitaMedica.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        } catch (JRException e) {
            System.out.println("Error JRException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error IOException: " + e.getMessage());
        }
    }

    public static String rutaReportes() {
        // Construye la ruta a 'src/main/resources'
        Path resourcePath = Paths.get("src", "main", "resources", "reportes");
        return resourcePath.toAbsolutePath().toString() + File.separator;
    }

}
