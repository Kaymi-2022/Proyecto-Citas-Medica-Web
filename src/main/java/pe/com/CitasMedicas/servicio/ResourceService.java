package pe.com.CitasMedicas.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

@Service
public class ResourceService {

    @Autowired
    private ResourceLoader resourceLoader;

    public String getResourcePath(String resourceLocation) throws IOException, URISyntaxException {
        Resource resource = resourceLoader.getResource(resourceLocation);
        return Paths.get(resource.getURI()).toString();
    }
}
