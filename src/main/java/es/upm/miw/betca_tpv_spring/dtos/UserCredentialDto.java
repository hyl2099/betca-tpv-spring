package es.upm.miw.betca_tpv_spring.dtos;

import javax.validation.constraints.NotNull;

public class UserCredentialDto {

    @NotNull
    private String mobile;

    @NotNull
    private String newPassword;

    public UserCredentialDto() {
        this("", "");
    }

    public UserCredentialDto(String mobile, String newPassword) {
        this.mobile = mobile;
        this.newPassword = newPassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
