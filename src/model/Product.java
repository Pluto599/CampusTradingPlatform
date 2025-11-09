package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {
	private Long id;
	private String title;
	private BigDecimal price;
	private String description;
	private List<String> imageUrls;
	private String status; // "PUBLISHED", "OFFLINE", "SOLD"
	private Date createdAt;
	private Date updatedAt;
	private Long ownerId; // 发布者ID
	private Long categoryId;
	private List<Long> tagIds;

	public Product() {
		this.imageUrls = new ArrayList<>();
		this.tagIds = new ArrayList<>();
		this.status = "PUBLISHED";
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	public Product(Long id, String title, BigDecimal price, String description, Long ownerId, Long categoryId) {
		this();
		this.id = id;
		this.title = title;
		this.price = price;
		this.description = description;
		this.ownerId = ownerId;
		this.categoryId = categoryId;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public void addImageUrl(String url) {
		this.imageUrls.add(url);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.updatedAt = new Date();
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public List<Long> getTagIds() {
		return tagIds;
	}

	public void setTagIds(List<Long> tagIds) {
		this.tagIds = tagIds;
	}

	public void addTagId(Long tagId) {
		this.tagIds.add(tagId);
	}

	@Override
	public String toString() {
		return "商品[" + title + "] ¥" + price + " (" + status + ")";
	}
}
