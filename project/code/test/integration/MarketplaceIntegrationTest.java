package integration;

import model.Product;
import repository.Database;
import service.AuthService;
import service.ProductService;
import service.SearchService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MarketplaceIntegrationTest {

	@BeforeEach
	void resetDatabaseSingleton() throws Exception {
		Field instance = Database.class.getDeclaredField("instance");
		instance.setAccessible(true);
		instance.set(null, null);
	}

	@Test
	void registerLoginPublishAndSearch_shouldWorkAcrossServices() {
		AuthService auth = new AuthService();
		assertTrue(auth.registerUser("user1", "u1@example.com", "13812345678", "123456"));
		assertTrue(auth.login("user1", "123456"));

		Database db = Database.getInstance();
		Long category1 = db.getAllCategories().get(0).getId();
		Long category2 = db.getAllCategories().get(1).getId();

		ProductService productService = new ProductService(auth);
		Product iphone = productService.createProduct(
				"iPhone 13",
				new BigDecimal("1000"),
				"Good condition, 128GB",
				category1);
		assertNotNull(iphone);

		Product book = productService.createProduct(
				"Calculus Book",
				new BigDecimal("50"),
				"Math textbook",
				category2);
		assertNotNull(book);

		SearchService searchService = new SearchService();

		List<Product> byKeyword = searchService.search("iphone");
		assertTrue(byKeyword.stream().anyMatch(p -> p.getId().equals(iphone.getId())));
		assertFalse(byKeyword.stream().anyMatch(p -> p.getId().equals(book.getId())));

		List<Product> filteredRight = searchService.searchWithFilters("iphone", category1);
		assertTrue(filteredRight.stream().anyMatch(p -> p.getId().equals(iphone.getId())));

		List<Product> filteredWrong = searchService.searchWithFilters("iphone", category2);
		assertFalse(filteredWrong.stream().anyMatch(p -> p.getId().equals(iphone.getId())));

		List<Product> priceInRange = searchService.searchByPriceRange(null, 500.0, 1500.0);
		assertTrue(priceInRange.stream().anyMatch(p -> p.getId().equals(iphone.getId())));
		assertFalse(priceInRange.stream().anyMatch(p -> p.getId().equals(book.getId())));

		List<Product> allPublished = searchService.search(" ");
		assertTrue(allPublished.stream().anyMatch(p -> p.getId().equals(iphone.getId())));
		assertTrue(allPublished.stream().anyMatch(p -> p.getId().equals(book.getId())));
	}
}
