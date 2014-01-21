package com.contactdialer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NumberConverter {
	private String number = null;
	private String country = null;
	private String district = null;
	private String group = null;
	private String unit = null;
	private int type;
	private static final int TYPE_MOBILE = 0;
	private static final int TYPE_FIXED = 1;
	private static final int TYPE_RAW = 1;
	private int group_width;

	public NumberConverter(String s, User user, LookupTable lt) {
		number = s;
		group_width=user.getGroupWidth();
		parse(lt);
	}

	public String getConverted() {
		return number;
	}
	public String getOriginal() {
		return number;
		
	}

	private void parse(LookupTable lt) {
		// parse country
		Pattern pattern = Pattern.compile("^\\+|00");
		String wcNumber = number; // number without country header
		Matcher matcher = pattern.matcher(number);
		if (matcher.find()) {
			// the number has a country header
			// string without header
			String whString = number.substring(matcher.end()); 
			for (int width=1;width<10;width++) {
				pattern=Pattern.compile(String.format("\\d{%d}", width));
				matcher=pattern.matcher(whString);
				matcher.find();
				if (lt.isCountry(matcher.group())){
					country=matcher.group();
					break;
				}
			}
			wcNumber=whString.substring(matcher.end());
		}
		
		//parse type
		pattern=Pattern.compile("^1\\d{10}");
		matcher=pattern.matcher(wcNumber);
		if(matcher.find()) {
			type=TYPE_MOBILE;
			parseMobile(lt,wcNumber);
		}
		else {
			type=TYPE_FIXED;
			if (country != null) {
				wcNumber="0"+wcNumber;	
			}
			parseFixed(lt,wcNumber);
		}
	}

	private void parseFixed(LookupTable lt, String wcNumber) {
		Pattern pattern;
		Matcher matcher;
		String wdNumber=wcNumber;
		for (int width=1;width<10;width++) {
			pattern=Pattern.compile(String.format("0(\\d{%d})", width));
			matcher=pattern.matcher(wcNumber);
			if(matcher.find()==false) {
				//there is no district header
				continue;
			}
			if (lt.isDistrict(matcher.group(1))){
				district=matcher.group(1);
				wdNumber=wcNumber.substring(matcher.end());
				break;
			}
		}
		if(wcNumber.length()>group_width) {
			group=wcNumber.substring(0, group_width);
			unit=wcNumber.substring(group_width);
		}
		unit=wcNumber;
		
	}

	private void parseMobile(LookupTable lt, String wcNumber) {
		Pattern pattern = Pattern.compile("^\\d{7}");
		Matcher matcher = pattern.matcher(wcNumber);
		if(matcher.find()){
			district=lt.district(matcher.group());
			if (district==null){
				type=TYPE_RAW;
			}
			unit=wcNumber;
		}
	}
}
