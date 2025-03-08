package com.sample.base.common.config.smtp;


import com.sample.base.common.config.env.DotEnvScheme;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class SmTpConfig {
    private final Dotenv dotenv;

    public String getHost(){
        return dotenv.get(DotEnvScheme.SMTP_HOST.name());
    }

    public int getPort(){
        return Integer.parseInt(dotenv.get(DotEnvScheme.SMTP_PORT.name()));
    }

    public String getUsername(){
        return dotenv.get(DotEnvScheme.SMTP_USERNAME.name());
    }

    public String getPassword(){
        return dotenv.get(DotEnvScheme.SMTP_PASSWORD.name());
    }

    public String getFromMail(){
        return dotenv.get(DotEnvScheme.SMTP_FROM_MAIL.name());
    }
}
