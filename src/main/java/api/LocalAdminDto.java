package api;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LocalAdminDto {
    private String cmd;
    private String name;
    private String email;
    private String x_password;
}
