package service;

import model.*;
import repository.Database;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {
	private Database db;
	private AuthService authService;

	public UserService(AuthService authService) {
		this.db = Database.getInstance();
		this.authService = authService;
	}

	/**
	 * 收藏商品
	 */
	public boolean addFavorite(Long productId) {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return false;
		}

		Product product = db.getProductById(productId);
		if (product == null) {
			System.out.println("商品不存在！");
			return false;
		}

		User currentUser = authService.getCurrentUser();

		// 检查是否已收藏
		if (db.getFavoriteByUserAndProduct(currentUser.getId(), productId) != null) {
			System.out.println("已经收藏过该商品！");
			return false;
		}

		Long favoriteId = db.generateFavoriteId();
		Favorite favorite = new Favorite(favoriteId, currentUser.getId(), productId);
		db.addFavorite(favorite);

		System.out.println("收藏成功！");
		return true;
	}

	/**
	 * 取消收藏
	 */
	public boolean removeFavorite(Long productId) {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return false;
		}

		User currentUser = authService.getCurrentUser();
		Favorite favorite = db.getFavoriteByUserAndProduct(currentUser.getId(), productId);

		if (favorite == null) {
			System.out.println("您还未收藏该商品！");
			return false;
		}

		db.deleteFavorite(favorite.getId());
		System.out.println("已取消收藏");
		return true;
	}

	/**
	 * 获取我的收藏列表
	 */
	public List<Product> getMyFavorites() {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return null;
		}

		User currentUser = authService.getCurrentUser();
		List<Favorite> favorites = db.getFavoritesByUserId(currentUser.getId());

		return favorites.stream()
				.map(f -> db.getProductById(f.getProductId()))
				.filter(p -> p != null)
				.collect(Collectors.toList());
	}

	/**
	 * 获取浏览历史
	 */
	public List<BrowseHistory> getBrowseHistory() {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return null;
		}

		User currentUser = authService.getCurrentUser();
		return db.getBrowseHistoriesByUserId(currentUser.getId());
	}

	/**
	 * 获取浏览历史对应的商品
	 */
	public List<Product> getBrowseHistoryProducts() {
		List<BrowseHistory> histories = getBrowseHistory();
		if (histories == null) {
			return null;
		}

		return histories.stream()
				.map(h -> db.getProductById(h.getProductId()))
				.filter(p -> p != null)
				.collect(Collectors.toList());
	}

	/**
	 * 获取用户信息
	 */
	public User getUserById(Long userId) {
		return db.getUserById(userId);
	}

	/**
	 * 查看个人资料
	 */
	public void displayUserProfile() {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return;
		}

		User user = authService.getCurrentUser();
		System.out.println("\n========== 个人资料 ==========");
		System.out.println("用户ID: " + user.getId());
		System.out.println("用户名: " + user.getUsername());
		System.out.println("邮箱: " + user.getEmail());
		System.out.println("手机: " + user.getPhone());
		System.out.println("信用分: " + user.getCreditScore());
		System.out.println("注册时间: " + user.getRegisteredAt());
		System.out.println("==============================\n");
	}
}
