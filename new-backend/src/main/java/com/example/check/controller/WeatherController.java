//package com.example.check.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 天气API代理控制器
// * 用于解决前端直接调用天气API的CORS跨域问题
// */
//@RestController
//@RequestMapping("/api/weather")
//@CrossOrigin(origins = "*")
//public class WeatherController {
//
//    @Autowired
//    private WebClient.Builder webClientBuilder;
//
//    // 和风天气API配置
//    private static final String QWEATHER_API_HOST = "https://pn6yvy5tx8.re.qweatherapi.com";
//    private static final String QWEATHER_BEARER_TOKEN = "KEWGF2UVAK";
//
//    private WebClient webClient;
//
//    public WeatherController() {
//        this.webClient = WebClient.builder()
//                .baseUrl(QWEATHER_API_HOST)
//                .defaultHeader("Authorization", "Bearer " + QWEATHER_BEARER_TOKEN)
//                .defaultHeader("Content-Type", "application/json")
//                .build();
//    }
//
//    /**
//     * 城市搜索API代理
//     *
//     * @param location 城市关键字
//     * @return 城市列表
//     */
//    @GetMapping("/cities/search")
//    public Mono<ResponseEntity<Map<String, Object>>> searchCities(
//            @RequestParam String location) {
//
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/geo/v2/city/lookup")
//                        .queryParam("location", location)
//                        .build())
//                .retrieve()
//                .bodyToMono(Map.class)
//                .map(data -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("success", true);
//                    response.put("data", data);
//                    return ResponseEntity.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(response);
//                })
//                .onErrorResume(error -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("success", false);
//                    response.put("message", "搜索城市失败: " + error.getMessage());
//                    response.put("data", new Object[0]);
//                    return Mono.just(ResponseEntity.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(response));
//                });
//    }
//
//    /**
//     * 获取实时天气API代理
//     *
//     * @param location 城市ID
//     * @return 实时天气数据
//     */
//    @GetMapping("/now")
//    public Mono<ResponseEntity<Map<String, Object>>> getCurrentWeather(
//            @RequestParam String location) {
//
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v7/weather/now")
//                        .queryParam("location", location)
//                        .build())
//                .retrieve()
//                .bodyToMono(Map.class)
//                .map(data -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("success", true);
//                    response.put("data", data);
//                    return ResponseEntity.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(response);
//                })
//                .onErrorResume(error -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("success", false);
//                    response.put("message", "获取天气失败: " + error.getMessage());
//                    return Mono.just(ResponseEntity.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(response));
//                });
//    }
//
//    /**
//     * 获取3天天气预报API代理
//     *
//     * @param location 城市ID
//     * @return 3天天气预报
//     */
//    @GetMapping("/forecast/3d")
//    public Mono<ResponseEntity<Map<String, Object>>> getWeather3Days(
//            @RequestParam String location) {
//
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v7/weather/3d")
//                        .queryParam("location", location)
//                        .build())
//                .retrieve()
//                .bodyToMono(Map.class)
//                .map(data -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("success", true);
//                    response.put("data", data);
//                    return ResponseEntity.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(response);
//                })
//                .onErrorResume(error -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("success", false);
//                    response.put("message", "获取天气预报失败: " + error.getMessage());
//                    return Mono.just(ResponseEntity.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(response));
//                });
//    }
//
//    /**
//     * 获取7天天气预报API代理
//     *
//     * @param location 城市ID
//     * @return 7天天气预报
//     */
//    @GetMapping("/forecast/7d")
//    public Mono<ResponseEntity<Map<String, Object>>> getWeather7Days(
//            @RequestParam String location) {
//
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v7/weather/7d")
//                        .queryParam("location", location)
//                        .build())
//                .retrieve()
//                .bodyToMono(Map.class)
//                .map(data -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("success", true);
//                    response.put("data", data);
//                    return ResponseEntity.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(response);
//                })
//                .onErrorResume(error -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("success", false);
//                    response.put("message", "获取天气预报失败: " + error.getMessage());
//                    return Mono.just(ResponseEntity.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(response));
//                });
//    }
//
//    /**
//     * 获取30天天气预报API代理
//     *
//     * @param location 城市ID
//     * @return 30天天气预报
//     */
//    @GetMapping("/forecast/30d")
//    public Mono<ResponseEntity<Map<String, Object>>> getWeather30Days(
//            @RequestParam String location) {
//
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v7/weather/30d")
//                        .queryParam("location", location)
//                        .build())
//                .retrieve()
//                .bodyToMono(Map.class)
//                .map(data -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("success", true);
//                    response.put("data", data);
//                    return ResponseEntity.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(response);
//                })
//                .onErrorResume(error -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("success", false);
//                    response.put("message", "获取30天天气预报失败: " + error.getMessage());
//                    return Mono.just(ResponseEntity.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(response));
//                });
//    }
//}
//
