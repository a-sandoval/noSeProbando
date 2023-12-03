package models.entities.domain.registro;


import com.google.common.hash.Hashing;
import server.Server;

import java.nio.charset.StandardCharsets;

public class Encriptador {
    public String encriptarContrasenia(String contrasenia) {
        return Hashing.sha256()
                .hashString(contrasenia, StandardCharsets.UTF_8)
                .toString();
    }


}

