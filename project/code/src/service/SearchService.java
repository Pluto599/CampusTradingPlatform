package service;

import model.Product;
import repository.Database;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {
	private Database db;

	public SearchService() {
		this.db = Database.getInstance();
	}

	/**
	 * 搜索商品(按标题和描述)
	 */
	public List<Product> search(String query) {
		if (query == null || query.trim().isEmpty()) {
			return db.getProductsByStatus("PUBLISHED");
		}

		String lowerQuery = query.toLowerCase();
		return db.getProductsByStatus("PUBLISHED").stream()
				.filter(p -> p.getTitle().toLowerCase().contains(lowerQuery) ||
						p.getDescription().toLowerCase().contains(lowerQuery))
				.collect(Collectors.toList());
	}

	/**
	 * 按分类和关键词筛选
	 */
	public List<Product> searchWithFilters(String query, Long categoryId) {
		List<Product> results = search(query);

		if (categoryId != null) {
			results = results.stream()
					.filter(p -> p.getCategoryId().equals(categoryId))
					.collect(Collectors.toList());
		}

		return results;
	}

	/**
	 * 价格范围筛选
	 */
	public List<Product> searchByPriceRange(String query, Double minPrice, Double maxPrice) {
		List<Product> results = search(query);

		if (minPrice != null) {
			results = results.stream()
					.filter(p -> p.getPrice().doubleValue() >= minPrice)
					.collect(Collectors.toList());
		}

		if (maxPrice != null) {
			results = results.stream()
					.filter(p -> p.getPrice().doubleValue() <= maxPrice)
					.collect(Collectors.toList());
		}

		return results;
	}
}
