/* :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:  */ 
package home.txtconv;
//{{{ import
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.bsh.NameSpace;
import org.gjt.sp.jedit.BeanShell;
import java.awt.Frame;
import java.util.HashSet;
import java.util.Set;
import java.util.Properties;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EditPlugin;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.Buffer; 
import org.gjt.sp.jedit.Mode;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.msg.BufferUpdate;
import org.gjt.sp.jedit.msg.VFSUpdate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter; 
import static home.txtconv.util.Util.*;
//}}} 
 
/**
 * The Plugin used to convert a mode to its corresponding output.
 * It acts on buffer, file or basedir level.
 */
public class TxtConvPlugin extends EditPlugin implements EBComponent,Runnable { 
	
	//{{{ public methods
	
	//{{{ Constructer
    /**
     * The constructer jEdit uses when putting the plugin on list
     */
	public TxtConvPlugin() {
		super(); 
		setReleaseCond(" "," ");
		
		iconifyOnSavedOutput = options.txtconvBoolean("iconifyOnSavedOutput");
		TxtConvPlugin.printUrgency = options.txtconvInteger("printUrgency");
		options.txtconv("pluginhome",ux(getPluginHome(this).getPath()));
		propModes = options.txtconvProperties(OPTION_PREFIX+"newfileSuffixOfMode.");
	}
	//}}}

	//{{{ setIconifyOnSavedOutput(boolean state)
	/**
     * State IconifyOnSavedOutput is mirrored in property options.txtconv.iconifyOnSavedOutput
     * when true jedit is iconified on saved output for convenient view in browser.
     * when false, a message apears in statusline.
     *
     * @param state that desides whether jEdit will be iconified on saved output
     */
	public void setIconifyOnSavedOutput(boolean state) {
		iconifyOnSavedOutput=options.txtconvBoolean("iconifyOnSavedOutput",state);
	}
	//}}}
	
	//{{{ void setPrintUrgency(int urgency)
    /**
     * Debug print messages has a urgency paremeter and is printet if this is equal or above the urgency level of txtConvPlugin 
     *
     * @param urgency desides which messages to print. 
     */
	public void setPrintUrgency(int urgency) { 
		TxtConvPlugin.printUrgency=options.txtconvInteger("printUrgency",urgency);
	} //}}}
	
	//{{{ void start()
	/**
     * Adds txtConvPlugin as EditbusListener on load.
     * Public for integration purposes
     */
	@Override
	public void start() {
		print(NAME+".start()");
		EditBus.addToBus(this);
	}
	//}}}
	
	//{{{ void stop() 
    /**
     * Removes txtConvPlugin as EditbusListener upon unload.
     * Public for integration purposes
     */
	@Override
	public void stop() {
		print(NAME+".stop()"); 
		EditBus.removeFromBus(this);
	}
	//}}}
		
	//{{{ void conv2file(Buffer buffer)
    /**
     * Converts to file if converter of editmode is toFileAble.
     *
     * @param buffer containing the source file.
     */
	public void conv2file(Buffer buffer) { 
	 	
		if (!savedCurrentBuffer(buffer))
			return;
		BaseDir baseDir = getBaseDir(buffer.getPath());
		if (null !=   baseDir)
			baseDir.flushSite();
		else {
			outputToBuffer=false;
			srcFiles=Arrays.asList(buffer.getPath()); 
			new Thread(this).start();
		}
		
	}
	//}}}


	//{{{  void convTofiles(List fileNames) 
    /**
     * Converts to file if converter of suffix is toFileAble.
     *
     * @param fileName containing the source file.
     */
	public void convToFiles(List<String> fileNames) {
		outputToBuffer=false;
		srcFiles=fileNames;
		new Thread(this).start();
	}
	//}}}
	
	//{{{ String getImplModes()
    /**
     * Lists of implemented conversion types
     *
     * @return list of implemented conversions 
     */
	public String getImplModes() {
		StringBuilder lines = new StringBuilder()
			.append(". This plugin implements following conversions\n")
			.append("\n")
			.append("edit\toutput\n")
			.append("mode\tsuffix\n")
			.append("-----\t-----\n");
			
			for (String s:propModes.stringPropertyNames())
				lines.append(s+"\t"+propModes.getProperty(s)+"\n");
			return lines
			.append("\nMode of a file with a given suffix can be changed in menuline->utilities->buffer options\n")
			.toString();	
	}
	//}}}

	//{{{ conv2buffer(Buffer buffer)
    /** 
     * Converts to a new buffer if converter of editmode is toBufferAble.
     *
     * @param buffer containing the source file.
     */
	public void conv2buffer(Buffer buffer) {
		if (!savedCurrentBuffer(buffer))
			return;
		srcFiles=Arrays.asList(buffer.getPath());
		outputToBuffer=true;
		new Thread(this).start();
	}
	//}}}

	//{{{ handleMessage(EBMessage message)
	/**
     * Mandatory for being a EBComponent. Used to sychronize invoking Converter to state of saving buffer.
     *
     * @param message of possible BufferUpdate instance
     */ 
	public void handleMessage(EBMessage message) { 
		if (message instanceof BufferUpdate) {    
			printTimed("BufferUpdate: "+((BufferUpdate)message).paramString());
			processBufferUpdate(
				 (Buffer)((BufferUpdate)message).getSource()
				,((BufferUpdate)message).getWhat().toString());
		
		} else 
			if (message instanceof VFSUpdate) {
				printTimed("VFSUpdate: "+ux(((VFSUpdate)message).getPath()));
				processVFSUpdate(ux(((VFSUpdate)message).getPath()));
			}
	}
	//}}} }}}
	
	//{{{ Test Method
	public static void showspinners() {
		print(9,""+Spinner.values().length+"\n spinners\n=======\n");
		
		for (Spinner s : Spinner.values())
			print(s);
		new Thread(new SpinTest()).start();
	} //}}}
	
    //{{{ public data member
    /**
     * Some final value used as property scope
     */
	public static final String NAME = "txtconv";  
	
	/**
     * Some final value used as property scope. Function optProperty(String key) is implmentet as jEdit.getProperty(OPTION_PREFIX+key)
     */
	public static final String OPTION_PREFIX = "options.txtconv.";
	
	/**
     * Used when project is compiled with util for printing to dockable window. Used under development.
     */
	public static int printUrgency;
	//}}}
 
	//{{{ private methods 

	//{{{ void saveCurrentBuffer()
	private boolean savedCurrentBuffer(Buffer buffer) {
		if (buffer.isDirty()) { 
			if (!buffer.save(jEdit.getActiveView(),null)) {
				GUIUtilities.message(null,"options.txtconv.joptionpane",new Object[]{buffer.getPath()});
				return false;
			}
		}
		return true;
	}
	//}}}
	
	//{{{ void toNewBuffer(String content,String mode)
	private void toNewBuffer(String content,String mode) throws InterruptedException {
		setReleaseCond("CREATED","");
		Buffer untitled = jEdit.newFile(jEdit.getActiveView());
		while (!gotThat(untitled)) 
			Thread.sleep(100);
		Thread.sleep(100);
		
		jEdit.getActiveView().setBuffer(untitled);
		setReleaseCond("DIRTY_CHANGED","");
		untitled.insert(0,content); 
		while (!gotThat(untitled))  
			Thread.sleep(100);
		Thread.sleep(100);
		
		setReleaseCond("PROPERTIES_CHANGED","");
		untitled.setMode(mode);
		while (!gotThat(untitled)) 
			Thread.sleep(100);
		Thread.sleep(100);
	}
	//}}} 

	//{{{ void run()
	public void run() { // conseptually private - could be enforced using innner class
		//Thread.currentThread().setPriority(4);
		
		for (String sFile : srcFiles) {
			try {
				Converter conv= getConverter(sFile);
				String outFile = sFile.replaceAll("\\.\\w+$",".")+conv.getMode();
				
				if (null != conv ) {
				
					if ((!outputToBuffer && conv.isToFileAble()) ) {
						try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")) {
							osw.write(conv.output());
							printTimed(0,"saving "+outFile);
						} catch(Exception e) {  throw e;}
						if (iconifyOnSavedOutput)
							jEdit.getActiveView().setState(Frame.ICONIFIED);
						//else
							//jEdit.getActiveView().getStatus().setMessage(outFile.replaceAll("^.+/","")+" made");
					} else 
						toNewBuffer(conv.output(),conv.getMode());
				}
			} catch(Exception e) {BeanShellErrorDialog(e);}
		}
		Spinner.clr();
	}
	//}}}
	
	//{{{ Converter getConverter()
	private Converter getConverter(String srcFile) {
		printTimed(0,"getconverter");
        
		String curMode = ModesOfGlob.getNameOfSuffix(srcFile.replaceAll("^.+\\.",""));
			printTimed("getConverter: curmode="+curMode);
		if ((null != curMode) && propModes.containsKey(curMode)) {	
			switch(propModes.getProperty(curMode)) {
				case "html":
					return new HtmlText(srcFile);
			}
			return new Converter() {
				public String output() {
					return null; // unimplemented although options.txtconv.newfileSuffixOfMode.* listet
			}};
		} else
			return new Converter() { 
				public String output() { 
					return null;// not implemenentet
			}};
	}
	//}}}
	      
    //{{{ void processBufferUpdate(Buffer source, String what)
	private void processBufferUpdate(Buffer source, String what) {
		synchronized(this) {
		if (what.equals(passWhat)) 
			bufferSet.add( source);
	}}
	//}}}
	
	//{{{ void processVFSUpdate(String path) 
	private void processVFSUpdate(String path) {
		if (!gotVFSUPath)
			if (path.equals(passPath))
				gotVFSUPath=true;
	}
	//}}} 

	//{{{ boolean gotThat(Buffer buffer)
	private boolean gotThat(Buffer buffer) {
		synchronized(this) {
		boolean gt=bufferSet.contains(buffer);
		if (gt) {
			passWhat="";
			bufferSet.clear();
		}
		return gt;
	}}
	//}}}

	//{{{ void setReleaseCond(String what,String VFSUpdPath)
	private void setReleaseCond(String what,String VFSUpdPath) {
		passWhat = what;
		passPath = VFSUpdPath;
		gotThatDone = false;
		gotVFSUPath = false;
	}
	//}}} 

	//{{{ private BaseDir getBaseDir(String fileName)
	private BaseDir getBaseDir(String fileName) {
		if (!options.txtconvBoolean("refreshOnFileResidesInBaseDir"))
			return null;
		BaseDir baseDir = this.new BaseDir(fileName);
		return baseDir.dependencyChain == null 
			? null 
			: baseDir;
	}
	// }}} }}}
	
	//{{{ private class BaseDir
	private class BaseDir implements Runnable {
		
		public BaseDir(String fileName) {
			String baseDir = options.txtconv("basedir");
			if (fileName.startsWith(baseDir)) {
				String modeKey = ModesOfGlob.getNameOfSuffix(fileName.replaceAll("^.+\\.",""))+".dependencychain";
				if (propModes.containsKey(modeKey)) 
					dependencyChain = propModes.getProperty(modeKey).split("->");
				
			}
		}
		//{{{ private methods
		//{{{ void flushSite 
		private void flushSite() { 
			new Thread(this).start();
		}
		//}}}
		
		//{{{ public void run()
		public void run() { 
			baseDirP = options.txtconv("basedir");
			if (baseDirP != null && baseDirP.length() > 0) { 
				for (int i=0; i<dependencyChain.length-1;i++)
					updatePagesOfSuffix(dependencyChain[i+1],dependencyChain[i]);
			}
		}
		//}}}
		
		//{{{ private void updatePagesOfSuffix(String ae, String ss)
		private void updatePagesOfSuffix(String ae, String ss) {
			actionOnELder = ae;
			srcSuffix = ss;
			wasDirty=false;
			fileNames =new ArrayList<>();
			crawlBaseDir(baseDirP,"");
			if (actionOnELder.equals("html") && fileNames.size()>0)
				convToFiles(fileNames);
			printTimed(0,"updatePagesOfSuffix on "+ss+" -> "+ae);
		} //}}}
		
		//{{{ String fileLongName(File f)
		private String fileLongName(File f) { 
			return f.getPath() + "("+mdhms(f.lastModified())+")"; 
		}
		//}}}
		
		//{{{ String mdhms(long unixtime)
		private String mdhms(long unixtime) {
			return new java.text.SimpleDateFormat("YYMMdd HH:mm:ss.SSS").format(new java.util.Date(unixtime)).toString();
		} 
		//}}}
		
		//{{{ private void crawlBaseDir(String path,String crawled)
		private void crawlBaseDir(String path,String crawled) {
			for (File f : new File(path).listFiles()) 
				if (f.isDirectory()) 
					crawlBaseDir(path+"/"+f.getName(),crawled+f.getName()+"/");
				else
					if (f.getName().endsWith("."+srcSuffix)) {
						Spinner.next(Spinner.CIRCLEHALVES);
						printTimed(0,"chekking "+fileLongName(f));
						long possElderLastM=0;
						File  possElder =new File(f.getPath().replaceAll("\\.\\w+$","."+actionOnELder));
						if (possElder.exists())
							possElderLastM = possElder.lastModified();
						if (f.lastModified() > possElderLastM) { 
							printTimed(0,"update: "+fileLongName(f)+" to "+fileLongName(new File(f.getPath().replaceAll("\\.\\w+$","."+actionOnELder)) ));
							wasDirty=true;
							switch (srcSuffix) {
								case "bsh":
									//p("bsh->md");
									String newFileName =f.getPath().replaceAll("\\.\\w+$","."+actionOnELder);
									long now = System.currentTimeMillis()/1000*1000;
									//pt("now="+mdhms(now));
									Spinner.next(Spinner.HAM);
									BeanShell.runScript(jEdit.getActiveView(),f.getPath(),null,true);
									int abortAfterLoops=10;
									try {
										while (new File(newFileName).lastModified() < now && abortAfterLoops-- > 0) 
											Thread.sleep(100);
									} catch(InterruptedException e) {}
									//pt("filelast="+mdhms(fnow));
									break;
								case "md":
									fileNames.add(f.getPath());
									printTimed(0,f.getPath()+" added im case md");
							}
						}
					}
		} //}}}	}}}	
		
		public String[] dependencyChain;
		
		private boolean wasDirty;
		private String actionOnELder;
		private String srcSuffix;
		private String baseDirP;
		private List<String> fileNames;
	}
	//}}}

	//{{{ private data members
	private boolean needProcessing=false;
	private boolean iconifyOnSavedOutput;
	private List<String> srcFiles;
	private volatile boolean outputToBuffer;
	private volatile boolean busy=false;
	private Properties propModes;
	
	private String passWhat;   
	private String passPath;
	private volatile boolean gotThatDone;
	private volatile boolean gotVFSUPath;			
	Set<Buffer> bufferSet = new HashSet<>();
	//}}}
}
