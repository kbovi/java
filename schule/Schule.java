package schule;

import java.io.Serializable;

import lehrer.Kollegium;
import eltern.Eltern;

/**
 * @author Administrator
 */
public class Schule implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 2304557432071734377L;
	private Eltern myEltern;
	private Kollegium myKollegen;
	/**
	 * @uml.property name="vorAlgo"
	 */
	private Boolean vorAlgo;

	/**
	 * @return the vorAlgo
	 * @uml.property name="vorAlgo"
	 */
	public Boolean getVorAlgo() {
		return vorAlgo;
	}

	/**
	 * @param vorAlgo
	 *            the vorAlgo to set
	 * @uml.property name="vorAlgo"
	 */
	public void setVorAlgo(Boolean vorAlgo) {
		this.vorAlgo = vorAlgo;
	}

	/**
	 * @param myEltern
	 * @param myKollegen
	 */
	public Schule(Eltern myEltern, Kollegium myKollegen, Boolean vorAlgo) {
		this.myEltern = myEltern;
		this.myKollegen = myKollegen;
		this.vorAlgo = vorAlgo;
	}

	/**
	 * @return the myEltern
	 * @uml.property name="myEltern"
	 */
	public Eltern getMyEltern() {
		return myEltern;
	}

	/**
	 * @return the myKollegen
	 * @uml.property name="myKollegen"
	 */
	public Kollegium getMyKollegen() {
		return myKollegen;
	}
}
