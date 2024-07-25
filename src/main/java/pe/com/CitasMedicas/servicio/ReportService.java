package pe.com.CitasMedicas.servicio;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    public File exportReport(String idhorario, String fecha, String hora, String medico, String consultorio,
            String situacion, String apellido, String nombre, String dni, String email,
            String idestado, String reportFormat) {
        try {
            String path = "C:\\Users\\USER\\Desktop\\Reporte";
            System.out.println("Ruta del directorio de salida: " + path);

        
            // Crear un objeto Map para los par�metros
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("nombreCitado", nombre + " " + apellido);
            parameters.put("nameConsultorio", consultorio);
            parameters.put("nameActividad", "Consulta M�dica");
            parameters.put("fechaCitado", fecha);
            parameters.put("turnoCitado", hora);
            parameters.put("imageDir", "classpath:static/img/imgReport/");
            parameters.put("nameMedico", medico);

            // Obtener el archivo JRXML desde el classpath
            File file = ResourceUtils.getFile("classpath:Blank_A4.jrxml");

            System.out.println("Ruta del archivo de reporte: " + file.getAbsolutePath());

            // Compilar el reporte
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            System.out.println("Reporte compilado con �xito");

            // Usar una fuente de datos vac�a si no tienes una colecci�n de objetos para el reporte
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.emptyList());

            // Llenar el reporte con los par�metros y la fuente de datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            System.out.println("Reporte llenado con �xito");

            File outputFile = null;

            // Exportar el reporte basado en el formato especificado
            if ("html".equalsIgnoreCase(reportFormat)) {
                outputFile = new File(path, "cita.html");
                JasperExportManager.exportReportToHtmlFile(jasperPrint, outputFile.getAbsolutePath());
            } else if ("pdf".equalsIgnoreCase(reportFormat)) {
                outputFile = new File(path, "cita.pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile.getAbsolutePath());
            } else {
                throw new IllegalArgumentException("Formato de reporte no soportado: " + reportFormat);
            }
            System.out.println("Reporte exportado con �xito en formato: " + reportFormat);

            if (outputFile != null && outputFile.exists()) {
                System.out.println("Archivo de reporte creado: " + outputFile.getAbsolutePath());
                return outputFile;
            } else {
                throw new IllegalStateException("No se pudo crear el archivo de reporte");
            }
        } catch (Exception e) {
            System.err.println("Reporte Service: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}

