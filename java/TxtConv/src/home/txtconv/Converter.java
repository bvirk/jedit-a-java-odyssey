package home.txtconv;

/** 
 * Forced generality of a converter. The default methods gives an easy way to create a 'no operation' converter by 
 * just adding a dummy return ""; output() method. 
 * 
 */
public interface Converter {
	
    /**
     * The only method having no default implementation
     *
     * @return String output
     */
	public String output();
	
	/**
     * Defaults to "text";
     * @return String output file suffix 
     */
	default public String getMode()  { return "text"; }

	/**
     * Defaults to false.
     * @return boolean isToFileAble determines if a converting result can be saved as file.
     */
	default public boolean isToFileAble() { return false; }
}
