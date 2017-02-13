package com.htetznaing.mmallsimnew;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.mmallsimnew", "com.htetznaing.mmallsimnew.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.htetznaing.mmallsimnew", "com.htetznaing.mmallsimnew.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.mmallsimnew.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _t = null;
public static anywheresoftware.b4a.objects.Timer _t1 = null;
public de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper _fb = null;
public de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper _fb1 = null;
public b4a.util.BClipboard _copy = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btelenor = null;
public anywheresoftware.b4a.objects.ButtonWrapper _bmpt = null;
public anywheresoftware.b4a.objects.ButtonWrapper _booredoo = null;
public anywheresoftware.b4a.objects.ButtonWrapper _bmec = null;
public anywheresoftware.b4a.object.XmlLayoutBuilder _res = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _tbg = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _mbg = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _obg = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _mecbg = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltitle = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfooter = null;
public anywheresoftware.b4a.objects.StringUtils _su = null;
public anywheresoftware.b4a.phone.Phone _scr = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _adview1 = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper _adview2 = null;
public anywheresoftware.b4a.phone.Phone _ph = null;
public anywheresoftware.b4a.objects.LabelWrapper _tlb = null;
public anywheresoftware.b4a.objects.ButtonWrapper _menu = null;
public anywheresoftware.b4a.objects.ButtonWrapper _share = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _mmbg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _sbg = null;
public com.htetznaing.mmallsimnew.runads _runads = null;
public com.htetznaing.mmallsimnew.ads _ads = null;
public com.htetznaing.mmallsimnew.lollipop _lollipop = null;
public com.htetznaing.mmallsimnew.tutorial _tutorial = null;
public com.htetznaing.mmallsimnew.telenor _telenor = null;
public com.htetznaing.mmallsimnew.mpt _mpt = null;
public com.htetznaing.mmallsimnew.ooredoo _ooredoo = null;
public com.htetznaing.mmallsimnew.mectel _mectel = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (ads.mostCurrent != null);
vis = vis | (lollipop.mostCurrent != null);
vis = vis | (tutorial.mostCurrent != null);
vis = vis | (telenor.mostCurrent != null);
vis = vis | (mpt.mostCurrent != null);
vis = vis | (ooredoo.mostCurrent != null);
vis = vis | (mectel.mostCurrent != null);
return vis;}
public static String  _about_click() throws Exception{
 //BA.debugLineNum = 147;BA.debugLine="Sub about_Click";
 //BA.debugLineNum = 148;BA.debugLine="StartActivity(lollipop)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._lollipop.getObject()));
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imvlogo = null;
anywheresoftware.b4a.objects.PanelWrapper _divbar = null;
anywheresoftware.b4a.objects.PanelWrapper _divbar1 = null;
 //BA.debugLineNum = 44;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 45;BA.debugLine="tlb.Initialize(\"tlb\")";
mostCurrent._tlb.Initialize(mostCurrent.activityBA,"tlb");
 //BA.debugLineNum = 46;BA.debugLine="tlb.Color = Colors.RGB(33,150,243)";
mostCurrent._tlb.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (33),(int) (150),(int) (243)));
 //BA.debugLineNum = 47;BA.debugLine="Activity.AddView(tlb,0%x,0%y,100%x,55dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._tlb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (55)));
 //BA.debugLineNum = 49;BA.debugLine="mmbg.Initialize(LoadBitmap(File.DirAssets,\"menu.p";
mostCurrent._mmbg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"menu.png").getObject()));
 //BA.debugLineNum = 50;BA.debugLine="menu.Initialize(\"menu\")";
mostCurrent._menu.Initialize(mostCurrent.activityBA,"menu");
 //BA.debugLineNum = 51;BA.debugLine="menu.Background = mmbg";
mostCurrent._menu.setBackground((android.graphics.drawable.Drawable)(mostCurrent._mmbg.getObject()));
 //BA.debugLineNum = 52;BA.debugLine="menu.Gravity = Gravity.CENTER";
mostCurrent._menu.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 53;BA.debugLine="Activity.AddView(menu,10dip,12.5dip,30dip,30dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._menu.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12.5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 55;BA.debugLine="sbg.Initialize(LoadBitmap(File.DirAssets,\"share.p";
mostCurrent._sbg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png").getObject()));
 //BA.debugLineNum = 56;BA.debugLine="share.Initialize(\"share\")";
mostCurrent._share.Initialize(mostCurrent.activityBA,"share");
 //BA.debugLineNum = 57;BA.debugLine="share.Background = sbg";
mostCurrent._share.setBackground((android.graphics.drawable.Drawable)(mostCurrent._sbg.getObject()));
 //BA.debugLineNum = 58;BA.debugLine="share.Gravity = Gravity.CENTER";
mostCurrent._share.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 59;BA.debugLine="Activity.AddView(share,100%x - 40dip,12.5dip,30di";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._share.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12.5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 61;BA.debugLine="Activity.Color = Colors.White";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 62;BA.debugLine="ph.SetScreenOrientation(1)";
mostCurrent._ph.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 63;BA.debugLine="Activity.LoadLayout(\"Lay1\")";
mostCurrent._activity.LoadLayout("Lay1",mostCurrent.activityBA);
 //BA.debugLineNum = 64;BA.debugLine="fb.Icon = res.GetDrawable(\"ic_add_white_24dp\")";
mostCurrent._fb.setIcon(mostCurrent._res.GetDrawable("ic_add_white_24dp"));
 //BA.debugLineNum = 65;BA.debugLine="fb.HideOffset = 100%y - fb.Top";
mostCurrent._fb.setHideOffset((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-mostCurrent._fb.getTop()));
 //BA.debugLineNum = 66;BA.debugLine="fb.Hide(False)";
mostCurrent._fb.Hide(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 67;BA.debugLine="fb.Show(True)";
mostCurrent._fb.Show(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 69;BA.debugLine="fb1.Icon = res.GetDrawable(\"about\")";
mostCurrent._fb1.setIcon(mostCurrent._res.GetDrawable("about"));
 //BA.debugLineNum = 70;BA.debugLine="fb1.HideOffset = 100%y - fb.Top";
mostCurrent._fb1.setHideOffset((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-mostCurrent._fb.getTop()));
 //BA.debugLineNum = 71;BA.debugLine="fb1.Hide(False)";
mostCurrent._fb1.Hide(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 72;BA.debugLine="fb1.Show(True)";
mostCurrent._fb1.Show(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 74;BA.debugLine="scr.SetScreenOrientation(1)";
mostCurrent._scr.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 76;BA.debugLine="StartService(RunAds)";
anywheresoftware.b4a.keywords.Common.StartService(mostCurrent.activityBA,(Object)(mostCurrent._runads.getObject()));
 //BA.debugLineNum = 77;BA.debugLine="AdView1.Initialize(\"AdView1\",\"ca-app-pub-41733485";
mostCurrent._adview1.Initialize(mostCurrent.activityBA,"AdView1","ca-app-pub-4173348573252986/4583640954");
 //BA.debugLineNum = 78;BA.debugLine="AdView1.LoadAd";
mostCurrent._adview1.LoadAd();
 //BA.debugLineNum = 79;BA.debugLine="Activity.AddView(AdView1, 0%x,100%y - 50dip, 100%";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._adview1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 81;BA.debugLine="AdView2.Initialize(\"AdView2\",\"ca-app-pub-41733485";
mostCurrent._adview2.Initialize(mostCurrent.activityBA,"AdView2","ca-app-pub-4173348573252986/6060374151");
 //BA.debugLineNum = 82;BA.debugLine="AdView2.LoadAd";
mostCurrent._adview2.LoadAd();
 //BA.debugLineNum = 84;BA.debugLine="t.Initialize(\"t\",15000)";
_t.Initialize(processBA,"t",(long) (15000));
 //BA.debugLineNum = 85;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 86;BA.debugLine="t1.Initialize(\"t1\",100)";
_t1.Initialize(processBA,"t1",(long) (100));
 //BA.debugLineNum = 87;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 89;BA.debugLine="Dim imvLogo As ImageView";
_imvlogo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 90;BA.debugLine="imvLogo.Initialize (\"\")";
_imvlogo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 91;BA.debugLine="imvLogo.Bitmap = LoadBitmap(File.DirAssets , \"ico";
_imvlogo.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"icon.png").getObject()));
 //BA.debugLineNum = 92;BA.debugLine="imvLogo.Gravity = Gravity.FILL";
_imvlogo.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 93;BA.debugLine="Activity.AddView ( imvLogo , 50%x - 50dip  , 65di";
mostCurrent._activity.AddView((android.view.View)(_imvlogo.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (65)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 95;BA.debugLine="Dim divBar,divBar1 As Panel";
_divbar = new anywheresoftware.b4a.objects.PanelWrapper();
_divbar1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 96;BA.debugLine="divBar.Initialize (\"\")";
_divbar.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 97;BA.debugLine="divBar.Color = Colors.LightGray";
_divbar.setColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 98;BA.debugLine="Activity.AddView (divBar , 18dip ,( imvLogo.Heigh";
mostCurrent._activity.AddView((android.view.View)(_divbar.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (18)),(int) ((_imvlogo.getHeight()+_imvlogo.getTop())+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (36))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 100;BA.debugLine="lblTitle.Initialize (\"\")";
mostCurrent._lbltitle.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 101;BA.debugLine="lblTitle.Text = \"All Sim Card Register!\"";
mostCurrent._lbltitle.setText((Object)("All Sim Card Register!"));
 //BA.debugLineNum = 102;BA.debugLine="lblTitle.TextColor = Colors.RGB(0,136,82)";
mostCurrent._lbltitle.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (136),(int) (82)));
 //BA.debugLineNum = 103;BA.debugLine="lblTitle.TextSize = 20";
mostCurrent._lbltitle.setTextSize((float) (20));
 //BA.debugLineNum = 104;BA.debugLine="lblTitle.Gravity = Gravity.CENTER";
mostCurrent._lbltitle.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 105;BA.debugLine="Activity.AddView (lblTitle, 10dip , (divBar.Top+d";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lbltitle.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) ((_divbar.getTop()+_divbar.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 106;BA.debugLine="lblTitle.Height = su.MeasureMultilineTextHeight (";
mostCurrent._lbltitle.setHeight(mostCurrent._su.MeasureMultilineTextHeight((android.widget.TextView)(mostCurrent._lbltitle.getObject()),mostCurrent._lbltitle.getText()));
 //BA.debugLineNum = 108;BA.debugLine="lblFooter.Initialize (\"lblFooter\")";
mostCurrent._lblfooter.Initialize(mostCurrent.activityBA,"lblFooter");
 //BA.debugLineNum = 109;BA.debugLine="lblFooter.Text = \"Powered By Myanmar Android Apps";
mostCurrent._lblfooter.setText((Object)("Powered By Myanmar Android Apps"));
 //BA.debugLineNum = 110;BA.debugLine="lblFooter.TextColor = Colors.RGB(156,39,176)";
mostCurrent._lblfooter.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (156),(int) (39),(int) (176)));
 //BA.debugLineNum = 111;BA.debugLine="lblFooter.TextSize = 12";
mostCurrent._lblfooter.setTextSize((float) (12));
 //BA.debugLineNum = 112;BA.debugLine="lblFooter.Gravity = Gravity.CENTER";
mostCurrent._lblfooter.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 114;BA.debugLine="divBar1.Initialize (\"\")";
_divbar1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 115;BA.debugLine="divBar1.Color = Colors.LightGray";
_divbar1.setColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 116;BA.debugLine="Activity.AddView (divBar1 , 18dip , (lblTitle.Hei";
mostCurrent._activity.AddView((android.view.View)(_divbar1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (18)),(int) ((mostCurrent._lbltitle.getHeight()+mostCurrent._lbltitle.getTop())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (36))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 118;BA.debugLine="tbg.Initialize(Colors.RGB(11,149,211),15)";
mostCurrent._tbg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (11),(int) (149),(int) (211)),(int) (15));
 //BA.debugLineNum = 119;BA.debugLine="mbg.Initialize(Colors.RGB(254,201,1),15)";
mostCurrent._mbg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (254),(int) (201),(int) (1)),(int) (15));
 //BA.debugLineNum = 120;BA.debugLine="obg.Initialize(Colors.RGB(237,27,36),15)";
mostCurrent._obg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (237),(int) (27),(int) (36)),(int) (15));
 //BA.debugLineNum = 121;BA.debugLine="mecbg.Initialize(Colors.RGB(0,137,123),15)";
mostCurrent._mecbg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (137),(int) (123)),(int) (15));
 //BA.debugLineNum = 123;BA.debugLine="bTelenor.Initialize(\"bTelenor\")";
mostCurrent._btelenor.Initialize(mostCurrent.activityBA,"bTelenor");
 //BA.debugLineNum = 124;BA.debugLine="bTelenor.Text = \"Telenor\"";
mostCurrent._btelenor.setText((Object)("Telenor"));
 //BA.debugLineNum = 125;BA.debugLine="bTelenor.Background = tbg";
mostCurrent._btelenor.setBackground((android.graphics.drawable.Drawable)(mostCurrent._tbg.getObject()));
 //BA.debugLineNum = 127;BA.debugLine="bMPT.Initialize(\"bMPT\")";
mostCurrent._bmpt.Initialize(mostCurrent.activityBA,"bMPT");
 //BA.debugLineNum = 128;BA.debugLine="bMPT.Text = \"MPT\"";
mostCurrent._bmpt.setText((Object)("MPT"));
 //BA.debugLineNum = 129;BA.debugLine="bMPT.Background = mbg";
mostCurrent._bmpt.setBackground((android.graphics.drawable.Drawable)(mostCurrent._mbg.getObject()));
 //BA.debugLineNum = 131;BA.debugLine="bOoredoo.Initialize(\"bOoredoo\")";
mostCurrent._booredoo.Initialize(mostCurrent.activityBA,"bOoredoo");
 //BA.debugLineNum = 132;BA.debugLine="bOoredoo.Text = \"Ooredoo\"";
mostCurrent._booredoo.setText((Object)("Ooredoo"));
 //BA.debugLineNum = 133;BA.debugLine="bOoredoo.Background = obg";
mostCurrent._booredoo.setBackground((android.graphics.drawable.Drawable)(mostCurrent._obg.getObject()));
 //BA.debugLineNum = 135;BA.debugLine="bMEC.Initialize(\"bMEC\")";
mostCurrent._bmec.Initialize(mostCurrent.activityBA,"bMEC");
 //BA.debugLineNum = 136;BA.debugLine="bMEC.Text = \"MecTel\"";
mostCurrent._bmec.setText((Object)("MecTel"));
 //BA.debugLineNum = 137;BA.debugLine="bMEC.Background = mecbg";
mostCurrent._bmec.setBackground((android.graphics.drawable.Drawable)(mostCurrent._mecbg.getObject()));
 //BA.debugLineNum = 139;BA.debugLine="Activity.AddView(bTelenor,15%x,(divBar1.Top+divBa";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._btelenor.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) ((_divbar1.getTop()+_divbar1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 140;BA.debugLine="Activity.AddView(bMPT,15%x,(bTelenor.Top+bTelenor";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._bmpt.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) ((mostCurrent._btelenor.getTop()+mostCurrent._btelenor.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 141;BA.debugLine="Activity.AddView(bOoredoo,15%x,(bMPT.Top+bMPT.Hei";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._booredoo.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) ((mostCurrent._bmpt.getTop()+mostCurrent._bmpt.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 142;BA.debugLine="Activity.AddView(bMEC,15%x,(bOoredoo.Top+bOoredoo";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._bmec.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) ((mostCurrent._booredoo.getTop()+mostCurrent._booredoo.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 143;BA.debugLine="Activity.AddView (lblFooter , 10dip,95%y, 100%x -";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfooter.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (95),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 145;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _answ = 0;
anywheresoftware.b4a.objects.IntentWrapper _facebook = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 269;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 270;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 271;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 272;BA.debugLine="Answ = Msgbox2(\"If you want to get new updates o";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2("If you want to get new updates on  Facebook? Please Like "+anywheresoftware.b4a.keywords.Common.CRLF+"Myanmar Android Apps Page!","Attention!","Yes","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"fb.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 273;BA.debugLine="If Answ = DialogResponse.NEGATIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 274;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 275;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 };
 //BA.debugLineNum = 278;BA.debugLine="If Answ = DialogResponse.POSITIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 279;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 280;BA.debugLine="Try";
try { //BA.debugLineNum = 282;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 284;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:/";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 285;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e16) {
			processBA.setLastException(e16); //BA.debugLineNum = 289;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 290;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.facebook";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 292;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 295;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 297;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 249;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 250;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 252;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 254;BA.debugLine="End Sub";
return "";
}
public static String  _adview2_adclosed() throws Exception{
 //BA.debugLineNum = 213;BA.debugLine="Sub AdView2_AdClosed";
 //BA.debugLineNum = 214;BA.debugLine="AdView2.LoadAd";
mostCurrent._adview2.LoadAd();
 //BA.debugLineNum = 215;BA.debugLine="End Sub";
return "";
}
public static String  _bmec_click() throws Exception{
 //BA.debugLineNum = 245;BA.debugLine="Sub bMEC_Click";
 //BA.debugLineNum = 246;BA.debugLine="StartActivity(MecTel)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._mectel.getObject()));
 //BA.debugLineNum = 247;BA.debugLine="End Sub";
return "";
}
public static String  _bmpt_click() throws Exception{
 //BA.debugLineNum = 235;BA.debugLine="Sub bMPT_Click";
 //BA.debugLineNum = 236;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 237;BA.debugLine="StartActivity(MPT)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._mpt.getObject()));
 //BA.debugLineNum = 238;BA.debugLine="End Sub";
return "";
}
public static String  _booredoo_click() throws Exception{
 //BA.debugLineNum = 240;BA.debugLine="Sub bOoredoo_CLick";
 //BA.debugLineNum = 241;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 242;BA.debugLine="StartActivity(Ooredoo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._ooredoo.getObject()));
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
return "";
}
public static String  _btelenor_click() throws Exception{
 //BA.debugLineNum = 230;BA.debugLine="Sub bTelenor_Click";
 //BA.debugLineNum = 231;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 232;BA.debugLine="StartActivity(Telenor)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._telenor.getObject()));
 //BA.debugLineNum = 233;BA.debugLine="End Sub";
return "";
}
public static String  _cso_click() throws Exception{
int _a = 0;
com.maximus.id.id _b = null;
anywheresoftware.b4a.objects.collections.List _li = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 151;BA.debugLine="Sub cso_Click";
 //BA.debugLineNum = 152;BA.debugLine="Dim a As Int";
_a = 0;
 //BA.debugLineNum = 153;BA.debugLine="Dim b As id";
_b = new com.maximus.id.id();
 //BA.debugLineNum = 154;BA.debugLine="Dim li As List";
_li = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 155;BA.debugLine="li.Initialize";
_li.Initialize();
 //BA.debugLineNum = 156;BA.debugLine="li.AddAll(Array As String(\"Call TELENOR Operator\"";
_li.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Call TELENOR Operator","Call MPT Operator","Call OOREDOO Operator","Call MECTEL Operator"}));
 //BA.debugLineNum = 157;BA.debugLine="a = b.InputList1(li,\"Choose Your Operator!\")";
_a = _b.InputList1(_li,"Choose Your Operator!",mostCurrent.activityBA);
 //BA.debugLineNum = 158;BA.debugLine="If a = 0 Then";
if (_a==0) { 
 //BA.debugLineNum = 159;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 160;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:979\")";
_i.Initialize(_i.ACTION_CALL,"tel:979");
 //BA.debugLineNum = 161;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 163;BA.debugLine="If a = 1 Then";
if (_a==1) { 
 //BA.debugLineNum = 164;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 165;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:106\")";
_i.Initialize(_i.ACTION_CALL,"tel:106");
 //BA.debugLineNum = 166;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 168;BA.debugLine="If a = 2 Then";
if (_a==2) { 
 //BA.debugLineNum = 169;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 170;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:234\")";
_i.Initialize(_i.ACTION_CALL,"tel:234");
 //BA.debugLineNum = 171;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 173;BA.debugLine="If a = 3 Then";
if (_a==3) { 
 //BA.debugLineNum = 174;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 175;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:01391814\")";
_i.Initialize(_i.ACTION_CALL,"tel:01391814");
 //BA.debugLineNum = 176;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return "";
}
public static String  _fb_click() throws Exception{
int _a = 0;
com.maximus.id.id _b = null;
anywheresoftware.b4a.objects.collections.List _li = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 180;BA.debugLine="Sub fb_Click";
 //BA.debugLineNum = 181;BA.debugLine="Dim a As Int";
_a = 0;
 //BA.debugLineNum = 182;BA.debugLine="Dim b As id";
_b = new com.maximus.id.id();
 //BA.debugLineNum = 183;BA.debugLine="Dim li As List";
_li = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 184;BA.debugLine="li.Initialize";
_li.Initialize();
 //BA.debugLineNum = 185;BA.debugLine="li.AddAll(Array As String(\"Call TELENOR Operator\"";
_li.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Call TELENOR Operator","Call MPT Operator","Call OOREDOO Operator","Call MECTEL Operator"}));
 //BA.debugLineNum = 186;BA.debugLine="a = b.InputList1(li,\"Choose Your Operator!\")";
_a = _b.InputList1(_li,"Choose Your Operator!",mostCurrent.activityBA);
 //BA.debugLineNum = 187;BA.debugLine="If a = 0 Then";
if (_a==0) { 
 //BA.debugLineNum = 188;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 189;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:979\")";
_i.Initialize(_i.ACTION_CALL,"tel:979");
 //BA.debugLineNum = 190;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 192;BA.debugLine="If a = 1 Then";
if (_a==1) { 
 //BA.debugLineNum = 193;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 194;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:106\")";
_i.Initialize(_i.ACTION_CALL,"tel:106");
 //BA.debugLineNum = 195;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 197;BA.debugLine="If a = 2 Then";
if (_a==2) { 
 //BA.debugLineNum = 198;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 199;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:234\")";
_i.Initialize(_i.ACTION_CALL,"tel:234");
 //BA.debugLineNum = 200;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 202;BA.debugLine="If a = 3 Then";
if (_a==3) { 
 //BA.debugLineNum = 203;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 204;BA.debugLine="i.Initialize(i.ACTION_Call, \"tel:01391814\")";
_i.Initialize(_i.ACTION_CALL,"tel:01391814");
 //BA.debugLineNum = 205;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return "";
}
public static String  _fb1_click() throws Exception{
 //BA.debugLineNum = 209;BA.debugLine="Sub fb1_Click";
 //BA.debugLineNum = 210;BA.debugLine="StartActivity(lollipop)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._lollipop.getObject()));
 //BA.debugLineNum = 211;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Dim fb,fb1 As FloatingActionButton";
mostCurrent._fb = new de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper();
mostCurrent._fb1 = new de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim copy As BClipboard";
mostCurrent._copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 26;BA.debugLine="Dim bTelenor, bMPT, bOoredoo,bMEC As Button";
mostCurrent._btelenor = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._bmpt = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._booredoo = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._bmec = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim res As XmlLayoutBuilder";
mostCurrent._res = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 28;BA.debugLine="Dim tbg As ColorDrawable";
mostCurrent._tbg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 29;BA.debugLine="Dim mbg As ColorDrawable";
mostCurrent._mbg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 30;BA.debugLine="Dim obg As ColorDrawable";
mostCurrent._obg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 31;BA.debugLine="Dim mecbg As ColorDrawable";
mostCurrent._mecbg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 32;BA.debugLine="Dim lblTitle,lblFooter As Label";
mostCurrent._lbltitle = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblfooter = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Dim su As StringUtils";
mostCurrent._su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 34;BA.debugLine="Dim scr As Phone";
mostCurrent._scr = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 35;BA.debugLine="Dim AdView1 As AdView";
mostCurrent._adview1 = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Dim AdView2 As InterstitialAd";
mostCurrent._adview2 = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim ph As Phone";
mostCurrent._ph = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 39;BA.debugLine="Dim tlb As Label";
mostCurrent._tlb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Dim menu,share As Button";
mostCurrent._menu = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._share = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim mmbg,sbg As BitmapDrawable";
mostCurrent._mmbg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._sbg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 42;BA.debugLine="End Sub";
return "";
}
public static String  _lblfooter_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _facebook = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 256;BA.debugLine="Sub lblFooter_Click";
 //BA.debugLineNum = 257;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 258;BA.debugLine="Try";
try { //BA.debugLineNum = 259;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 260;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb://";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 261;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e7) {
			processBA.setLastException(e7); //BA.debugLineNum = 263;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 264;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.facebook.";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 265;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 267;BA.debugLine="End Sub";
return "";
}
public static String  _menu_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _lis = null;
int _idd_int = 0;
com.maximus.id.id _idd = null;
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
int _a = 0;
com.maximus.id.id _b = null;
anywheresoftware.b4a.objects.collections.List _li = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 299;BA.debugLine="Sub menu_Click";
 //BA.debugLineNum = 300;BA.debugLine="Dim lis As List";
_lis = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 301;BA.debugLine="Dim idd_int As Int";
_idd_int = 0;
 //BA.debugLineNum = 302;BA.debugLine="Dim idd As id";
_idd = new com.maximus.id.id();
 //BA.debugLineNum = 303;BA.debugLine="lis.Initialize";
_lis.Initialize();
 //BA.debugLineNum = 304;BA.debugLine="lis.AddAll(Array As String(\"Stop Ad Showing\",\"How";
_lis.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Stop Ad Showing","How to use ?","Call to Operator!","About"}));
 //BA.debugLineNum = 305;BA.debugLine="idd_int = idd.InputList1(lis,\"Choose!\")";
_idd_int = _idd.InputList1(_lis,"Choose!",mostCurrent.activityBA);
 //BA.debugLineNum = 307;BA.debugLine="If idd_int = 0 Then";
if (_idd_int==0) { 
 //BA.debugLineNum = 308;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 309;BA.debugLine="t1.Enabled = False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 312;BA.debugLine="If idd_int = 1 Then";
if (_idd_int==1) { 
 //BA.debugLineNum = 313;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 314;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 315;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.htetznai";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_p.OpenBrowser("http://www.htetznaing.com/myanmar-all-sim-register")));
 };
 //BA.debugLineNum = 318;BA.debugLine="If idd_int = 2 Then";
if (_idd_int==2) { 
 //BA.debugLineNum = 319;BA.debugLine="Dim a As Int";
_a = 0;
 //BA.debugLineNum = 320;BA.debugLine="Dim b As id";
_b = new com.maximus.id.id();
 //BA.debugLineNum = 321;BA.debugLine="Dim li As List";
_li = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 322;BA.debugLine="li.Initialize";
_li.Initialize();
 //BA.debugLineNum = 323;BA.debugLine="li.AddAll(Array As String(\"Call TELENOR Operator";
_li.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Call TELENOR Operator","Call MPT Operator","Call OOREDOO Operator","Call MECTEL Operator"}));
 //BA.debugLineNum = 324;BA.debugLine="a = b.InputList1(li,\"Choose Your Operator!\")";
_a = _b.InputList1(_li,"Choose Your Operator!",mostCurrent.activityBA);
 //BA.debugLineNum = 325;BA.debugLine="If a = 0 Then";
if (_a==0) { 
 //BA.debugLineNum = 326;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 327;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:979\")";
_i.Initialize(_i.ACTION_CALL,"tel:979");
 //BA.debugLineNum = 328;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 330;BA.debugLine="If a = 1 Then";
if (_a==1) { 
 //BA.debugLineNum = 331;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 332;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:106\")";
_i.Initialize(_i.ACTION_CALL,"tel:106");
 //BA.debugLineNum = 333;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 335;BA.debugLine="If a = 2 Then";
if (_a==2) { 
 //BA.debugLineNum = 336;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 337;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:234\")";
_i.Initialize(_i.ACTION_CALL,"tel:234");
 //BA.debugLineNum = 338;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 340;BA.debugLine="If a = 3 Then";
if (_a==3) { 
 //BA.debugLineNum = 341;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 342;BA.debugLine="i.Initialize(i.ACTION_CALL, \"tel:01391814\")";
_i.Initialize(_i.ACTION_CALL,"tel:01391814");
 //BA.debugLineNum = 343;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 };
 //BA.debugLineNum = 347;BA.debugLine="If idd_int = 3 Then";
if (_idd_int==3) { 
 //BA.debugLineNum = 348;BA.debugLine="StartActivity(lollipop)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._lollipop.getObject()));
 };
 //BA.debugLineNum = 350;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
runads._process_globals();
ads._process_globals();
lollipop._process_globals();
tutorial._process_globals();
telenor._process_globals();
mpt._process_globals();
ooredoo._process_globals();
mectel._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 20;BA.debugLine="Dim t,t1 As Timer";
_t = new anywheresoftware.b4a.objects.Timer();
_t1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _share_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
 //BA.debugLineNum = 352;BA.debugLine="Sub share_Click";
 //BA.debugLineNum = 353;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 354;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 355;BA.debugLine="copy.setText(\"#Myanmar_All_Sim_Register App! Regi";
mostCurrent._copy.setText(mostCurrent.activityBA,"#Myanmar_All_Sim_Register App! Registration you sim Free with this app. You can register all Myanmar sim Card, MPT, Telenor, Ooredoo, MecTel. Very simple useful App. Download Free at : http://www.htetznaing.com/myanmar-all-sim-register/");
 //BA.debugLineNum = 356;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 357;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 358;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",cop";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(mostCurrent._copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 359;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT\",";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("Get Free!!"));
 //BA.debugLineNum = 360;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 361;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 362;BA.debugLine="End Sub";
return "";
}
public static String  _t_tick() throws Exception{
 //BA.debugLineNum = 224;BA.debugLine="Sub t_tick";
 //BA.debugLineNum = 225;BA.debugLine="If AdView2.Ready Then AdView2.Show";
if (mostCurrent._adview2.getReady()) { 
mostCurrent._adview2.Show();};
 //BA.debugLineNum = 227;BA.debugLine="If AdView2.Ready = False Then AdView2.LoadAd";
if (mostCurrent._adview2.getReady()==anywheresoftware.b4a.keywords.Common.False) { 
mostCurrent._adview2.LoadAd();};
 //BA.debugLineNum = 228;BA.debugLine="End Sub";
return "";
}
public static String  _t1_tick() throws Exception{
 //BA.debugLineNum = 217;BA.debugLine="Sub t1_tick";
 //BA.debugLineNum = 218;BA.debugLine="If AdView2.Ready Then AdView2.Show";
if (mostCurrent._adview2.getReady()) { 
mostCurrent._adview2.Show();};
 //BA.debugLineNum = 220;BA.debugLine="If AdView2.Ready = False Then AdView2.LoadAd";
if (mostCurrent._adview2.getReady()==anywheresoftware.b4a.keywords.Common.False) { 
mostCurrent._adview2.LoadAd();};
 //BA.debugLineNum = 221;BA.debugLine="t1.Enabled = False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 222;BA.debugLine="End Sub";
return "";
}
}
