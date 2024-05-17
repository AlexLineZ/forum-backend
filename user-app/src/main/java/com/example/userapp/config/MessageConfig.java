package com.example.userapp.config;

public class MessageConfig {
    public static final String SUBJECT_MESSAGE = "Подтверждение регистрации";
    public static final String EMAIL_MESSAGE = "<html>" +
            "<body>" +
            "<h1>Добро пожаловать на самый крутой форум!</h1>" +
            "<p>Чтобы подтвердить свой аккаунт, перейдите по ссылке ниже:</p>" +
            "<a href=\"http://localhost:8084/api/auth/confirm-account?token={token}\">Подтвердить аккаунт</a>" +
            "<p>Если кнопка не работает, то скопируйте ссылку ниже и перейдите по ней:</p>" +
            "<p>http://localhost:8084/api/auth/confirm-account?token={token}</p>" +
            "<p>Удачного общения и не нарушайте правила!</p>" +
            "</body>" +
            "</html>";
}
