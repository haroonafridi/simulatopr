package com.hkcapital.portoflio.config;

import com.hkcapital.portoflio.service.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "etoro")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EtoroApiConfiguration implements Service
{
    private String apiKey;
    private String userKey;
    private String limitOrderUrl;
    private String marketOrderUrl;
    private String instrumentCandleDataUrl;
    private String orderInformationUrl;
    private String portfolioInformationUrl;

    public String getXRequestId() {
        return "x-request-id";
    }

    public String getXApiKey() {
        return "x-api-key";
    }

    public String getXUserKey() {
        return "x-user-key";
    }
}
