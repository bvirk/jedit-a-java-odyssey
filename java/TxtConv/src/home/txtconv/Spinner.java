package home.txtconv;

/**
 * Spinner is a  indeterminate progress bar which sliding samples of text can give a true 
 * proof of activity, if it is driven by progress in execution. The samples are strings of text
 * Spinner is shown i status bar.
 *
 * Called as eg. Spinner.next(Spinner.HAM);
 */
public enum Spinner {
	// step, len,string
	
	 DOTS( 3,3,".  .. ... ..  .   ")
	,HAM(   1,1,"\u2631\u2632\u2634")
	,STAR( 1,1,"+x*")
	,CIRCLEHALVES(1,1,"\u25d0\u25d3\u25d1\u25d2")
	,BRAILLE(1,4,"\u2880\u2800\u2840\u2800\u2804\u2800\u2882\u2800\u2842\u2800\u2805\u2800\u2883\u2800\u2843\u2800\u280d\u2800\u288b\u2800\u284b\u2800\u280d\u2801\u288b\u2801\u284b\u2801\u280d\u2809\u280b\u2809\u280b\u2809\u2809\u2819\u2809\u2819\u2809\u2829\u2808\u2899\u2808\u2859\u2888\u2829\u2840\u2899\u2804\u2859\u2882\u2829\u2842\u2898\u2805\u2858\u2883\u2828\u2843\u2890\u280d\u2850\u288b\u2820\u284b\u2880\u280d\u2841\u288b\u2801\u284b\u2801\u280d\u2809\u280b\u2809\u280b\u2809\u2809\u2819\u2809\u2819\u2809\u2829\u2808\u2899\u2808\u2859\u2808\u2829\u2800\u2899\u2800\u2859\u2800\u2829\u2800\u2898\u2800\u2858\u2800\u2828\u2800\u2890\u2800\u2850\u2800\u2820\u2800\u2880\u2800\u2840")
	,ARROWS3(1,5,"\u25c3\u25c3\u25c3\u25c3\u25c3\u25c2\u25c3\u25c3\u25c3\u25c3\u25c3")
	,ARROWS4(1,5,"\u25c1\u25c1\u25c1\u25c1\u25c1\u25c0\u25c1\u25c1\u25c1\u25c1\u25c1")
	;
	static private int ix;
	static private int curOrd=-1;
	
	private int step,len;
	private String chrs;
	
	private  Spinner(int s,int l, String c) {step=s;len=l;chrs=c;}
	
    /**
     * shifts spinner to next sample of text in a rounding cycle when
     * called with same spinner. Each call with another spinner starts cycling 
     * that spinners sample list and the old spinners state is forgotten
     *
     * @param spinner to be shown
     */
	static public void next(Spinner spinner) { 
		if (Spinner.curOrd != spinner.ordinal()) {
			Spinner.ix=0;
			Spinner.curOrd = spinner.ordinal();
		}
		else
			Spinner.ix = Spinner.ix >= spinner.chrs.length() - spinner.len
				? 0
				: Spinner.ix + spinner.step;
		put(spinner.chrs.substring(Spinner.ix,Spinner.ix+spinner.len));	
	}
    /**
     * clears the status where spinner previous was showed 
     * 
     */
	static public void clr() {
		Spinner.curOrd=-1;
		put(null);
	}
	static private void put(String mes) {
		org.gjt.sp.jedit.jEdit.getActiveView().getStatus().setMessage(mes);
	}
}
