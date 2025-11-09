package model;

import java.util.Date;

public class Favorite {
	private Long id;
	private Long userId;
	private Long productId;
	private Date favoritedAt;

	public Favorite() {
		this.favoritedAt = new Date();
	}

	public Favorite(Long id, Long userId, Long productId) {
		this();
		this.id = id;
		this.userId = userId;
		this.productId = productId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Date getFavoritedAt() {
		return favoritedAt;
	}

	public void setFavoritedAt(Date favoritedAt) {
		this.favoritedAt = favoritedAt;
	}
}
