/**
 * 
 */
package tools;

import estKonstanten.ESTEigenschaften;
import estKonstanten.ESTStatics;

/**
 * @author Administrator
 */
public class Beobachter {

	private boolean isChanged;

	/**
	 * @uml.property name="lastOutDir"
	 */
	private String lastOutDir;
	/**
	 * @uml.property name="lastOutPath"
	 */
	private String lastOutPath;

	/**
	 * @uml.property name="lastDatenDir"
	 */
	private String lastDatenDir;
	/**
	 * @uml.property name="lastDatenPath"
	 */
	private String lastDatenPath;

	private static Beobachter beobacht = null;

	/**
     * 
     */
	public Beobachter() {
		this.isChanged = false;
		this.lastOutDir = "";
		this.lastOutPath = "";

		this.lastDatenDir = "";
		this.lastDatenPath = "";

		String lod = ESTEigenschaften.getLastOutDir();
		String lop = ESTEigenschaften.getLastOutPath();
		if (lod != null) {
			this.lastOutDir = lod;
		}
		if (lop != null) {
			this.lastOutPath = lop;
		}
		String ldd = ESTEigenschaften.getLastDatenDir();
		String ldp = ESTEigenschaften.getLastDatenPath();
		if (ldd != null) {
			this.lastDatenDir = ldd;
		}
		if (ldp != null) {
			this.lastDatenPath = ldp;
		}

	}

	public void save() {
		ESTEigenschaften.getProps().setProperty(ESTStatics.OutDir, lastOutDir);
		ESTEigenschaften.getProps()
				.setProperty(ESTStatics.OutPath, lastOutPath);
		ESTEigenschaften.getProps().setProperty(ESTStatics.DatenDir,
				lastDatenDir);
		ESTEigenschaften.getProps().setProperty(ESTStatics.DatenPath,
				lastDatenPath);
		ESTEigenschaften.saveProps();
	}

	public static Beobachter getBeobachter() {
		init();
		return beobacht;
	}

	public static void init() {
		if (beobacht == null) {
			beobacht = new Beobachter();
		}
	}

	public boolean somethingHasChanged() {
		return this.isChanged;
	}

	public void somethingChanged() {
		this.isChanged = true;
	}

	public void resetSomethingChanged() {
		this.isChanged = false;
	}

	/**
	 * @return the lastDatenDir
	 * @uml.property name="lastdatenDir"
	 */
	public String getLastDatenDir() {
		return lastDatenDir;
	}

	/**
	 * @param lastDir
	 *            the lastDatenDir to set
	 * @uml.property name="lastDatenDir"
	 */
	public void setLastDatenDir(String lastDatenDir) {
		this.lastDatenDir = lastDatenDir;
	}

	/**
	 * @return the lastDatenPath
	 * @uml.property name="lastDatenPath"
	 */
	public String getLastDatenPath() {
		return lastDatenPath;
	}

	/**
	 * @param lastPath
	 *            the lastDatenPath to set
	 * @uml.property name="lastDatenPath"
	 */
	public void setLastDatenPath(String lastDatenPath) {
		this.lastDatenPath = lastDatenPath;
	}

	/**
	 * @return the lastOutDir
	 * @uml.property name="lastOutDir"
	 */
	public String getLastOutDir() {
		return lastOutDir;
	}

	/**
	 * @param lastDir
	 *            the lastDir to set
	 * @uml.property name="lastDir"
	 */
	public void setLastOutDir(String lastOutDir) {
		this.lastOutDir = lastOutDir;
	}

	/**
	 * @return the lastOutPath
	 * @uml.property name="lastOutPath"
	 */
	public String getLastOutPath() {
		return lastOutPath;
	}

	/**
	 * @param lastPath
	 *            the lastOutPath to set
	 * @uml.property name="lastOutPath"
	 */
	public void setLastOutPath(String lastOutPath) {
		this.lastOutPath = lastOutPath;
	}

}
