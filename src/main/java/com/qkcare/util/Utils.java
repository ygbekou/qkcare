package com.qkcare.util;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date; 
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
import org.springframework.stereotype.Component; 

@Component
public class Utils {

	public static Map<String, String> configMap;
	private static final String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
 
	public static String getConfig(String name) {
		return configMap.get(name);
	}

	protected static ClassLoader getCurrentClassLoader(Object defaultObject) {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (loader == null) {
			loader = defaultObject.getClass().getClassLoader();
		}

		return loader;
	}

	public static String getMessageResourceString(String bundleName, String key, Object params[], Locale locale) {

		String text = null;

		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentClassLoader(params));

		try {
			text = bundle.getString(key);
		} catch (MissingResourceException e) {
			text = "?? key " + key + " not found ??";
		}

		if (params != null) {
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}

		return text;
	}

	public static String getRandomString(int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}

	public static int getAge(Date birthDate) {
		Calendar now = Calendar.getInstance();
		Calendar dob = Calendar.getInstance();
		dob.setTime(birthDate);
		if (dob.after(now)) {
			throw new IllegalArgumentException("Can't be born in the future");
		}
		int year1 = now.get(Calendar.YEAR);
		int year2 = dob.get(Calendar.YEAR);
		int age = year1 - year2;
		int month1 = now.get(Calendar.MONTH);
		int month2 = dob.get(Calendar.MONTH);
		if (month2 > month1) {
			age--;
		} else if (month1 == month2) {
			int day1 = now.get(Calendar.DAY_OF_MONTH);
			int day2 = dob.get(Calendar.DAY_OF_MONTH);
			if (day2 > day1) {
				age--;
			}
		}

		return age;
	}

	public static String trimString(String string, int length, boolean soft) {
		if (string == null || string.trim().isEmpty()) {
			return string;
		}

		StringBuffer sb = new StringBuffer(string);
		int actualLength = length - 3;
		if (sb.length() > actualLength) {
			// -3 because we add 3 dots at the end. Returned string length has
			// to be length including the dots.
			if (!soft)
				return escapeHtml(sb.insert(actualLength, "...").substring(0, actualLength + 3));
			else {
				int endIndex = sb.indexOf(" ", actualLength);
				return escapeHtml(sb.insert(endIndex, "...").substring(0, endIndex + 3));
			}
		}
		return string;
	}

	public static String truncateString(String string, int length) {
		if (string == null || string.trim().isEmpty()) {
			return string;
		}
		StringBuffer sb = new StringBuffer(string);
		if (sb.length() > length) {
			int endIndex = sb.indexOf(" ", length);
			return escapeHtml(sb.insert(endIndex, "").substring(0, endIndex));
		}
		return string;
	}

	public static String truncateHTML(String text, int length, String suffix) {
		// if the plain text is shorter than the maximum length, return the
		// whole text
		if (text.replaceAll("<.*?>", "").length() <= length) {
			return text;
		}
		String result = "";
		boolean trimmed = false;
		if (suffix == null) {
			suffix = "...";
		}

		/*
		 * This pattern creates tokens, where each line starts with the tag. For
		 * example, "One, <b>Two</b>, Three" produces the following: One, <b>Two
		 * </b>, Three
		 */
		Pattern tagPattern = Pattern.compile("(<.+?>)?([^<>]*)");

		/*
		 * Checks for an empty tag, for example img, br, etc.
		 */
		Pattern emptyTagPattern = Pattern
				.compile("^<\\s*(img|br|input|hr|area|base|basefont|col|frame|isindex|link|meta|param).*>$");

		/*
		 * Checks for closing tags, allowing leading and ending space inside the
		 * brackets
		 */
		Pattern closingTagPattern = Pattern.compile("^<\\s*/\\s*([a-zA-Z]+)\\s*>$");

		/*
		 * Checks for opening tags, allowing leading and ending space inside the
		 * brackets
		 */
		Pattern openingTagPattern = Pattern.compile("^<\\s*([a-zA-Z]+).*?>$");

		/*
		 * Find &nbsp; &gt; ...
		 */
		Pattern entityPattern = Pattern.compile("(&[0-9a-z]{2,8};|&#[0-9]{1,7};|[0-9a-f]{1,6};)");

		// splits all html-tags to scanable lines
		Matcher tagMatcher = tagPattern.matcher(text);
		int numTags = tagMatcher.groupCount();

		int totalLength = suffix.length();
		List<String> openTags = new ArrayList<String>();

		boolean proposingChop = false;
		while (tagMatcher.find()) {
			String tagText = tagMatcher.group(1);
			String plainText = tagMatcher.group(2);

			if (proposingChop && tagText != null && tagText.length() != 0 && plainText != null
					&& plainText.length() != 0) {
				trimmed = true;
				break;
			}

			// if there is any html-tag in this line, handle it and add it
			// (uncounted) to the output
			if (tagText != null && tagText.length() > 0) {
				boolean foundMatch = false;

				// if it's an "empty element" with or without xhtml-conform
				// closing slash
				Matcher matcher = emptyTagPattern.matcher(tagText);
				if (matcher.find()) {
					foundMatch = true;
					// do nothing
				}

				// closing tag?
				if (!foundMatch) {
					matcher = closingTagPattern.matcher(tagText);
					if (matcher.find()) {
						foundMatch = true;
						// delete tag from openTags list
						String tagName = matcher.group(1);
						openTags.remove(tagName.toLowerCase());
					}
				}

				// opening tag?
				if (!foundMatch) {
					matcher = openingTagPattern.matcher(tagText);
					if (matcher.find()) {
						// add tag to the beginning of openTags list
						String tagName = matcher.group(1);
						openTags.add(0, tagName.toLowerCase());
					}
				}

				// add html-tag to result
				result += tagText;
			}

			// calculate the length of the plain text part of the line; handle
			// entities (e.g. &nbsp;) as one character
			int contentLength = plainText.replaceAll("&[0-9a-z]{2,8};|&#[0-9]{1,7};|[0-9a-f]{1,6};", " ").length();
			if (totalLength + contentLength > length) {
				// the number of characters which are left
				int numCharsRemaining = length - totalLength;
				int entitiesLength = 0;
				Matcher entityMatcher = entityPattern.matcher(plainText);
				while (entityMatcher.find()) {
					String entity = entityMatcher.group(1);
					if (numCharsRemaining > 0) {
						numCharsRemaining--;
						entitiesLength += entity.length();
					} else {
						// no more characters left
						break;
					}
				}

				// keep us from chopping words in half
				int proposedChopPosition = numCharsRemaining + entitiesLength;
				int endOfWordPosition = plainText.indexOf(" ", proposedChopPosition - 1);
				if (endOfWordPosition == -1) {
					endOfWordPosition = plainText.length();
				}
				int endOfWordOffset = endOfWordPosition - proposedChopPosition;
				if (endOfWordOffset > 6) { // chop the word if it's extra long
					endOfWordOffset = 0;
				}

				proposedChopPosition = numCharsRemaining + entitiesLength + endOfWordOffset;
				if (plainText.length() >= proposedChopPosition) {
					result += plainText.substring(0, proposedChopPosition);
					proposingChop = true;
					if (proposedChopPosition < plainText.length()) {
						trimmed = true;
						break; // maximum length is reached, so get off the loop
					}
				} else {
					result += plainText;
				}
			} else {
				result += plainText;
				totalLength += contentLength;
			}
			// if the maximum length is reached, get off the loop
			if (totalLength >= length) {
				trimmed = true;
				break;
			}
		}

		for (String openTag : openTags) {
			result += "</" + openTag + ">";
		}
		/*
		 * if (trimmed) { result += suffix; }
		 */
		return result;
	}
	
	public static String getStrValue(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	
	public static String getNullStrValue(Object obj) {
		return obj == null ? null : obj.toString();
	}
	
	public static Long getLongValue(Object obj) {
		return obj == null ? null : new Long(obj.toString());
	}
	public static Double getDoubleValue(Object obj) {
		return obj == null ? null : new Double(obj.toString());
	}
	
	public static Integer getIntegerValue(Object obj) {
		return obj == null ? null : new Integer(obj.toString());
	}
}
