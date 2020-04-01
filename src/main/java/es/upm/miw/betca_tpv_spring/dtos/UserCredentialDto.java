package es.upm.miw.betca_tpv_spring.dtos;

import javax.validation.constraints.NotNull;

public class UserCredentialDto {

    @NotNull
    private String password;

    @NotNull
    private String newPassword;

    public UserCredentialDto() {
        this("", "");
    }

    public UserCredentialDto(String password, String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
