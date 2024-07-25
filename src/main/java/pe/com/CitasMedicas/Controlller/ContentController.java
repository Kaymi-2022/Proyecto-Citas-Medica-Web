package pe.com.CitasMedicas.Controlller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContentController {

  @GetMapping("/admin/home")
  public String handleAdminHome() {
    return "home_admin";
  }

  @GetMapping("/user/home")
  public String handleUserHome() {
    return "home_user";
  }

  @GetMapping("/login")
  public String handleLogin(@RequestParam(value = "error", required = false) String error, Model model) {
    if (error != null) {
      model.addAttribute("mensaje", "Usuario o contraseña incorrectos");
    }
    return "custom_login";
  }
}
