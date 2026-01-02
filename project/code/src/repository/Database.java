package repository;

import model.*;
import java.util.*;
import java.util.stream.Collectors;

public class Database {
	// 单例模式
	private static Database instance;

	// 内存数据存储
	private Map<Long, User> users;
	private Map<Long, Product> products;
	private Map<Long, Category> categories;
	private Map<Long, Tag> tags;
	private Map<Long, Favorite> favorites;
	private Map<Long, BrowseHistory> browseHistories;
	private Map<Long, Conversation> conversations;
	private Map<Long, Message> messages;

	// ID生成器
	private Long userIdCounter = 1L;
	private Long productIdCounter = 1L;
	private Long categoryIdCounter = 1L;
	private Long tagIdCounter = 1L;
	private Long favoriteIdCounter = 1L;
	private Long historyIdCounter = 1L;
	private Long conversationIdCounter = 1L;
	private Long messageIdCounter = 1L;

	private Database() {
		users = new HashMap<>();
		products = new HashMap<>();
		categories = new HashMap<>();
		tags = new HashMap<>();
		favorites = new HashMap<>();
		browseHistories = new HashMap<>();
		conversations = new HashMap<>();
		messages = new HashMap<>();
		initDefaultData();
	}

	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	// 初始化默认数据
	private void initDefaultData() {
		// 添加默认分类
		addCategory(new Category(categoryIdCounter++, "电子产品"));
		addCategory(new Category(categoryIdCounter++, "书籍教材"));
		addCategory(new Category(categoryIdCounter++, "生活用品"));
		addCategory(new Category(categoryIdCounter++, "运动器材"));
		addCategory(new Category(categoryIdCounter++, "其他"));

		// 添加默认标签
		addTag(new Tag(tagIdCounter++, "九成新"));
		addTag(new Tag(tagIdCounter++, "全新"));
		addTag(new Tag(tagIdCounter++, "急售"));
		addTag(new Tag(tagIdCounter++, "可议价"));
		addTag(new Tag(tagIdCounter++, "包邮"));
	}

	// User CRUD
	public Long generateUserId() {
		return userIdCounter++;
	}

	public void addUser(User user) {
		users.put(user.getId(), user);
	}

	public User getUserById(Long id) {
		return users.get(id);
	}

	public User getUserByUsername(String username) {
		return users.values().stream()
				.filter(u -> u.getUsername().equals(username))
				.findFirst()
				.orElse(null);
	}

	public User getUserByEmail(String email) {
		return users.values().stream()
				.filter(u -> u.getEmail().equals(email))
				.findFirst()
				.orElse(null);
	}

	public List<User> getAllUsers() {
		return new ArrayList<>(users.values());
	}

	public void updateUser(User user) {
		users.put(user.getId(), user);
	}

	// Product CRUD
	public Long generateProductId() {
		return productIdCounter++;
	}

	public void addProduct(Product product) {
		products.put(product.getId(), product);
	}

	public Product getProductById(Long id) {
		return products.get(id);
	}

	public List<Product> getAllProducts() {
		return new ArrayList<>(products.values());
	}

	public List<Product> getProductsByOwnerId(Long ownerId) {
		return products.values().stream()
				.filter(p -> p.getOwnerId().equals(ownerId))
				.collect(Collectors.toList());
	}

	public List<Product> getProductsByStatus(String status) {
		return products.values().stream()
				.filter(p -> p.getStatus().equals(status))
				.collect(Collectors.toList());
	}

	public void updateProduct(Product product) {
		products.put(product.getId(), product);
	}

	public void deleteProduct(Long id) {
		products.remove(id);
	}

	// Category CRUD
	public Long generateCategoryId() {
		return categoryIdCounter++;
	}

	public void addCategory(Category category) {
		categories.put(category.getId(), category);
	}

	public Category getCategoryById(Long id) {
		return categories.get(id);
	}

	public List<Category> getAllCategories() {
		return new ArrayList<>(categories.values());
	}

	// Tag CRUD
	public Long generateTagId() {
		return tagIdCounter++;
	}

	public void addTag(Tag tag) {
		tags.put(tag.getId(), tag);
	}

	public Tag getTagById(Long id) {
		return tags.get(id);
	}

	public List<Tag> getAllTags() {
		return new ArrayList<>(tags.values());
	}

	// Favorite CRUD
	public Long generateFavoriteId() {
		return favoriteIdCounter++;
	}

	public void addFavorite(Favorite favorite) {
		favorites.put(favorite.getId(), favorite);
	}

	public void deleteFavorite(Long id) {
		favorites.remove(id);
	}

	public List<Favorite> getFavoritesByUserId(Long userId) {
		return favorites.values().stream()
				.filter(f -> f.getUserId().equals(userId))
				.collect(Collectors.toList());
	}

	public Favorite getFavoriteByUserAndProduct(Long userId, Long productId) {
		return favorites.values().stream()
				.filter(f -> f.getUserId().equals(userId) && f.getProductId().equals(productId))
				.findFirst()
				.orElse(null);
	}

	// BrowseHistory CRUD
	public Long generateHistoryId() {
		return historyIdCounter++;
	}

	public void addBrowseHistory(BrowseHistory history) {
		browseHistories.put(history.getId(), history);
	}

	public List<BrowseHistory> getBrowseHistoriesByUserId(Long userId) {
		return browseHistories.values().stream()
				.filter(h -> h.getUserId().equals(userId))
				.sorted((h1, h2) -> h2.getViewedAt().compareTo(h1.getViewedAt()))
				.collect(Collectors.toList());
	}

	// Conversation CRUD
	public Long generateConversationId() {
		return conversationIdCounter++;
	}

	public void addConversation(Conversation conversation) {
		conversations.put(conversation.getId(), conversation);
	}

	public Conversation getConversationById(Long id) {
		return conversations.get(id);
	}

	public List<Conversation> getConversationsByUserId(Long userId) {
		return conversations.values().stream()
				.filter(c -> c.isParticipant(userId))
				.collect(Collectors.toList());
	}

	public Conversation getConversationBetweenUsers(Long user1Id, Long user2Id) {
		return conversations.values().stream()
				.filter(c -> c.isParticipant(user1Id) && c.isParticipant(user2Id))
				.findFirst()
				.orElse(null);
	}

	public void updateConversation(Conversation conversation) {
		conversations.put(conversation.getId(), conversation);
	}

	// Message CRUD
	public Long generateMessageId() {
		return messageIdCounter++;
	}

	public void addMessage(Message message) {
		messages.put(message.getId(), message);
	}

	public List<Message> getMessagesByConversationId(Long conversationId) {
		return messages.values().stream()
				.filter(m -> m.getConversationId().equals(conversationId))
				.sorted(Comparator.comparing(Message::getSentAt))
				.collect(Collectors.toList());
	}

	public void updateMessage(Message message) {
		messages.put(message.getId(), message);
	}
}
