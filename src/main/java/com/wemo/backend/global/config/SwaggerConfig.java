package com.wemo.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addTagsItem(new Tag().name("Users").description("사용자 관련 API"))
                .addTagsItem(new Tag().name("Images").description("이미지 관련 API"))
                .addTagsItem(new Tag().name("Meetings").description("모임 관련 API"))
                .addTagsItem(new Tag().name("Plans").description("일정 관련 API"))
                .addTagsItem(new Tag().name("Reviews").description("후기 관련 API"))
                .addTagsItem(new Tag().name("Regions").description("지역 관련 API"))
                .addTagsItem(new Tag().name("Tokens").description("토큰 관련 API"))
                .addTagsItem(new Tag().name("Oauth2").description("Oauth2 관련 API"));

    }

    private Info apiInfo() {

        return new Info()
                .title("WeMo의 백엔드 서버")
                .description("WeMo의 백엔드 서버입니다.")
                .version("1.0");
    }

}
