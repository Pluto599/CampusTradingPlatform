import service.*;
import model.*;
import repository.Database;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class CampusMarketApp {
	private Scanner scanner;
	private AuthService authService;
	private ProductService productService;
	private SearchService searchService;
	private ChatService chatService;
	private UserService userService;
	private Database db;

	public CampusMarketApp() {
		// 使用 UTF-8 确保控制台输入输出不乱码
		try {
			System.setOut(
					new java.io.PrintStream(new java.io.FileOutputStream(java.io.FileDescriptor.out), true, "UTF-8"));
			System.setErr(
					new java.io.PrintStream(new java.io.FileOutputStream(java.io.FileDescriptor.err), true, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// 忽略
		}

		scanner = new java.util.Scanner(System.in, "UTF-8");
		authService = new AuthService();
		productService = new ProductService(authService);
		searchService = new SearchService();
		chatService = new ChatService(authService);
		userService = new UserService(authService);
		db = Database.getInstance();
	}

	public void start() {
		System.out.println("╔══════════════════════════════╗");
		System.out.println("║   欢迎使用校园二手交易平台   ║");
		System.out.println("╚══════════════════════════════╝");

		while (true) {
			if (!authService.isLoggedIn()) {
				showGuestMenu();
			} else {
				showUserMenu();
			}
		}
	}

	/**
	 * 游客菜单
	 */
	private void showGuestMenu() {
		System.out.println("\n========== 主菜单 ==========");
		System.out.println("1. 注册");
		System.out.println("2. 登录");
		System.out.println("3. 浏览商品");
		System.out.println("4. 搜索商品");
		System.out.println("5. 查看商品详情");
		System.out.println("0. 退出系统");
		System.out.println("============================");
		System.out.print("请选择: ");

		String choice = scanner.nextLine().trim();

		switch (choice) {
			case "1":
				handleRegister();
				break;
			case "2":
				handleLogin();
				break;
			case "3":
				handleBrowseProducts();
				break;
			case "4":
				handleSearchProducts();
				break;
			case "5":
				handleViewProductDetail();
				break;
			case "0":
				System.out.println("感谢使用，再见！");
				System.exit(0);
				break;
			default:
				System.out.println("无效选择，请重新输入");
		}
	}

	/**
	 * 已登录用户菜单
	 */
	private void showUserMenu() {
		System.out.println("\n========== 用户中心 ==========");
		System.out.println("当前用户: " + authService.getCurrentUser().getUsername());
		System.out.println("1. 浏览商品");
		System.out.println("2. 搜索商品");
		System.out.println("3. 查看商品详情");
		System.out.println("4. 发布商品");
		System.out.println("5. 我的发布");
		System.out.println("6. 我的收藏");
		System.out.println("7. 浏览历史");
		System.out.println("8. 站内消息");
		System.out.println("9. 个人资料");
		System.out.println("0. 退出登录");
		System.out.println("============================");
		System.out.print("请选择: ");

		String choice = scanner.nextLine().trim();

		switch (choice) {
			case "1":
				handleBrowseProducts();
				break;
			case "2":
				handleSearchProducts();
				break;
			case "3":
				handleViewProductDetail();
				break;
			case "4":
				handlePublishProduct();
				break;
			case "5":
				handleMyProducts();
				break;
			case "6":
				handleMyFavorites();
				break;
			case "7":
				handleBrowseHistory();
				break;
			case "8":
				handleMessages();
				break;
			case "9":
				handleUserProfile();
				break;
			case "0":
				authService.logout();
				break;
			default:
				System.out.println("无效选择，请重新输入");
		}
	}

	/**
	 * 处理用户注册
	 */
	private void handleRegister() {
		System.out.println("\n========== 用户注册 ==========");
		System.out.print("用户名 (3-20位字母数字下划线): ");
		String username = scanner.nextLine().trim();

		System.out.print("邮箱: ");
		String email = scanner.nextLine().trim();

		System.out.print("手机号: ");
		String phone = scanner.nextLine().trim();

		System.out.print("密码 (至少6位): ");
		String password = scanner.nextLine().trim();

		System.out.print("确认密码: ");
		String confirmPassword = scanner.nextLine().trim();

		if (!password.equals(confirmPassword)) {
			System.out.println("两次密码输入不一致！");
			return;
		}

		authService.registerUser(username, email, phone, password);
	}

	/**
	 * 处理用户登录
	 */
	private void handleLogin() {
		System.out.println("\n========== 用户登录 ==========");
		System.out.print("用户名: ");
		String username = scanner.nextLine().trim();

		System.out.print("密码: ");
		String password = scanner.nextLine().trim();

		authService.login(username, password);
	}

	/**
	 * 浏览商品列表
	 */
	private void handleBrowseProducts() {
		System.out.println("\n========== 商品列表 ==========");

		// 显示分类筛选
		System.out.println("\n可选分类:");
		List<Category> categories = db.getAllCategories();
		System.out.println("0. 全部分类");
		for (int i = 0; i < categories.size(); i++) {
			System.out.println((i + 1) + ". " + categories.get(i).getName());
		}

		System.out.print("\n选择分类 (直接回车查看全部): ");
		String categoryChoice = scanner.nextLine().trim();

		List<Product> products;
		if (categoryChoice.isEmpty() || categoryChoice.equals("0")) {
			products = productService.listPublishedProducts();
		} else {
			try {
				int index = Integer.parseInt(categoryChoice) - 1;
				if (index >= 0 && index < categories.size()) {
					Long categoryId = categories.get(index).getId();
					products = productService.getProductsByCategory(categoryId);
				} else {
					products = productService.listPublishedProducts();
				}
			} catch (NumberFormatException e) {
				products = productService.listPublishedProducts();
			}
		}

		if (products.isEmpty()) {
			System.out.println("暂无商品");
			return;
		}

		displayProductList(products);
	}

	/**
	 * 搜索商品
	 */
	private void handleSearchProducts() {
		System.out.println("\n========== 搜索商品 ==========");
		System.out.print("输入搜索关键词: ");
		String query = scanner.nextLine().trim();

		List<Product> products = searchService.search(query);

		if (products.isEmpty()) {
			System.out.println("未找到相关商品");
			return;
		}

		System.out.println("\n找到 " + products.size() + " 个商品:");
		displayProductList(products);
	}

	/**
	 * 显示商品列表
	 */
	private void displayProductList(List<Product> products) {
		System.out.println("\n----------------------------------------");
		for (int i = 0; i < products.size(); i++) {
			Product p = products.get(i);
			Category category = db.getCategoryById(p.getCategoryId());
			User owner = db.getUserById(p.getOwnerId());

			System.out.println((i + 1) + ". [" + p.getId() + "] " + p.getTitle());
			System.out.println("   价格: ¥" + p.getPrice() + " | 分类: " + category.getName());
			System.out.println("   卖家: " + owner.getUsername() + " | 状态: " + p.getStatus());
			System.out.println("   描述: " +
					(p.getDescription().length() > 50 ? p.getDescription().substring(0, 50) + "..."
							: p.getDescription()));
			System.out.println("----------------------------------------");
		}
	}

	/**
	 * 查看商品详情
	 */
	private void handleViewProductDetail() {
		System.out.print("\n请输入商品ID: ");
		String idStr = scanner.nextLine().trim();

		try {
			Long productId = Long.parseLong(idStr);
			Product product = productService.getProductById(productId);

			if (product == null) {
				System.out.println("商品不存在！");
				return;
			}

			displayProductDetail(product);

			// 如果已登录，显示操作菜单
			if (authService.isLoggedIn()) {
				showProductActions(product);
			}

		} catch (NumberFormatException e) {
			System.out.println("无效的商品ID！");
		}
	}

	/**
	 * 显示商品详情
	 */
	private void displayProductDetail(Product product) {
		Category category = db.getCategoryById(product.getCategoryId());
		User owner = db.getUserById(product.getOwnerId());

		System.out.println("\n========== 商品详情 ==========");
		System.out.println("商品ID: " + product.getId());
		System.out.println("标题: " + product.getTitle());
		System.out.println("价格: ¥" + product.getPrice());
		System.out.println("分类: " + category.getName());
		System.out.println("状态: " + product.getStatus());
		System.out.println("描述: " + product.getDescription());
		System.out.println("发布时间: " + product.getCreatedAt());
		System.out.println("卖家: " + owner.getUsername() + " (ID: " + owner.getId() + ")");
		System.out.println("卖家信用分: " + owner.getCreditScore());
		System.out.println("卖家联系方式: " + owner.getPhone());
		System.out.println("============================");
	}

	/**
	 * 商品操作菜单
	 */
	private void showProductActions(Product product) {
		User currentUser = authService.getCurrentUser();
		boolean isOwner = product.getOwnerId().equals(currentUser.getId());

		if (isOwner) {
			// 商品所有者的操作
			System.out.println("\n商品管理:");
			System.out.println("1. 编辑商品");
			System.out.println("2. 下架商品");
			System.out.println("3. 删除商品");
			System.out.println("0. 返回");
			System.out.print("请选择: ");

			String choice = scanner.nextLine().trim();
			switch (choice) {
				case "1":
					handleEditProduct(product.getId());
					break;
				case "2":
					productService.changeProductStatus(product.getId(), "OFFLINE");
					break;
				case "3":
					productService.deleteProduct(product.getId());
					break;
			}
		} else {
			// 其他用户的操作
			System.out.println("\n可用操作:");
			System.out.println("1. 收藏商品");
			System.out.println("2. 联系卖家");
			System.out.println("0. 返回");
			System.out.print("请选择: ");

			String choice = scanner.nextLine().trim();
			switch (choice) {
				case "1":
					userService.addFavorite(product.getId());
					break;
				case "2":
					handleContactSeller(product.getOwnerId());
					break;
			}
		}
	}

	/**
	 * 发布商品
	 */
	private void handlePublishProduct() {
		System.out.println("\n========== 发布商品 ==========");

		System.out.print("商品标题: ");
		String title = scanner.nextLine().trim();

		System.out.print("价格: ");
		String priceStr = scanner.nextLine().trim();

		System.out.print("描述: ");
		String description = scanner.nextLine().trim();

		// 选择分类
		List<Category> categories = db.getAllCategories();
		System.out.println("\n选择分类:");
		for (int i = 0; i < categories.size(); i++) {
			System.out.println((i + 1) + ". " + categories.get(i).getName());
		}
		System.out.print("请选择 (1-" + categories.size() + "): ");
		String categoryChoice = scanner.nextLine().trim();

		try {
			BigDecimal price = new BigDecimal(priceStr);
			int categoryIndex = Integer.parseInt(categoryChoice) - 1;

			if (categoryIndex >= 0 && categoryIndex < categories.size()) {
				Long categoryId = categories.get(categoryIndex).getId();
				productService.createProduct(title, price, description, categoryId);
			} else {
				System.out.println("无效的分类选择！");
			}
		} catch (NumberFormatException e) {
			System.out.println("输入格式错误！");
		}
	}

	/**
	 * 编辑商品
	 */
	private void handleEditProduct(Long productId) {
		System.out.println("\n========== 编辑商品 ==========");
		System.out.println("(直接回车跳过不修改的项)");

		System.out.print("新标题: ");
		String title = scanner.nextLine().trim();

		System.out.print("新价格: ");
		String priceStr = scanner.nextLine().trim();

		System.out.print("新描述: ");
		String description = scanner.nextLine().trim();

		BigDecimal price = null;
		if (!priceStr.isEmpty()) {
			try {
				price = new BigDecimal(priceStr);
			} catch (NumberFormatException e) {
				System.out.println("价格格式错误！");
				return;
			}
		}

		productService.updateProduct(productId,
				title.isEmpty() ? null : title,
				price,
				description.isEmpty() ? null : description,
				null);
	}

	/**
	 * 我的发布
	 */
	private void handleMyProducts() {
		System.out.println("\n========== 我的发布 ==========");
		List<Product> products = productService.getMyProducts();

		if (products == null || products.isEmpty()) {
			System.out.println("您还没有发布任何商品");
			return;
		}

		displayProductList(products);
	}

	/**
	 * 我的收藏
	 */
	private void handleMyFavorites() {
		System.out.println("\n========== 我的收藏 ==========");
		List<Product> products = userService.getMyFavorites();

		if (products == null || products.isEmpty()) {
			System.out.println("您还没有收藏任何商品");
			return;
		}

		displayProductList(products);

		System.out.print("\n是否取消收藏某个商品？(输入商品ID，直接回车返回): ");
		String idStr = scanner.nextLine().trim();
		if (!idStr.isEmpty()) {
			try {
				Long productId = Long.parseLong(idStr);
				userService.removeFavorite(productId);
			} catch (NumberFormatException e) {
				System.out.println("无效的商品ID！");
			}
		}
	}

	/**
	 * 浏览历史
	 */
	private void handleBrowseHistory() {
		System.out.println("\n========== 浏览历史 ==========");
		List<BrowseHistory> histories = userService.getBrowseHistory();

		if (histories == null || histories.isEmpty()) {
			System.out.println("暂无浏览历史");
			return;
		}

		for (int i = 0; i < Math.min(histories.size(), 20); i++) {
			BrowseHistory h = histories.get(i);
			Product p = db.getProductById(h.getProductId());
			if (p != null) {
				System.out.println((i + 1) + ". [" + p.getId() + "] " + h.getSnapshotTitle() +
						" (浏览于: " + h.getViewedAt() + ")");
			}
		}
	}

	/**
	 * 站内消息
	 */
	private void handleMessages() {
		System.out.println("\n========== 站内消息 ==========");
		List<Conversation> conversations = chatService.getMyConversations();

		if (conversations == null || conversations.isEmpty()) {
			System.out.println("暂无会话");
			return;
		}

		System.out.println("我的会话列表:");
		for (int i = 0; i < conversations.size(); i++) {
			Conversation conv = conversations.get(i);
			Long otherUserId = conv.getOtherUserId(authService.getCurrentUser().getId());
			User otherUser = db.getUserById(otherUserId);
			System.out.println((i + 1) + ". 与 " + otherUser.getUsername() + " 的会话 (ID: " + conv.getId() + ")");
		}

		System.out.print("\n选择会话查看 (输入序号): ");
		String choice = scanner.nextLine().trim();

		try {
			int index = Integer.parseInt(choice) - 1;
			if (index >= 0 && index < conversations.size()) {
				handleConversation(conversations.get(index));
			}
		} catch (NumberFormatException e) {
			System.out.println("无效输入！");
		}
	}

	/**
	 * 处理会话
	 */
	private void handleConversation(Conversation conversation) {
		Long otherUserId = conversation.getOtherUserId(authService.getCurrentUser().getId());
		User otherUser = db.getUserById(otherUserId);

		while (true) {
			System.out.println("\n========== 与 " + otherUser.getUsername() + " 的对话 ==========");

			List<Message> messages = chatService.getConversationHistory(conversation.getId());
			if (messages != null && !messages.isEmpty()) {
				for (Message msg : messages) {
					User sender = db.getUserById(msg.getSenderId());
					String prefix = msg.getSenderId().equals(authService.getCurrentUser().getId()) ? "我"
							: sender.getUsername();
					System.out.println("[" + msg.getSentAt() + "] " + prefix + ": " + msg.getContent());
				}
			} else {
				System.out.println("暂无消息");
			}

			System.out.println("\n1. 发送消息");
			System.out.println("0. 返回");
			System.out.print("请选择: ");
			String choice = scanner.nextLine().trim();

			if (choice.equals("1")) {
				System.out.print("输入消息内容: ");
				String content = scanner.nextLine().trim();
				chatService.sendMessage(conversation.getId(), content);
			} else {
				break;
			}
		}
	}

	/**
	 * 联系卖家
	 */
	private void handleContactSeller(Long sellerId) {
		Conversation conversation = chatService.getOrCreateConversation(sellerId);
		if (conversation != null) {
			handleConversation(conversation);
		}
	}

	/**
	 * 个人资料
	 */
	private void handleUserProfile() {
		userService.displayUserProfile();

		System.out.println("1. 编辑资料");
		System.out.println("0. 返回");
		System.out.print("请选择: ");
		String choice = scanner.nextLine().trim();

		if (choice.equals("1")) {
			handleEditProfile();
		}
	}

	/**
	 * 编辑个人资料
	 */
	private void handleEditProfile() {
		System.out.println("\n========== 编辑资料 ==========");
		System.out.println("(直接回车跳过不修改的项)");

		System.out.print("新邮箱: ");
		String email = scanner.nextLine().trim();

		System.out.print("新手机号: ");
		String phone = scanner.nextLine().trim();

		authService.editProfile(
				email.isEmpty() ? null : email,
				phone.isEmpty() ? null : phone);
	}

	public static void main(String[] args) {
		CampusMarketApp app = new CampusMarketApp();
		app.start();
	}
}
