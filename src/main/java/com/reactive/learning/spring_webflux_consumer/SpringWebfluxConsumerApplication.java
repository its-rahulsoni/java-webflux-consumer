package com.reactive.learning.spring_webflux_consumer;

import com.reactive.learning.spring_webflux_consumer.pojo.Book;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication
public class SpringWebfluxConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxConsumerApplication.class, args);

		// Create a reactive WebClient
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080/").build();

		// Consume the books stream (Flux)
		webClient.get().uri("v1/books")
				.retrieve()
				.bodyToFlux(Book.class)         // Convert HTTP response body into Flux<Book>
				.log()                          // Log every emitted signal (for debugging)
				.subscribe(new Subscriber() {   // Subscribe to consume the data
					Subscription s;
					@Override
					public void onSubscribe(Subscription s) {
						s.request(1);             // Request 1 element from Flux
						this.s = s;
					}
					@Override
					public void onNext(Object o) {
						Book b = (Book) o;
						System.out.println(b.getName() + " from flux");
						s.request(1);             // Request the next element
					}
					@Override
					public void onError(Throwable t) {
						System.err.println("Error occurred: " + t);
					}
					@Override
					public void onComplete() {
						System.out.println("Flux stream complete!");
					}
				});
		// Consume a single book (Mono)
		webClient.get().uri("v1/books/1")
				.retrieve()
				.bodyToMono(Book.class)         // Convert to Mono<Book>
				.log()                          // Log signals for debugging
				.subscribe(new Subscriber() {
					@Override
					public void onSubscribe(Subscription s) {
						s.request(1);             // Request element from Mono
					}
					@Override
					public void onNext(Object o) {
						Book b = (Book) o;
						System.out.println(b.getName() + " from mono");
					}
					@Override
					public void onError(Throwable t) {
						System.err.println("Error occurred: " + t);
					}
					@Override
					public void onComplete() {
						System.out.println("Mono complete!");
					}
				});
	}
}