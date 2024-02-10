package io.temporal.sample_project;

public class Main {
    public static void main(String[] args) {
        HelloWorldService helloWorldService = new HelloWorldServiceImpl();
        helloWorldService.sayHello();
    }
}
