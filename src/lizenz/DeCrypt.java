package lizenz;

import java.math.BigInteger;
import java.util.ArrayList;

public class DeCrypt {
	// Hilfsmethoden
	static int binToInt(String string) throws NumberFormatException {
		int zahl = 0;
		for (int i = 0; i < 8; i++) {
			zahl += (Integer.parseInt(string.substring(i, i + 1)) * Math.pow(2,
					7 - i));
		}
		return zahl;
	}

	// Verschlüsselungsmethoden
	public static String caesar(String geheimText, int k) {
		String originalText = "";
		for (int i = 0; i < geheimText.length(); i++) {
			originalText = originalText
					+ (char) ((geheimText.charAt(i) + 26 - k - 65) % 26 + 65);
		}
		return originalText.toLowerCase();
	}

	public static String xor(String geheimText, String schluessel) {
		String originalText = "";
		for (int i = 0; i < geheimText.length(); i++) {
			originalText += (char) (geheimText.charAt(i) ^ schluessel.charAt(i));
		}
		return originalText;
	}

	public static String oneTimePad(String geheimText, String schluessel) {
		String originalText = "";
		String key = "";

		for (int i = 0; i < schluessel.length(); i += 8) {
			key += (char) binToInt(schluessel.substring(i, i + 8));
		}

		for (int i = 0; i < geheimText.length(); i++) {
			originalText += (char) (geheimText.charAt(i) ^ key.charAt(i));
		}
		return originalText;
	}

	public static String rsa(String geheimText, int n, int d) {
		// Das Ensemle (n,e) stellt den public key des RSA-Verfahrens dar
		String originalText = "";
		java.math.BigInteger nBig = new java.math.BigInteger("" + n);
		java.math.BigInteger dBig = new java.math.BigInteger("" + d);
		java.math.BigInteger cBig;
		int m;

		for (int i = 0; i < geheimText.length(); i++) {
			cBig = new java.math.BigInteger("" + (int) geheimText.charAt(i));
			m = cBig.modPow(dBig, nBig).intValue();
			originalText += (char) m;
		}
		return originalText;
	}

	public static String rsa(ArrayList<BigInteger> geheimText, BigInteger nBig,
			BigInteger dBig) {
		// Das Ensemle (n,e) stellt den public key des RSA-Verfahrens dar
		String originalText = "";
		// java.math.BigInteger nBig = new java.math.BigInteger(""+n);
		// java.math.BigInteger dBig = new java.math.BigInteger(""+d);
		java.math.BigInteger cBig;

		for (int i = 0; i < geheimText.size(); i++) {
			cBig = geheimText.get(i);
			int m = cBig.modPow(dBig, nBig).intValue();
			originalText += (char) m;
		}
		return originalText;
	}

}
