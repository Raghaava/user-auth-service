package com.example.ws.ui.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PasswordResetRequestModel implements Serializable {
    private static final long serialVersionUID = -2330492483721305636L;
    private String email;
}
