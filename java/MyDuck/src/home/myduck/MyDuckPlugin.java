package home.myduck;

import org.gjt.sp.jedit.EditPlugin;

/**
 * The princip of being the plugin class of a plugin in its simplest form.
 * <p>
 * It extends EditPlugin, as an plugin must do. 
 * <p>
 * The existence of this, minimum implementation of an abstract class, can be verified by:
 * <blockquote><pre>
 *     jEdit.getPlugin("home.myduck.MyDuckPlugin").getClassName();
 * </pre></blockquote>
 * @see org.gjt.sp.jedit.EditPlugin
 */
public class MyDuckPlugin extends EditPlugin { }
