package com.example.crud_menu.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class SpringDataSourceProperties {

    @Value("${spring.datasource.url:jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username:sa}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password:#{null}}")
    private String dataSourcePassword;

    @PostConstruct
    public void logDataSourceProperties() {
        log.info("dataSource url {}", dataSourceUrl);
        log.info("dataSource username {}", dataSourceUsername);

        log.info("dataSource password:  {}", maskedPassword(dataSourcePassword));
        String expressionValue = currenDataSourceUrl();
        log.info("expression value {}", expressionValue);
    }

    private String maskedPassword(String password) {
        if (password == null || password.isBlank() || password.isEmpty()) {
            return "N/A";
        }
        return password.replaceAll(".", "*");
    }

    public String currenDataSourceUrl() {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        context.setVariable("url", dataSourceUrl);
        context.setVariable("username", dataSourceUsername);

        log.debug("Evaluating SpEL with variables - URL: {}, Username: {}", dataSourceUrl, dataSourceUsername);
//        String expressionData = "'dataBaseUrl:' #url + ', username: '";
        String expressionData = "#url";
        try {
            String sampleExpression = Objects.requireNonNull(parser.parseExpression(expressionData).getValue(context, String.class));
            log.debug("Expression level for expressionData and sampleData, {}, {}", expressionData, sampleExpression);

            String complexExpression = "'Database URL: ' + #url + ', Username: ' + #username";
            String fullExpression = parser.parseExpression(complexExpression).getValue(context, String.class);
            log.debug("Evaluating spEL variables with both URL and password, {}, {} ", complexExpression, fullExpression);

        } catch (Exception e) {
            log.error("Error evaluating spEL expression, {}",  e);
            return "spEL expression error!";
        }


        return parser.parseExpression(expressionData).getValue(context, String.class);


    }

}

