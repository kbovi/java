package lizenz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.util.ArrayList;

import tools.Logbuch;

public class ESTReadLicence {
	private Logbuch myLogbuch;

	@SuppressWarnings("unchecked")
	public String getLicence() {
		BigInteger n = new BigInteger("6012707");
		// BigInteger e = new BigInteger ("3674911");
		BigInteger d = new BigInteger("422191");

		// int n = 2773;
		// int e = 17;
		// int d = 157;

		ArrayList<BigInteger> geheimText = new ArrayList<BigInteger>();
		String entschluesselt = "";

		this.myLogbuch = Logbuch.getLogbuch();
		this.myLogbuch.log("Lizenz wird gelesen!", 1);

		try {
			FileInputStream fs = new FileInputStream("Lizenz.EST");

			ObjectInputStream is = new ObjectInputStream(fs);

			geheimText = (ArrayList<BigInteger>) is.readObject();

			is.close();
		} catch (FileNotFoundException ex) {
			entschluesselt = "X";
			this.myLogbuch.log("FileNotFoundException", 1);
		} catch (SecurityException ex) {
			entschluesselt = "X";
			this.myLogbuch.log("SecurityException", 1);
		} catch (IOException ex) {
			entschluesselt = "X";
			this.myLogbuch.log("IOException", 1);
		} catch (ClassNotFoundException ex) {
			entschluesselt = "X";
			this.myLogbuch.log("IOException", 1);
		}

		if (entschluesselt.equals("X")) {
			entschluesselt = "";
			this.myLogbuch.log("Keine Lizenz lesbar!", 1);
			return entschluesselt;
		} else {
			BigInteger pruefer = vorsumme(geheimText);
			if (!pruefer.equals(geheimText.get(geheimText.size() - 1))) {
				entschluesselt = "";
				this.myLogbuch.log("falsche Prüfziffer", 1);
			} else {
				geheimText.remove(geheimText.size() - 1);
				entschluesselt = DeCrypt.rsa(geheimText, n, d);
				this.myLogbuch.log("Lizenz: " + entschluesselt, 1);
			}

			if (entschluesselt.equals("")) {
				this.myLogbuch.log("keine Lizenz!", 1);
				return entschluesselt;
			} else {
				return entschluesselt;
			}
		}
	}

	private static BigInteger vorsumme(ArrayList<BigInteger> bi) {
		BigInteger result = new BigInteger("0");
		for (int i = 0; i < bi.size() - 1; i++) {
			result = result.add(bi.get(i));
		}
		return result;
	}
}
