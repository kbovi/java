package lizenz;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;

public class ESTWriteLicence {
	public static void main(String[] args) {
		BigInteger n = new BigInteger("6012707");
		BigInteger e = new BigInteger("3674911");
		// BigInteger d = new BigInteger ("422191");

		// int n = 2773;
		// int e = 17;
		// int d = 157;

		String originalText = args[0];
		ArrayList<BigInteger> geheimText = EnCrypt.rsa(originalText, n, e);
		// System.out.println("Original  = " + originalText);
		// System.out.println("Geheim    = !" + geheimText + "!");
		// String kontrolle = DeCrypt.rsa(geheimText, n, d);
		// System.out.println ("Kontrolle = " + kontrolle);

		BigInteger pruefen = summe(geheimText);

		geheimText.add(pruefen); // Achtung: jetzt ist ein Objekt zu viel
									// drin!!!

		try {

			FileOutputStream fs = new FileOutputStream("Lizenz.EST");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(geheimText);
			os.close();
		} catch (IOException ex) {
			System.out.println("IO-Fehler");
		}
	}

	private static BigInteger summe(ArrayList<BigInteger> bi) {
		BigInteger result = new BigInteger("0");
		for (int i = 0; i < bi.size(); i++) {
			result = result.add(bi.get(i));
		}
		return result;
	}
}
