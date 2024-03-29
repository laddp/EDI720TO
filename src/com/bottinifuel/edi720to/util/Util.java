package com.bottinifuel.edi720to.util;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;


public class Util {

	public static final Abbreviation [] USstateAbbrevs = new Abbreviation[51];

	static
	{
		int stateNum = 0;
		USstateAbbrevs[stateNum++] = new Abbreviation("Alabama", "AL");
		USstateAbbrevs[stateNum++] = new Abbreviation("Alaska", "AK");
		USstateAbbrevs[stateNum++] = new Abbreviation("Arizona", "AZ");
		USstateAbbrevs[stateNum++] = new Abbreviation("Arkansas", "AR");
		USstateAbbrevs[stateNum++] = new Abbreviation("California", "CA");
		USstateAbbrevs[stateNum++] = new Abbreviation("Colorado", "CO");
		USstateAbbrevs[stateNum++] = new Abbreviation("Connecticut", "CT");
		USstateAbbrevs[stateNum++] = new Abbreviation("Delaware", "DE");
		USstateAbbrevs[stateNum++] = new Abbreviation("District of Columbia", "DC");
		USstateAbbrevs[stateNum++] = new Abbreviation("Florida", "FL");
		USstateAbbrevs[stateNum++] = new Abbreviation("Georgia", "GA");
		USstateAbbrevs[stateNum++] = new Abbreviation("Hawaii", "HI");
		USstateAbbrevs[stateNum++] = new Abbreviation("Idaho", "ID");
		USstateAbbrevs[stateNum++] = new Abbreviation("Illinois", "IL");
		USstateAbbrevs[stateNum++] = new Abbreviation("Indiana", "IN");
		USstateAbbrevs[stateNum++] = new Abbreviation("Iowa", "IA");
		USstateAbbrevs[stateNum++] = new Abbreviation("Kansas", "KS");
		USstateAbbrevs[stateNum++] = new Abbreviation("Kentucky", "KY");
		USstateAbbrevs[stateNum++] = new Abbreviation("Louisiana", "LA");
		USstateAbbrevs[stateNum++] = new Abbreviation("Maine", "ME");
		USstateAbbrevs[stateNum++] = new Abbreviation("Maryland", "MD");
		USstateAbbrevs[stateNum++] = new Abbreviation("Massachusetts", "MA");
		USstateAbbrevs[stateNum++] = new Abbreviation("Michigan", "MI");
		USstateAbbrevs[stateNum++] = new Abbreviation("Minnesota", "MN");
		USstateAbbrevs[stateNum++] = new Abbreviation("Mississippi", "MS");
		USstateAbbrevs[stateNum++] = new Abbreviation("Missouri", "MO");
		USstateAbbrevs[stateNum++] = new Abbreviation("Montana", "MT");
		USstateAbbrevs[stateNum++] = new Abbreviation("Nebraska", "NE");
		USstateAbbrevs[stateNum++] = new Abbreviation("Nevada", "NV");
		USstateAbbrevs[stateNum++] = new Abbreviation("New Hampshire", "NH");
		USstateAbbrevs[stateNum++] = new Abbreviation("New Jersey", "NJ");
		USstateAbbrevs[stateNum++] = new Abbreviation("New Mexico", "NM");
		USstateAbbrevs[stateNum++] = new Abbreviation("New York", "NY");
		USstateAbbrevs[stateNum++] = new Abbreviation("North Carolina", "NC");
		USstateAbbrevs[stateNum++] = new Abbreviation("North Dakota", "ND");
		USstateAbbrevs[stateNum++] = new Abbreviation("Ohio", "OH");
		USstateAbbrevs[stateNum++] = new Abbreviation("Oklahoma", "OK");
		USstateAbbrevs[stateNum++] = new Abbreviation("Oregon", "OR");
		USstateAbbrevs[stateNum++] = new Abbreviation("Pennsylvania", "PA");
		USstateAbbrevs[stateNum++] = new Abbreviation("Rhode Island", "RI");
		USstateAbbrevs[stateNum++] = new Abbreviation("South Carolina", "SC");
		USstateAbbrevs[stateNum++] = new Abbreviation("South Dakota", "SD");
		USstateAbbrevs[stateNum++] = new Abbreviation("Tennessee", "TN");
		USstateAbbrevs[stateNum++] = new Abbreviation("Texas", "TX");
		USstateAbbrevs[stateNum++] = new Abbreviation("Utah", "UT");
		USstateAbbrevs[stateNum++] = new Abbreviation("Vermont", "VT");
		USstateAbbrevs[stateNum++] = new Abbreviation("Virginia", "VA");
		USstateAbbrevs[stateNum++] = new Abbreviation("Washington", "WA");
		USstateAbbrevs[stateNum++] = new Abbreviation("West Virginia", "WV");
		USstateAbbrevs[stateNum++] = new Abbreviation("Wisconsin", "WI");
		USstateAbbrevs[stateNum++] = new Abbreviation("Wyoming", "WY");
	}

	
	public static final Abbreviation [] CanadianAbbrevs = new Abbreviation [13];
	
	static {
		int provinceNum = 0;
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Alberta", "AB");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("British Columbia", "BC");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Manitoba", "MB");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("New Brunswick", "NB");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Newfoundland and Labrador", "NL");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Nova Scotia", "NS");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Northwest Territories", "NT");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Nunavut", "NU");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Ontario", "ON");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Prince Edward Island", "PE");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Quebec", "QC");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Saskatchewan", "SK");
		CanadianAbbrevs[provinceNum++] = new Abbreviation("Yukon", "YT");
	}
	
	public static Abbreviation CountryAbbrevs[] = new Abbreviation[235];
	
	static {
		int countryNum = 0;
		CountryAbbrevs[countryNum++] = new Abbreviation("UNITED STATES", "USA");
		CountryAbbrevs[countryNum++] = new Abbreviation("ARUBA", "ABW");
		CountryAbbrevs[countryNum++] = new Abbreviation("AFGHANISTAN", "AFG");
		CountryAbbrevs[countryNum++] = new Abbreviation("ANGOLA", "AGO");
		CountryAbbrevs[countryNum++] = new Abbreviation("ANGUILLA", "AIA");
		CountryAbbrevs[countryNum++] = new Abbreviation("ALBANIA", "ALB");
		CountryAbbrevs[countryNum++] = new Abbreviation("ANDORRA", "AND");
		CountryAbbrevs[countryNum++] = new Abbreviation("NETHERLANDS ANTILLES", "ANT");
		CountryAbbrevs[countryNum++] = new Abbreviation("UNITED ARAB EMIRATES", "ARE");
		CountryAbbrevs[countryNum++] = new Abbreviation("ARGENTINA", "ARG");
		CountryAbbrevs[countryNum++] = new Abbreviation("ARMENIA", "ARM");
		CountryAbbrevs[countryNum++] = new Abbreviation("ANTARCTICA", "ATA");
		CountryAbbrevs[countryNum++] = new Abbreviation("FRENCH SOUTHERN TERRITORIES", "ATF");
		CountryAbbrevs[countryNum++] = new Abbreviation("ANTIGUA AND BARBUDA", "ATG");
		CountryAbbrevs[countryNum++] = new Abbreviation("AUSTRALIA", "AUS");
		CountryAbbrevs[countryNum++] = new Abbreviation("AUSTRIA", "AUT");
		CountryAbbrevs[countryNum++] = new Abbreviation("AZERBAIJAN", "AZE");
		CountryAbbrevs[countryNum++] = new Abbreviation("BURUNDI", "BDI");
		CountryAbbrevs[countryNum++] = new Abbreviation("BELGIUM", "BEL");
		CountryAbbrevs[countryNum++] = new Abbreviation("BENIN", "BEN");
		CountryAbbrevs[countryNum++] = new Abbreviation("BURKINA FASO", "BFA");
		CountryAbbrevs[countryNum++] = new Abbreviation("BANGLADESH", "BGD");
		CountryAbbrevs[countryNum++] = new Abbreviation("BULGARIA", "BGR");
		CountryAbbrevs[countryNum++] = new Abbreviation("BAHRAIN", "BHR");
		CountryAbbrevs[countryNum++] = new Abbreviation("BAHAMAS", "BHS");
		CountryAbbrevs[countryNum++] = new Abbreviation("BOSNIA AND HERZEGOWINA", "BIH");
		CountryAbbrevs[countryNum++] = new Abbreviation("BELARUS", "BLR");
		CountryAbbrevs[countryNum++] = new Abbreviation("BELIZE", "BLZ");
		CountryAbbrevs[countryNum++] = new Abbreviation("BERMUDA", "BMU");
		CountryAbbrevs[countryNum++] = new Abbreviation("BOLIVIA", "BOL");
		CountryAbbrevs[countryNum++] = new Abbreviation("BRAZIL", "BRA");
		CountryAbbrevs[countryNum++] = new Abbreviation("BARBADOS", "BRB");
		CountryAbbrevs[countryNum++] = new Abbreviation("BRUNEI DARUSSALAM", "BRN");
		CountryAbbrevs[countryNum++] = new Abbreviation("BHUTAN", "BTN");
		CountryAbbrevs[countryNum++] = new Abbreviation("BOUVET ISLAND", "BVT");
		CountryAbbrevs[countryNum++] = new Abbreviation("BOTSWANA", "BWA");
		CountryAbbrevs[countryNum++] = new Abbreviation("CENTRAL AFRICAN REPUBLIC", "CAF");
		CountryAbbrevs[countryNum++] = new Abbreviation("CANADA", "CAN");
		CountryAbbrevs[countryNum++] = new Abbreviation("COCOS (KEELING) ISLANDS", "CCK");
		CountryAbbrevs[countryNum++] = new Abbreviation("SWITZERLAND", "CHE");
		CountryAbbrevs[countryNum++] = new Abbreviation("CHILE", "CHL");
		CountryAbbrevs[countryNum++] = new Abbreviation("CHINA", "CHN");
		CountryAbbrevs[countryNum++] = new Abbreviation("COTE D�IVOIRE", "CIV");
		CountryAbbrevs[countryNum++] = new Abbreviation("CAMEROON", "CMR");
		CountryAbbrevs[countryNum++] = new Abbreviation("CONGO", "COG");
		CountryAbbrevs[countryNum++] = new Abbreviation("COOK ISLANDS", "COK");
		CountryAbbrevs[countryNum++] = new Abbreviation("COLOMBIA", "COL");
		CountryAbbrevs[countryNum++] = new Abbreviation("COMOROS", "COM");
		CountryAbbrevs[countryNum++] = new Abbreviation("CAPE VERDE", "CPV");
		CountryAbbrevs[countryNum++] = new Abbreviation("COSTA RICA", "CRI");
		CountryAbbrevs[countryNum++] = new Abbreviation("CUBA", "CUB");
		CountryAbbrevs[countryNum++] = new Abbreviation("CHRISTMAS ISLAND", "CXR");
		CountryAbbrevs[countryNum++] = new Abbreviation("CAYMAN ISLANDS", "CYM");
		CountryAbbrevs[countryNum++] = new Abbreviation("CYPRUS", "CYP");
		CountryAbbrevs[countryNum++] = new Abbreviation("CZECH REPUBLIC", "CZE");
		CountryAbbrevs[countryNum++] = new Abbreviation("GERMANY", "DEU");
		CountryAbbrevs[countryNum++] = new Abbreviation("DJIBOUTI", "DJI");
		CountryAbbrevs[countryNum++] = new Abbreviation("DOMINICA", "DMA");
		CountryAbbrevs[countryNum++] = new Abbreviation("DENMARK", "DNK");
		CountryAbbrevs[countryNum++] = new Abbreviation("DOMINICAN REPUBLIC", "DOM");
		CountryAbbrevs[countryNum++] = new Abbreviation("ALGERIA", "DZA");
		CountryAbbrevs[countryNum++] = new Abbreviation("ECUADOR", "ECU");
		CountryAbbrevs[countryNum++] = new Abbreviation("EGYPT", "EGY");
		CountryAbbrevs[countryNum++] = new Abbreviation("ERITREA", "ERI");
		CountryAbbrevs[countryNum++] = new Abbreviation("WESTERN SAHARA", "ESH");
		CountryAbbrevs[countryNum++] = new Abbreviation("SPAIN", "ESP");
		CountryAbbrevs[countryNum++] = new Abbreviation("ESTONIA", "EST");
		CountryAbbrevs[countryNum++] = new Abbreviation("ETHIOPIA", "ETH");
		CountryAbbrevs[countryNum++] = new Abbreviation("FINLAND", "FIN");
		CountryAbbrevs[countryNum++] = new Abbreviation("FIJI", "FJI");
		CountryAbbrevs[countryNum++] = new Abbreviation("FALKLAND ISLANDS (MALVINAS)", "FLK");
		CountryAbbrevs[countryNum++] = new Abbreviation("FRANCE", "FRA");
		CountryAbbrevs[countryNum++] = new Abbreviation("FAROE ISLANDS", "FRO");
		CountryAbbrevs[countryNum++] = new Abbreviation("MICRONESIA, FEDERATED STATES OF", "FSM");
		CountryAbbrevs[countryNum++] = new Abbreviation("FRANCE, METROPOLITAN", "FXX");
		CountryAbbrevs[countryNum++] = new Abbreviation("GABON", "GAB");
		CountryAbbrevs[countryNum++] = new Abbreviation("UNITED KINGDOM", "GBR");
		CountryAbbrevs[countryNum++] = new Abbreviation("GEORGIA", "GEO");
		CountryAbbrevs[countryNum++] = new Abbreviation("GHANA", "GHA");
		CountryAbbrevs[countryNum++] = new Abbreviation("GIBRALTAR", "GIB");
		CountryAbbrevs[countryNum++] = new Abbreviation("GUINEA", "GIN");
		CountryAbbrevs[countryNum++] = new Abbreviation("GUADELOUPE", "GLP");
		CountryAbbrevs[countryNum++] = new Abbreviation("GAMBIA", "GMB");
		CountryAbbrevs[countryNum++] = new Abbreviation("GUINEA-BISSAU", "GNB");
		CountryAbbrevs[countryNum++] = new Abbreviation("EQUATORIAL GUINEA", "GNQ");
		CountryAbbrevs[countryNum++] = new Abbreviation("GREECE", "GRC");
		CountryAbbrevs[countryNum++] = new Abbreviation("GRENADA", "GRD");
		CountryAbbrevs[countryNum++] = new Abbreviation("GREENLAND", "GRL");
		CountryAbbrevs[countryNum++] = new Abbreviation("GUATEMALA", "GTM");
		CountryAbbrevs[countryNum++] = new Abbreviation("FRENCH GUIANA", "GUF");
		CountryAbbrevs[countryNum++] = new Abbreviation("GUYANA", "GUY");
		CountryAbbrevs[countryNum++] = new Abbreviation("HONG KONG", "HKG");
		CountryAbbrevs[countryNum++] = new Abbreviation("HEARD AND MC DONALD ISLANDS", "HMD");
		CountryAbbrevs[countryNum++] = new Abbreviation("HONDURAS", "HND");
		CountryAbbrevs[countryNum++] = new Abbreviation("CROATIA (local name: Hrvatska)", "HRV");
		CountryAbbrevs[countryNum++] = new Abbreviation("HAITI", "HTI");
		CountryAbbrevs[countryNum++] = new Abbreviation("HUNGARY", "HUN");
		CountryAbbrevs[countryNum++] = new Abbreviation("INDONESIA", "IDN");
		CountryAbbrevs[countryNum++] = new Abbreviation("INDIA", "IND");
		CountryAbbrevs[countryNum++] = new Abbreviation("BRITISH INDIAN OCEAN TERRITORY", "IOT");
		CountryAbbrevs[countryNum++] = new Abbreviation("IRELAND", "IRL");
		CountryAbbrevs[countryNum++] = new Abbreviation("IRAN (ISLAMIC REPUBLIC OF)", "IRN");
		CountryAbbrevs[countryNum++] = new Abbreviation("IRAQ", "IRQ");
		CountryAbbrevs[countryNum++] = new Abbreviation("ICELAND", "ISL");
		CountryAbbrevs[countryNum++] = new Abbreviation("ISRAEL", "ISR");
		CountryAbbrevs[countryNum++] = new Abbreviation("ITALY", "ITA");
		CountryAbbrevs[countryNum++] = new Abbreviation("JAMAICA", "JAM");
		CountryAbbrevs[countryNum++] = new Abbreviation("JORDAN", "JOR");
		CountryAbbrevs[countryNum++] = new Abbreviation("JAPAN", "JPN");
		CountryAbbrevs[countryNum++] = new Abbreviation("KAZAKHSTAN", "KAZ");
		CountryAbbrevs[countryNum++] = new Abbreviation("KENYA", "KEN");
		CountryAbbrevs[countryNum++] = new Abbreviation("KYRGYZSTAN", "KGZ");
		CountryAbbrevs[countryNum++] = new Abbreviation("CAMBODIA", "KHM");
		CountryAbbrevs[countryNum++] = new Abbreviation("KIRIBATI", "KIR");
		CountryAbbrevs[countryNum++] = new Abbreviation("SAINT KITTS AND NEVIS", "KNA");
		CountryAbbrevs[countryNum++] = new Abbreviation("KOREA, REPUBLIC OF", "KOR");
		CountryAbbrevs[countryNum++] = new Abbreviation("KUWAIT", "KWT");
		CountryAbbrevs[countryNum++] = new Abbreviation("LAO PEOPLE�S DEMOCRATIC REPUBLIC", "LAO");
		CountryAbbrevs[countryNum++] = new Abbreviation("LEBANON", "LBN");
		CountryAbbrevs[countryNum++] = new Abbreviation("LIBERIA", "LBR");
		CountryAbbrevs[countryNum++] = new Abbreviation("LIBYAN ARAB JAMAHIRIYA", "LBY");
		CountryAbbrevs[countryNum++] = new Abbreviation("SAINT LUCIA", "LCA");
		CountryAbbrevs[countryNum++] = new Abbreviation("LIECHTENSTEIN", "LIE");
		CountryAbbrevs[countryNum++] = new Abbreviation("SRI LANKA", "LKA");
		CountryAbbrevs[countryNum++] = new Abbreviation("LESOTHO", "LSO");
		CountryAbbrevs[countryNum++] = new Abbreviation("LITHUANIA", "LTU");
		CountryAbbrevs[countryNum++] = new Abbreviation("LUXEMBOURG", "LUX");
		CountryAbbrevs[countryNum++] = new Abbreviation("LATVIA", "LVA");
		CountryAbbrevs[countryNum++] = new Abbreviation("MACAU", "MAC");
		CountryAbbrevs[countryNum++] = new Abbreviation("MOROCCO", "MAR");
		CountryAbbrevs[countryNum++] = new Abbreviation("MONACO", "MCO");
		CountryAbbrevs[countryNum++] = new Abbreviation("MOLDOVA, REPUBLIC OF", "MDA");
		CountryAbbrevs[countryNum++] = new Abbreviation("MADAGASCAR", "MDG");
		CountryAbbrevs[countryNum++] = new Abbreviation("MALDIVES", "MDV");
		CountryAbbrevs[countryNum++] = new Abbreviation("MEXICO", "MEX");
		CountryAbbrevs[countryNum++] = new Abbreviation("MARSHALL ISLANDS", "MHL");
		CountryAbbrevs[countryNum++] = new Abbreviation("MACEDONIA, THE FORMER YUGOSLAV REPUBLIC", "MKD");
		CountryAbbrevs[countryNum++] = new Abbreviation("MALI", "MLI");
		CountryAbbrevs[countryNum++] = new Abbreviation("MALTA", "MLT");
		CountryAbbrevs[countryNum++] = new Abbreviation("MYANMAR", "MMR");
		CountryAbbrevs[countryNum++] = new Abbreviation("MONGOLIA", "MNG");
		CountryAbbrevs[countryNum++] = new Abbreviation("MOZAMBIQUE", "MOZ");
		CountryAbbrevs[countryNum++] = new Abbreviation("MAURITANIA", "MRT");
		CountryAbbrevs[countryNum++] = new Abbreviation("MONTSERRAT", "MSR");
		CountryAbbrevs[countryNum++] = new Abbreviation("MARTINIQUE", "MTQ");
		CountryAbbrevs[countryNum++] = new Abbreviation("MAURITIUS", "MUS");
		CountryAbbrevs[countryNum++] = new Abbreviation("MALAWI", "MWI");
		CountryAbbrevs[countryNum++] = new Abbreviation("MALAYSIA", "MYS");
		CountryAbbrevs[countryNum++] = new Abbreviation("MAYOTTE", "MYT");
		CountryAbbrevs[countryNum++] = new Abbreviation("NAMIBIA", "NAM");
		CountryAbbrevs[countryNum++] = new Abbreviation("NEW CALEDONIA", "NCL");
		CountryAbbrevs[countryNum++] = new Abbreviation("NIGER", "NER");
		CountryAbbrevs[countryNum++] = new Abbreviation("NORFOLK ISLAND", "NFK");
		CountryAbbrevs[countryNum++] = new Abbreviation("NIGERIA", "NGA");
		CountryAbbrevs[countryNum++] = new Abbreviation("NICARAGUA", "NIC");
		CountryAbbrevs[countryNum++] = new Abbreviation("NIUE", "NIU");
		CountryAbbrevs[countryNum++] = new Abbreviation("NETHERLANDS", "NLD");
		CountryAbbrevs[countryNum++] = new Abbreviation("NORWAY", "NOR");
		CountryAbbrevs[countryNum++] = new Abbreviation("NEPAL", "NPL");
		CountryAbbrevs[countryNum++] = new Abbreviation("NAURU", "NRU");
		CountryAbbrevs[countryNum++] = new Abbreviation("NEW ZEALAND", "NZL");
		CountryAbbrevs[countryNum++] = new Abbreviation("OMAN", "OMN");
		CountryAbbrevs[countryNum++] = new Abbreviation("PAKISTAN", "PAK");
		CountryAbbrevs[countryNum++] = new Abbreviation("PANAMA", "PAN");
		CountryAbbrevs[countryNum++] = new Abbreviation("PITCAIRN", "PCN");
		CountryAbbrevs[countryNum++] = new Abbreviation("PERU", "PER");
		CountryAbbrevs[countryNum++] = new Abbreviation("PHILIPPINES", "PHL");
		CountryAbbrevs[countryNum++] = new Abbreviation("PALAU", "PLW");
		CountryAbbrevs[countryNum++] = new Abbreviation("PAPUA NEW GUINEA", "PNG");
		CountryAbbrevs[countryNum++] = new Abbreviation("POLAND", "POL");
		CountryAbbrevs[countryNum++] = new Abbreviation("PUERTO RICO", "PRI");
		CountryAbbrevs[countryNum++] = new Abbreviation("KOREA, DEMOCRATIC PEOPLE'S REPUBLIC", "PRK");
		CountryAbbrevs[countryNum++] = new Abbreviation("PORTUGAL", "PRT");
		CountryAbbrevs[countryNum++] = new Abbreviation("PARAGUAY", "PRY");
		CountryAbbrevs[countryNum++] = new Abbreviation("FRENCH POLYNESIA", "PYF");
		CountryAbbrevs[countryNum++] = new Abbreviation("QATAR", "QAT");
		CountryAbbrevs[countryNum++] = new Abbreviation("REUNION", "REU");
		CountryAbbrevs[countryNum++] = new Abbreviation("ROMANIA", "ROM");
		CountryAbbrevs[countryNum++] = new Abbreviation("RUSSIAN FEDERATION", "RUS");
		CountryAbbrevs[countryNum++] = new Abbreviation("RWANDA", "RWA");
		CountryAbbrevs[countryNum++] = new Abbreviation("SAUDI ARABIA", "SAU");
		CountryAbbrevs[countryNum++] = new Abbreviation("SUDAN", "SDN");
		CountryAbbrevs[countryNum++] = new Abbreviation("SENEGAL", "SEN");
		CountryAbbrevs[countryNum++] = new Abbreviation("SINGAPORE", "SGP");
		CountryAbbrevs[countryNum++] = new Abbreviation("SOUTH GEORGIA AND THE SOUTH SANDWICH", "SGS");
		CountryAbbrevs[countryNum++] = new Abbreviation("ST. HELENA", "SHN");
		CountryAbbrevs[countryNum++] = new Abbreviation("SVALBARD AND JAN MAYEN ISLANDS", "SJM");
		CountryAbbrevs[countryNum++] = new Abbreviation("SOLOMON ISLANDS", "SLB");
		CountryAbbrevs[countryNum++] = new Abbreviation("SIERRA LEONE", "SLE");
		CountryAbbrevs[countryNum++] = new Abbreviation("EL SALVADOR", "SLV");
		CountryAbbrevs[countryNum++] = new Abbreviation("SAN MARINO", "SMR");
		CountryAbbrevs[countryNum++] = new Abbreviation("SOMALIA", "SOM");
		CountryAbbrevs[countryNum++] = new Abbreviation("ST. PIERRE AND MIQUELON", "SPM");
		CountryAbbrevs[countryNum++] = new Abbreviation("SAO TOME AND PRINCIPE", "STP");
		CountryAbbrevs[countryNum++] = new Abbreviation("SURINAME", "SUR");
		CountryAbbrevs[countryNum++] = new Abbreviation("SLOVAKIA (Slovak Republic)", "SVK");
		CountryAbbrevs[countryNum++] = new Abbreviation("SLOVENIA", "SVN");
		CountryAbbrevs[countryNum++] = new Abbreviation("SWEDEN", "SWE");
		CountryAbbrevs[countryNum++] = new Abbreviation("SWAZILAND", "SWZ");
		CountryAbbrevs[countryNum++] = new Abbreviation("SEYCHELLES", "SYC");
		CountryAbbrevs[countryNum++] = new Abbreviation("SYRIAN ARAB REPUBLIC", "SYR");
		CountryAbbrevs[countryNum++] = new Abbreviation("TURKS AND CAICOS ISLANDS", "TCA");
		CountryAbbrevs[countryNum++] = new Abbreviation("CHAD", "TCD");
		CountryAbbrevs[countryNum++] = new Abbreviation("TOGO", "TGO");
		CountryAbbrevs[countryNum++] = new Abbreviation("THAILAND", "THA");
		CountryAbbrevs[countryNum++] = new Abbreviation("TAJIKISTAN", "TJK");
		CountryAbbrevs[countryNum++] = new Abbreviation("TOKELAU", "TKL");
		CountryAbbrevs[countryNum++] = new Abbreviation("TURKMENISTAN", "TKM");
		CountryAbbrevs[countryNum++] = new Abbreviation("EAST TIMOR", "TMP");
		CountryAbbrevs[countryNum++] = new Abbreviation("TONGA", "TON");
		CountryAbbrevs[countryNum++] = new Abbreviation("TRINIDAD AND TOBAGO", "TTO");
		CountryAbbrevs[countryNum++] = new Abbreviation("TUNISIA", "TUN");
		CountryAbbrevs[countryNum++] = new Abbreviation("TURKEY", "TUR");
		CountryAbbrevs[countryNum++] = new Abbreviation("TUVALU", "TUV");
		CountryAbbrevs[countryNum++] = new Abbreviation("TAIWAN, PROVINCE OF CHINA", "TWN");
		CountryAbbrevs[countryNum++] = new Abbreviation("TANZANIA, UNITED REPUBLIC OF", "TZA");
		CountryAbbrevs[countryNum++] = new Abbreviation("UGANDA", "UGA");
		CountryAbbrevs[countryNum++] = new Abbreviation("UKRAINE", "UKR");
		CountryAbbrevs[countryNum++] = new Abbreviation("UNITED STATES MINOR OUTLYING ISLANDS", "UMI");
		CountryAbbrevs[countryNum++] = new Abbreviation("URUGUAY", "URY");
		CountryAbbrevs[countryNum++] = new Abbreviation("UZBEKISTAN", "UZB");
		CountryAbbrevs[countryNum++] = new Abbreviation("HOLY SEE (VATICAN CITY STATE)", "VAT");
		CountryAbbrevs[countryNum++] = new Abbreviation("SAINT VINCENT AND THE GRENADINES", "VCT");
		CountryAbbrevs[countryNum++] = new Abbreviation("VENEZUELA", "VEN");
		CountryAbbrevs[countryNum++] = new Abbreviation("VIRGIN ISLANDS (BRITISH)", "VGB");
		CountryAbbrevs[countryNum++] = new Abbreviation("VIET NAM", "VNM");
		CountryAbbrevs[countryNum++] = new Abbreviation("VANUATU", "VUT");
		CountryAbbrevs[countryNum++] = new Abbreviation("WALLIS AND FUTUNA ISLANDS", "WLF");
		CountryAbbrevs[countryNum++] = new Abbreviation("SAMOA", "WSM");
		CountryAbbrevs[countryNum++] = new Abbreviation("YEMEN", "YEM");
		CountryAbbrevs[countryNum++] = new Abbreviation("YUGOSLAVIA", "YUG");
		CountryAbbrevs[countryNum++] = new Abbreviation("SOUTH AFRICA", "ZAF");
		CountryAbbrevs[countryNum++] = new Abbreviation("ZAIRE", "ZAR");
		CountryAbbrevs[countryNum++] = new Abbreviation("ZAMBIA", "ZMB");
		CountryAbbrevs[countryNum++] = new Abbreviation("ZIMBABWE", "ZWE");
	}
	
	public static MaskFormatter EINFormatter;
	public static MaskFormatter TelephoneFormatter;
	static {
		try {
			EINFormatter = new MaskFormatter("##-#######");
			TelephoneFormatter = new MaskFormatter("###-###-####");
			} catch (ParseException e) {}
	}
}