package lizenz;

import java.math.BigInteger;
import java.util.ArrayList;

public class EnCrypt {

	// Hilfsmethoden
	private static int binToInt(String string) throws NumberFormatException {
		int zahl = 0;
		for (int i = 0; i < 8; i++) {
			zahl += (Integer.parseInt(string.substring(i, i + 1)) * Math.pow(2,
					7 - i));
		}
		return zahl;
	}

	// Verschlüsselungmethoden
	public static String caesar(String originalText, int k) {
		// alles in Großbuchstaben
		originalText = originalText.toUpperCase();

		// Initialisierung des Geheimtextes
		String geheimText = "";

		// Aufbau des neuen Strings
		for (int i = 0; i < originalText.length(); i++) {
			// nacheinander wird ein Zeichen aus dem Oroginaltext gelesen
			char zeichen = originalText.charAt(i);

			// Leerzeichen herausfiltern
			if (zeichen == ' ')
				;
			// eigentliches Verschlüsseln
			else
				geheimText = geheimText + (char) ((zeichen + k - 65) % 26 + 65);
		}
		return geheimText;
	}

	public static String vigenere(String originalText, String schluessel) {
		originalText = originalText.toUpperCase();
		schluessel = schluessel.toUpperCase();
		String geheimText = "";
		for (int i = 0, j = 0; i < originalText.length(); i++, j++) {
			// i ist der Zähler für den Originaltext
			// j ist der Zähler für das Schlüsselwort
			if (j == schluessel.length())
				j = 0; // wenn Schlüsselwort durchlaufen
			geheimText += (char) ((originalText.charAt(i)
					+ schluessel.charAt(j) - 130) % 26 + 65);
		}
		return geheimText;
	}

	public static String xor(String originalText, String schluessel) {
		String geheimText = "";
		for (int i = 0; i < originalText.length(); i++) {
			geheimText += (char) (originalText.charAt(i) ^ schluessel.charAt(i));
		}
		return geheimText;
	}

	public static String oneTimePad(String originalText, String schluessel) {
		String geheimText = "";
		String key = "";

		for (int i = 0; i < schluessel.length(); i += 8) {
			key += (char) binToInt(schluessel.substring(i, i + 8));
		}

		for (int i = 0; i < originalText.length(); i++) {
			geheimText += (char) (originalText.charAt(i) ^ key.charAt(i));
		}
		return geheimText;
	}

	public static String rsa(String originalText, int n, int e) {
		// Das Ensemle (n,e) stellt den public key des RSA-Verfahrens dar
		String geheimText = "";
		java.math.BigInteger nBig = new java.math.BigInteger("" + n);
		java.math.BigInteger eBig = new java.math.BigInteger("" + e);
		java.math.BigInteger mBig;
		int c;

		for (int i = 0; i < originalText.length(); i++) {
			mBig = new java.math.BigInteger("" + (int) originalText.charAt(i));
			c = mBig.modPow(eBig, nBig).intValue();
			geheimText += (char) c;
		}
		return geheimText;
	}

	public static ArrayList<BigInteger> rsa(String originalText,
			BigInteger nBig, BigInteger eBig) {
		// Das Ensemle (n,e) stellt den public key des RSA-Verfahrens dar
		ArrayList<BigInteger> result = new ArrayList<BigInteger>();
		// java.math.BigInteger nBig = new java.math.BigInteger(""+n);
		// java.math.BigInteger eBig = new java.math.BigInteger(""+e);
		java.math.BigInteger mBig;

		for (int i = 0; i < originalText.length(); i++) {
			mBig = new java.math.BigInteger("" + (int) originalText.charAt(i));
			BigInteger c = mBig.modPow(eBig, nBig);
			result.add(c);
		}
		return result;
	}

}
