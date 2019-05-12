package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
 
@Entity
@Table(name="HOSPITAL")
public class Hospital extends BaseEntity {
	
	@Id
	@Column(name ="HOSPITAL_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	@Column(name ="META_KEYWORD")
	private String metaKeyword;
	@Column(name ="META_DESCRIPTION")
	private String metaDescription;
	private String address;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "PHONE_NUMBER")
	private String phone;
	private String logo;
	private String favicon;
	@Column(name = "BACKGROUND_SLIDER")
	private String backgroundSlider;
	@Column(name = "BANNER_CONTENT_HEADER")
	private String bannerContentHeader;
	@Column(name = "BANNER_CONTENT_PARAGRAPH")
	private String bannerContentParagraph;
	private String copyright;
	@Column(name = "TWITTER_API")
	private String twitterApi;
	@Column(name = "GOOGLE_MAP")
	private String googleMap;
	@Column(name = "FACEBOOK_URL")
	private String facebookUrl;
	@Column(name = "TWITTER_URL")
	private String twitterUrl;
	@Column(name = "INSTAGRAM_URL")
	private String instagramUrl;
	@Column(name = "GOOGLE_PLUS_URL")
	private String googlePlusUrl;
	@Column(name = "DRIBBBLE_URL")
	private String dribbbleUrl;
	@Column(name = "SKYPE_URL")
	private String skypeUrl;
	@Column(name = "VIMEO_URL")
	private String vimeoUrl;
	private int status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMetaKeyword() {
		return metaKeyword;
	}
	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getFavicon() {
		return favicon;
	}
	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}
	public String getBackgroundSlider() {
		return backgroundSlider;
	}
	public void setBackgroundSlider(String backgroundSlider) {
		this.backgroundSlider = backgroundSlider;
	}
	public String getBannerContentHeader() {
		return bannerContentHeader;
	}
	public void setBannerContentHeader(String bannerContentHeader) {
		this.bannerContentHeader = bannerContentHeader;
	}
	public String getBannerContentParagraph() {
		return bannerContentParagraph;
	}
	public void setBannerContentParagraph(String bannerContentParagraph) {
		this.bannerContentParagraph = bannerContentParagraph;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public String getTwitterApi() {
		return twitterApi;
	}
	public void setTwitterApi(String twitterApi) {
		this.twitterApi = twitterApi;
	}
	public String getGoogleMap() {
		return googleMap;
	}
	public void setGoogleMap(String googleMap) {
		this.googleMap = googleMap;
	}
	public String getFacebookUrl() {
		return facebookUrl;
	}
	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}
	public String getTwitterUrl() {
		return twitterUrl;
	}
	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}
	public String getInstagramUrl() {
		return instagramUrl;
	}
	public void setInstagramUrl(String instagramUrl) {
		this.instagramUrl = instagramUrl;
	}
	public String getGooglePlusUrl() {
		return googlePlusUrl;
	}
	public void setGooglePlusUrl(String googlePlusUrl) {
		this.googlePlusUrl = googlePlusUrl;
	}
	public String getDribbbleUrl() {
		return dribbbleUrl;
	}
	public void setDribbbleUrl(String dribbbleUrl) {
		this.dribbbleUrl = dribbbleUrl;
	}
	public String getSkypeUrl() {
		return skypeUrl;
	}
	public void setSkypeUrl(String skypeUrl) {
		this.skypeUrl = skypeUrl;
	}
	public String getVimeoUrl() {
		return vimeoUrl;
	}
	public void setVimeoUrl(String vimeoUrl) {
		this.vimeoUrl = vimeoUrl;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
