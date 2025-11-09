package model;

import java.util.Date;

public class BrowseHistory {
	private Long id;
	private Long userId;
	private Long productId;
	private Date viewedAt;
	private String snapshotTitle; // 商品标题快照

	public BrowseHistory() {
		this.viewedAt = new Date();
	}

	public BrowseHistory(Long id, Long userId, Long productId, String snapshotTitle) {
		this();
		this.id = id;
		this.userId = userId;
		this.productId = productId;
		this.snapshotTitle = snapshotTitle;
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

	public Date getViewedAt() {
		return viewedAt;
	}

	public void setViewedAt(Date viewedAt) {
		this.viewedAt = viewedAt;
	}

	public String getSnapshotTitle() {
		return snapshotTitle;
	}

	public void setSnapshotTitle(String snapshotTitle) {
		this.snapshotTitle = snapshotTitle;
	}
}
