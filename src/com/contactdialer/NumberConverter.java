package com.contactdialer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class NumberConverter {
	private String number = null;
	private String country = null;
	private String district = null;
	private String group = null;
	private String unit = null;
	private int type;
	private static final int TYPE_MOBILE = 0;
	private static final int TYPE_FIXED = 1;
	private static final int TYPE_RAW = 2;
	private int group_width;
	private UserModel.User user;
	private static final String CHINA="86";

	public NumberConverter(String s, UserModel.User user, NumberParserModel lt) {
		number = s.replaceAll("\\s", "");
		this.user=user;
		group_width = user.getGroupWidth();
		parse(lt);
	}

	public String getConverted() {
		String cNumber = unit;
		switch (type) {
		case TYPE_FIXED:
			cNumber = group + unit;
			if (!country.equals(user.getCountry())) {
				cNumber = "00" + country + district + cNumber;
			} else {
				if (!district.equals(user.getDistrict())) {
					cNumber = "0" + district + cNumber;
				} else {
					if (group.equals(user.getGroup())) {
						return user.getInGroup() + unit;
					}
				}
			}
			break;
		case TYPE_MOBILE:
			if (country.equals(user.getCountry())) {
				if (!district.equals(user.getDistrict())) {
					cNumber = user.getMobilePrefix() + cNumber;
				}
			} else {
				cNumber = "00" + user.getCountry() + cNumber;
			}
			break;
		case TYPE_RAW:
			cNumber = unit;
			if (!country.equals(user.getCountry())) {
				cNumber = "00" + country + cNumber;
			}
			break;
		default:
			return number;
		}
		cNumber = user.getOutGroup() + cNumber;
		return cNumber;
	}

	public String getOriginal() {
		return number;

	}

	@SuppressLint("DefaultLocale")
	private void parse(NumberParserModel lt) {
		// parse country
		Pattern pattern = Pattern.compile("^\\+|^00");
		String wcNumber = number; // number without country header
		Matcher matcher = pattern.matcher(number);
		boolean hasCountry=false;
		if (matcher.find()) {
			hasCountry=true;
			// the number has a country header
			// string without header
			String whString = number.substring(matcher.end());
			for (int width = 1; width < 5; width++) {
				pattern = Pattern.compile(String.format("\\d{%d}", width));
				matcher = pattern.matcher(whString);
				matcher.find();
				if (lt.isCountry(matcher.group())) {
					country = matcher.group();
					break;
				}
			}
			wcNumber = whString.substring(matcher.end());
		} else {
			
			///////this should be replaced with a variable item.
			///////
			country=CHINA;
		}

		// if it is in China parse type
		if (country.equals(CHINA)) {
			pattern = Pattern.compile("^1\\d{10}");
			matcher = pattern.matcher(wcNumber);
			if (matcher.find()) {
				type = TYPE_MOBILE;
				parseMobile(lt, wcNumber);
			} else {
				type = TYPE_FIXED;
				parseFixed(lt, wcNumber,hasCountry);
			}
		}else {
			type=TYPE_RAW;
			unit=wcNumber;
		}
	}

	@SuppressLint("DefaultLocale")
	private void parseFixed(NumberParserModel lt, String wcNumber,boolean hasCountry) {
		Pattern pattern;
		Matcher matcher;
		// number without district header
		String wdNumber = wcNumber;
		district=user.getDistrict();
		String districtHeader="0";
		if(hasCountry) {
			districtHeader="";
		}
		for (int width = 1; width < 4; width++) {
			pattern = Pattern.compile(String.format("%s(\\d{%d})", districtHeader,width));
			matcher = pattern.matcher(wcNumber);
			if (matcher.find() == false) {
				// there is no district header
				continue;
			}
			if (lt.isDistrict(matcher.group(1))) {
				district = matcher.group(1);
				wdNumber = wcNumber.substring(matcher.end());
				break;
			}
		}
		if (wdNumber.length() > group_width) {
			group = wdNumber.substring(0, group_width);
			unit = wdNumber.substring(group_width);
		} else {
			group="";
			unit = wcNumber;
		}
	}

	private void parseMobile(NumberParserModel lt, String wcNumber) {
		Pattern pattern = Pattern.compile("^\\d{7}");
		Matcher matcher = pattern.matcher(wcNumber);
		if (matcher.find()) {
			district = lt.district(matcher.group());
			if (district == null) {
				type = TYPE_RAW;
			}
			unit = wcNumber;
		}
	}
}
