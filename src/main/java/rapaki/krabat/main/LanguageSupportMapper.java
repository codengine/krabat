package rapaki.krabat.main;

public class LanguageSupportMapper {

	private static final LanguageSupportData[] supportedLanguages = new LanguageSupportData[] {
		new LanguageSupportData("Hornjoserbsce", "hs", "stringtable_de.txt", 1, false), // German is default additional language
		new LanguageSupportData("Dolnoserbski", "ds", "stringtable_de.txt", 2, false), // German is default additional language
		new LanguageSupportData("Deutsch", "de", "stringtable_de.txt", 3, false),
		new LanguageSupportData("English", "en", "stringtable_de.txt", 4, true), // fake
		new LanguageSupportData("#Kesky", "cz", "stringtable_de.txt", 5, true), // fake
		new LanguageSupportData("Polski", "pl", "stringtable_de.txt", 6, true), // fake
		new LanguageSupportData("Francais", "fr", "stringtable_de.txt", 7, true), // fake
		new LanguageSupportData("Espanol", "es", "stringtable_de.txt", 8, true), // fake
		new LanguageSupportData("Nederlands", "nl", "stringtable_de.txt", 9, true), // fake
		new LanguageSupportData("Hrvatski", "hr", "stringtable_de.txt", 10, true), // fake
		new LanguageSupportData("Sloven#s#kina", "sl", "stringtable_de.txt", 11, true), // fake
		new LanguageSupportData("Slovensky", "sk", "stringtable_de.txt", 12, true), // fake
		new LanguageSupportData("Magyar", "hu", "stringtable_de.txt", 13, true), // fake
		// add new languages here...
	};
	
	public static int getNumberSupportedLanguages() {
		return supportedLanguages.length;
	}
	
	public static String getLanguageName(int index) {
		return supportedLanguages[index].getName();
	}
	
	public static String getLanguageAbbreviation(int index) {
		return supportedLanguages[index].getAbbreviation();
	}
	
	public static String getLanguageFilename(String abbreviation) {

		// default if no language selected yet
		if (abbreviation == null) {
			return "stringtable_de.txt";
		}
		

		for (LanguageSupportData data : supportedLanguages) {
			if (data.getAbbreviation().equalsIgnoreCase(abbreviation)) {
				return data.getFilename();
			}
		}
		
		// always have a reasonable default
		return "stringtable_de.txt";
	}
	
	public static boolean getFakeLanguage(String abbreviation) {

		// default if no language selected yet
		if (abbreviation == null) {
			return true;
		}
		

		for (LanguageSupportData data : supportedLanguages) {
			if (data.getAbbreviation().equalsIgnoreCase(abbreviation)) {
				return data.isFake();
			}
		}
		
		return true;
	}
	
	public static int getInternalCode(String abbreviation) {
		
		// no code - language chooser to be shown
		if (abbreviation == null) {
			return 0;
		}
		
		for (LanguageSupportData data : supportedLanguages) {
			if (data.getAbbreviation().equalsIgnoreCase(abbreviation)) {
				return data.getInternalCode();
			}
		}
		
		// invalid/unsupported code - language chooser to be shown
		return 0;
	}

	public static class LanguageSupportData {
		
		private final String name;
		
		private final String abbreviation;
		
		private final String filename;
		
		private final int internalCode;
		
		private final boolean fake;
		
		public LanguageSupportData(String name, String abbreviation, String filename, int internalCode, boolean fake) {
			this.name = name;
			this.abbreviation = abbreviation;
			this.filename = filename;
			this.internalCode = internalCode;
			this.fake = fake;
		}
		
		public String getName() {
			return name;
		}
		
		public String getAbbreviation() {
			return abbreviation;
		}
		
		public String getFilename() {
			return filename;
		}
		
		public int getInternalCode() {
			return internalCode;
		}
		
		public boolean isFake() {
			return fake;
		}
	}
}
