package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
 
@Entity
@Table(name="COMPANY")
public class Company extends BaseEntity {
	
	@Id
	@Column(name ="COMPANY_ID")
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
	@Column(name = "PHONE")
	private String phone;
	private String phone2;
	private String fax;
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
	@Column(name = "FOOTER_PARAGRAPH1")
	private String footerParagraph1;
	@Column(name = "FOOTER_PARAGRAPH2")
	private String footerParagraph2;
	@Column(name = "FOOTER_PARAGRAPH3")
	private String footerParagraph3;
	private String language;
	@Column(name = "FROM_EMAIL")
	private String fromEmail;
	@Column(name = "TO_EMAIL")
	private String toEmail;
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
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
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
	public String getFooterParagraph1() {
		return footerParagraph1;
	}
	public void setFooterParagraph1(String footerParagraph1) {
		this.footerParagraph1 = footerParagraph1;
	}
	public String getFooterParagraph2() {
		return footerParagraph2;
	}
	public void setFooterParagraph2(String footerParagraph2) {
		this.footerParagraph2 = footerParagraph2;
	}
	public String getFooterParagraph3() {
		return footerParagraph3;
	}
	public void setFooterParagraph3(String footerParagraph3) {
		this.footerParagraph3 = footerParagraph3;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
}
