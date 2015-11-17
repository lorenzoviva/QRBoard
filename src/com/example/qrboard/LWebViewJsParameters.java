package com.example.qrboard;

public class LWebViewJsParameters {
	private boolean followOnClick;
	private boolean excludeImages;
	private boolean executeOnClick;
	private boolean followId;
	private boolean followExternalLinks;
	private boolean openOnNewWindow;
	public LWebViewJsParameters(boolean followOnClick, boolean excludeImages, boolean executeOnClick, boolean followId, boolean followExternalLinks,boolean openOnNewWindow) {
		this.followOnClick = followOnClick;
		this.excludeImages = excludeImages;
		this.executeOnClick = executeOnClick;
		this.followId = followId;
		this.followExternalLinks = followExternalLinks;
		this.openOnNewWindow = openOnNewWindow;
	}
	public boolean isFollowOnClick() {
		return followOnClick;
	}
	public void setFollowOnClick(boolean followOnClick) {
		this.followOnClick = followOnClick;
	}
	public boolean isExcludeImages() {
		return excludeImages;
	}
	public void setExcludeImages(boolean excludeImages) {
		this.excludeImages = excludeImages;
	}
	public boolean isExecuteOnClick() {
		return executeOnClick;
	}
	public void setExecuteOnClick(boolean executeOnClick) {
		this.executeOnClick = executeOnClick;
	}
	public boolean isFollowId() {
		return followId;
	}
	public void setFollowId(boolean followId) {
		this.followId = followId;
	}
	public boolean isFollowExternalLinks() {
		return followExternalLinks;
	}
	public void setFollowExternalLinks(boolean followExternalLinks) {
		this.followExternalLinks = followExternalLinks;
	}
	public boolean isOpenOnNewWindow() {
		return openOnNewWindow;
	}
	public void setOpenOnNewWindow(boolean openOnNewWindow) {
		this.openOnNewWindow = openOnNewWindow;
	}
	
}
