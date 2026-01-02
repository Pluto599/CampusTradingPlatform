package service;

import model.*;
import repository.Database;
import util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
	private Database db;
	private AuthService authService;

	public ProductService(AuthService authService) {
		this.db = Database.getInstance();
		this.authService = authService;
	}

	/**
	 * 发布商品
	 */
	public Product createProduct(String title, BigDecimal price, String description, Long categoryId) {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return null;
		}

		// 验证输入
		if (!ValidationUtil.isValidProductTitle(title)) {
			System.out.println("商品标题不合法！(1-100字符)");
			return null;
		}

		if (!ValidationUtil.isValidDescription(description)) {
			System.out.println("商品描述过长！(最多1000字符)");
			return null;
		}

		if (price.compareTo(BigDecimal.ZERO) <= 0) {
			System.out.println("价格必须大于0！");
			return null;
		}

		if (db.getCategoryById(categoryId) == null) {
			System.out.println("分类不存在！");
			return null;
		}

		// 创建商品
		Long productId = db.generateProductId();
		User currentUser = authService.getCurrentUser();
		Product product = new Product(productId, title, price, description, currentUser.getId(), categoryId);
		db.addProduct(product);

		System.out.println("商品发布成功！商品ID: " + productId);
		return product;
	}

	/**
	 * 编辑商品
	 */
	public boolean updateProduct(Long productId, String title, BigDecimal price, String description, Long categoryId) {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return false;
		}

		Product product = db.getProductById(productId);
		if (product == null) {
			System.out.println("商品不存在！");
			return false;
		}

		// 检查权限
		if (!product.getOwnerId().equals(authService.getCurrentUser().getId())) {
			System.out.println("您没有权限编辑此商品！");
			return false;
		}

		// 更新信息
		if (title != null && !title.isEmpty()) {
			if (!ValidationUtil.isValidProductTitle(title)) {
				System.out.println("商品标题不合法！");
				return false;
			}
			product.setTitle(title);
		}

		if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
			product.setPrice(price);
		}

		if (description != null) {
			if (!ValidationUtil.isValidDescription(description)) {
				System.out.println("商品描述过长！");
				return false;
			}
			product.setDescription(description);
		}

		if (categoryId != null) {
			if (db.getCategoryById(categoryId) == null) {
				System.out.println("分类不存在！");
				return false;
			}
			product.setCategoryId(categoryId);
		}

		db.updateProduct(product);
		System.out.println("商品更新成功！");
		return true;
	}

	/**
	 * 删除商品
	 */
	public boolean deleteProduct(Long productId) {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return false;
		}

		Product product = db.getProductById(productId);
		if (product == null) {
			System.out.println("商品不存在！");
			return false;
		}

		// 检查权限
		if (!product.getOwnerId().equals(authService.getCurrentUser().getId())) {
			System.out.println("您没有权限删除此商品！");
			return false;
		}

		db.deleteProduct(productId);
		System.out.println("商品已删除！");
		return true;
	}

	/**
	 * 更改商品状态
	 */
	public boolean changeProductStatus(Long productId, String status) {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return false;
		}

		Product product = db.getProductById(productId);
		if (product == null) {
			System.out.println("商品不存在！");
			return false;
		}

		// 检查权限
		if (!product.getOwnerId().equals(authService.getCurrentUser().getId())) {
			System.out.println("您没有权限修改此商品状态！");
			return false;
		}

		product.setStatus(status);
		db.updateProduct(product);
		System.out.println("商品状态已更新为: " + status);
		return true;
	}

	/**
	 * 获取所有在售商品
	 */
	public List<Product> listPublishedProducts() {
		return db.getProductsByStatus("PUBLISHED");
	}

	/**
	 * 获取我的发布
	 */
	public List<Product> getMyProducts() {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return null;
		}
		return db.getProductsByOwnerId(authService.getCurrentUser().getId());
	}

	/**
	 * 获取商品详情
	 */
	public Product getProductById(Long productId) {
		Product product = db.getProductById(productId);

		// 记录浏览历史
		if (authService.isLoggedIn() && product != null) {
			BrowseHistory history = new BrowseHistory(
					db.generateHistoryId(),
					authService.getCurrentUser().getId(),
					productId,
					product.getTitle());
			db.addBrowseHistory(history);
		}

		return product;
	}

	/**
	 * 按分类筛选
	 */
	public List<Product> getProductsByCategory(Long categoryId) {
		return db.getProductsByStatus("PUBLISHED").stream()
				.filter(p -> p.getCategoryId().equals(categoryId))
				.collect(Collectors.toList());
	}

	/**
	 * 获取商品发布者
	 */
	public User getProductOwner(Long productId) {
		Product product = db.getProductById(productId);
		if (product == null) {
			return null;
		}
		return db.getUserById(product.getOwnerId());
	}
}
