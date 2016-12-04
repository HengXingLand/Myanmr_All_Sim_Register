package com.htetznaing.mmallsimregistration;


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
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.mmallsimregistration", "com.htetznaing.mmallsimregistration.main");
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
		activityBA = new BA(this, layout, processBA, "com.htetznaing.mmallsimregistration", "com.htetznaing.mmallsimregistration.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.mmallsimregistration.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public static int _theme_value = 0;
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
public mobi.mindware.admob.interstitial.AdmobInterstitialsAds _adview2 = null;
public anywheresoftware.b4a.phone.Phone _ph = null;
public anywheresoftware.b4a.objects.ButtonWrapper _bstart = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _bsbg = null;
public anywheresoftware.b4a.objects.WebViewWrapper _wv = null;
public com.htetznaing.mmallsimregistration.runads _runads = null;
public com.htetznaing.mmallsimregistration.ads _ads = null;
public com.htetznaing.mmallsimregistration.lollipop _lollipop = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (ads.mostCurrent != null);
vis = vis | (lollipop.mostCurrent != null);
return vis;}
public static String  _about_click() throws Exception{
 //BA.debugLineNum = 180;BA.debugLine="Sub about_Click";
 //BA.debugLineNum = 181;BA.debugLine="StartActivity(lollipop)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._lollipop.getObject()));
 //BA.debugLineNum = 182;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imvlogo = null;
anywheresoftware.b4a.objects.PanelWrapper _divbar = null;
anywheresoftware.b4a.objects.PanelWrapper _divbar1 = null;
 //BA.debugLineNum = 45;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 46;BA.debugLine="Activity.LoadLayout(\"Lay1\")";
mostCurrent._activity.LoadLayout("Lay1",mostCurrent.activityBA);
 //BA.debugLineNum = 47;BA.debugLine="fb.Icon = res.GetDrawable(\"ic_add_white_24dp\")";
mostCurrent._fb.setIcon(mostCurrent._res.GetDrawable("ic_add_white_24dp"));
 //BA.debugLineNum = 48;BA.debugLine="fb.HideOffset = 100%y - fb.Top";
mostCurrent._fb.setHideOffset((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-mostCurrent._fb.getTop()));
 //BA.debugLineNum = 49;BA.debugLine="fb.Hide(False)";
mostCurrent._fb.Hide(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 50;BA.debugLine="fb.Show(True)";
mostCurrent._fb.Show(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 52;BA.debugLine="fb1.Icon = res.GetDrawable(\"about\")";
mostCurrent._fb1.setIcon(mostCurrent._res.GetDrawable("about"));
 //BA.debugLineNum = 53;BA.debugLine="fb1.HideOffset = 100%y - fb.Top";
mostCurrent._fb1.setHideOffset((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-mostCurrent._fb.getTop()));
 //BA.debugLineNum = 54;BA.debugLine="fb1.Hide(False)";
mostCurrent._fb1.Hide(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 55;BA.debugLine="fb1.Show(True)";
mostCurrent._fb1.Show(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 57;BA.debugLine="scr.SetScreenOrientation(1)";
mostCurrent._scr.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 58;BA.debugLine="Activity.Title = \"All Sim Registration!\"";
mostCurrent._activity.setTitle((Object)("All Sim Registration!"));
 //BA.debugLineNum = 60;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 61;BA.debugLine="StartService(RunAds)";
anywheresoftware.b4a.keywords.Common.StartService(mostCurrent.activityBA,(Object)(mostCurrent._runads.getObject()));
 //BA.debugLineNum = 62;BA.debugLine="AdView1.Initialize(\"AdView1\",\"ca-app-pub-41733485";
mostCurrent._adview1.Initialize(mostCurrent.activityBA,"AdView1","ca-app-pub-4173348573252986/7394508954");
 //BA.debugLineNum = 63;BA.debugLine="AdView1.LoadAd";
mostCurrent._adview1.LoadAd();
 //BA.debugLineNum = 65;BA.debugLine="AdView2.Initialize(\"AdView2\",\"ca-app-pub-41733485";
mostCurrent._adview2.Initialize(mostCurrent.activityBA,"AdView2","ca-app-pub-4173348573252986/8871242155");
 //BA.debugLineNum = 66;BA.debugLine="AdView2.LoadAd";
mostCurrent._adview2.LoadAd(mostCurrent.activityBA);
 //BA.debugLineNum = 68;BA.debugLine="t.Initialize(\"t\",15000)";
_t.Initialize(processBA,"t",(long) (15000));
 //BA.debugLineNum = 69;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 70;BA.debugLine="t1.Initialize(\"t1\",100)";
_t1.Initialize(processBA,"t1",(long) (100));
 //BA.debugLineNum = 71;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 75;BA.debugLine="Dim imvLogo As ImageView";
_imvlogo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 76;BA.debugLine="imvLogo.Initialize (\"\")";
_imvlogo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 77;BA.debugLine="imvLogo.Bitmap = LoadBitmap(File.DirAssets , \"ico";
_imvlogo.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"icon.png").getObject()));
 //BA.debugLineNum = 78;BA.debugLine="imvLogo.Gravity = Gravity.FILL";
_imvlogo.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 79;BA.debugLine="Activity.AddView ( imvLogo , 50%x - 50dip  , 10di";
mostCurrent._activity.AddView((android.view.View)(_imvlogo.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 81;BA.debugLine="Dim divBar,divBar1 As Panel";
_divbar = new anywheresoftware.b4a.objects.PanelWrapper();
_divbar1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="divBar.Initialize (\"\")";
_divbar.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 83;BA.debugLine="divBar.Color = Colors.LightGray";
_divbar.setColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 84;BA.debugLine="Activity.AddView (divBar , 18dip , imvLogo.Height";
mostCurrent._activity.AddView((android.view.View)(_divbar.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (18)),(int) (_imvlogo.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25))),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (36))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 86;BA.debugLine="lblTitle.Initialize (\"\")";
mostCurrent._lbltitle.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 87;BA.debugLine="lblTitle.Text = \"Myanmar All Sim Register!\"";
mostCurrent._lbltitle.setText((Object)("Myanmar All Sim Register!"));
 //BA.debugLineNum = 88;BA.debugLine="lblTitle.TextColor = Colors.RGB(0,136,82)";
mostCurrent._lbltitle.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (136),(int) (82)));
 //BA.debugLineNum = 89;BA.debugLine="lblTitle.TextSize = 20";
mostCurrent._lbltitle.setTextSize((float) (20));
 //BA.debugLineNum = 90;BA.debugLine="lblTitle.Gravity = Gravity.CENTER";
mostCurrent._lbltitle.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 91;BA.debugLine="Activity.AddView (lblTitle, 10dip , (divBar.Top+d";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lbltitle.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) ((_divbar.getTop()+_divbar.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 92;BA.debugLine="lblTitle.Height = su.MeasureMultilineTextHeight (";
mostCurrent._lbltitle.setHeight(mostCurrent._su.MeasureMultilineTextHeight((android.widget.TextView)(mostCurrent._lbltitle.getObject()),mostCurrent._lbltitle.getText()));
 //BA.debugLineNum = 94;BA.debugLine="lblFooter.Initialize (\"lblFooter\")";
mostCurrent._lblfooter.Initialize(mostCurrent.activityBA,"lblFooter");
 //BA.debugLineNum = 95;BA.debugLine="lblFooter.Text = \"Developed By Khun Htetz Naing\"";
mostCurrent._lblfooter.setText((Object)("Developed By Khun Htetz Naing"));
 //BA.debugLineNum = 96;BA.debugLine="lblFooter.TextColor = Colors.RGB(156,39,176)";
mostCurrent._lblfooter.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (156),(int) (39),(int) (176)));
 //BA.debugLineNum = 97;BA.debugLine="lblFooter.TextSize = 12";
mostCurrent._lblfooter.setTextSize((float) (12));
 //BA.debugLineNum = 98;BA.debugLine="lblFooter.Gravity = Gravity.CENTER";
mostCurrent._lblfooter.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 100;BA.debugLine="divBar1.Initialize (\"\")";
_divbar1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 101;BA.debugLine="divBar1.Color = Colors.LightGray";
_divbar1.setColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 102;BA.debugLine="Activity.AddView (divBar1 , 18dip , (lblTitle.Hei";
mostCurrent._activity.AddView((android.view.View)(_divbar1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (18)),(int) ((mostCurrent._lbltitle.getHeight()+mostCurrent._lbltitle.getTop())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (36))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 104;BA.debugLine="tbg.Initialize(Colors.RGB(11,149,211),15)";
mostCurrent._tbg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (11),(int) (149),(int) (211)),(int) (15));
 //BA.debugLineNum = 105;BA.debugLine="mbg.Initialize(Colors.RGB(254,201,1),15)";
mostCurrent._mbg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (254),(int) (201),(int) (1)),(int) (15));
 //BA.debugLineNum = 106;BA.debugLine="obg.Initialize(Colors.RGB(237,27,36),15)";
mostCurrent._obg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (237),(int) (27),(int) (36)),(int) (15));
 //BA.debugLineNum = 107;BA.debugLine="mecbg.Initialize(Colors.RGB(0,137,123),15)";
mostCurrent._mecbg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (137),(int) (123)),(int) (15));
 //BA.debugLineNum = 109;BA.debugLine="bTelenor.Initialize(\"bTelenor\")";
mostCurrent._btelenor.Initialize(mostCurrent.activityBA,"bTelenor");
 //BA.debugLineNum = 110;BA.debugLine="bTelenor.Text = \"Telenor\"";
mostCurrent._btelenor.setText((Object)("Telenor"));
 //BA.debugLineNum = 111;BA.debugLine="bTelenor.Background = tbg";
mostCurrent._btelenor.setBackground((android.graphics.drawable.Drawable)(mostCurrent._tbg.getObject()));
 //BA.debugLineNum = 113;BA.debugLine="bMPT.Initialize(\"bMPT\")";
mostCurrent._bmpt.Initialize(mostCurrent.activityBA,"bMPT");
 //BA.debugLineNum = 114;BA.debugLine="bMPT.Text = \"MPT\"";
mostCurrent._bmpt.setText((Object)("MPT"));
 //BA.debugLineNum = 115;BA.debugLine="bMPT.Background = mbg";
mostCurrent._bmpt.setBackground((android.graphics.drawable.Drawable)(mostCurrent._mbg.getObject()));
 //BA.debugLineNum = 117;BA.debugLine="bOoredoo.Initialize(\"bOoredoo\")";
mostCurrent._booredoo.Initialize(mostCurrent.activityBA,"bOoredoo");
 //BA.debugLineNum = 118;BA.debugLine="bOoredoo.Text = \"Ooredoo\"";
mostCurrent._booredoo.setText((Object)("Ooredoo"));
 //BA.debugLineNum = 119;BA.debugLine="bOoredoo.Background = obg";
mostCurrent._booredoo.setBackground((android.graphics.drawable.Drawable)(mostCurrent._obg.getObject()));
 //BA.debugLineNum = 121;BA.debugLine="bMEC.Initialize(\"bMEC\")";
mostCurrent._bmec.Initialize(mostCurrent.activityBA,"bMEC");
 //BA.debugLineNum = 122;BA.debugLine="bMEC.Text = \"MecTel\"";
mostCurrent._bmec.setText((Object)("MecTel"));
 //BA.debugLineNum = 123;BA.debugLine="bMEC.Background = mecbg";
mostCurrent._bmec.setBackground((android.graphics.drawable.Drawable)(mostCurrent._mecbg.getObject()));
 //BA.debugLineNum = 125;BA.debugLine="Activity.AddView(bTelenor,15%x,(divBar1.Top+divBa";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._btelenor.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) ((_divbar1.getTop()+_divbar1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 126;BA.debugLine="Activity.AddView(bMPT,15%x,(bTelenor.Top+bTelenor";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._bmpt.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) ((mostCurrent._btelenor.getTop()+mostCurrent._btelenor.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 127;BA.debugLine="Activity.AddView(bOoredoo,15%x,(bMPT.Top+bMPT.Hei";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._booredoo.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) ((mostCurrent._bmpt.getTop()+mostCurrent._bmpt.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 128;BA.debugLine="Activity.AddView(AdView1, 0%x,85%y, 100%x, 10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._adview1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (85),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 129;BA.debugLine="Activity.AddView(bMEC,15%x,(bOoredoo.Top+bOoredoo";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._bmec.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) ((mostCurrent._booredoo.getTop()+mostCurrent._booredoo.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 130;BA.debugLine="Activity.AddView (lblFooter , 10dip,95%y, 100%x -";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfooter.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (95),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 132;BA.debugLine="Activity.AddMenuItem(\"Stop Showing Ads!\",\"rad\")";
mostCurrent._activity.AddMenuItem("Stop Showing Ads!","rad");
 //BA.debugLineNum = 133;BA.debugLine="Activity.AddMenuItem(\"Change Theme\",\"ct\")";
mostCurrent._activity.AddMenuItem("Change Theme","ct");
 //BA.debugLineNum = 134;BA.debugLine="Activity.AddMenuItem3(\"Share App\",\"share\",LoadBit";
mostCurrent._activity.AddMenuItem3("Share App","share",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png").getObject()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 135;BA.debugLine="Activity.AddMenuItem(\"How to Use.?\",\"help\")";
mostCurrent._activity.AddMenuItem("How to Use.?","help");
 //BA.debugLineNum = 136;BA.debugLine="Activity.AddMenuItem(\"About\",\"about\")";
mostCurrent._activity.AddMenuItem("About","about");
 //BA.debugLineNum = 137;BA.debugLine="Activity.AddMenuItem(\"Call SIM Operator\",\"cso\")";
mostCurrent._activity.AddMenuItem("Call SIM Operator","cso");
 //BA.debugLineNum = 139;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 140;BA.debugLine="wv.Initialize(\"wv\")";
mostCurrent._wv.Initialize(mostCurrent.activityBA,"wv");
 //BA.debugLineNum = 141;BA.debugLine="wv.LoadUrl(\"file:///android_asset/sp.html\")";
mostCurrent._wv.LoadUrl("file:///android_asset/sp.html");
 //BA.debugLineNum = 142;BA.debugLine="wv.Visible=True";
mostCurrent._wv.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 143;BA.debugLine="wv.ZoomEnabled = False";
mostCurrent._wv.setZoomEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 145;BA.debugLine="bsbg.Initialize(Colors.Blue,15)";
mostCurrent._bsbg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Blue,(int) (15));
 //BA.debugLineNum = 146;BA.debugLine="bstart.Initialize(\"bstart\")";
mostCurrent._bstart.Initialize(mostCurrent.activityBA,"bstart");
 //BA.debugLineNum = 147;BA.debugLine="bstart.Text = \">> Start >>\"";
mostCurrent._bstart.setText((Object)(">> Start >>"));
 //BA.debugLineNum = 148;BA.debugLine="bstart.Background = bsbg";
mostCurrent._bstart.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bsbg.getObject()));
 //BA.debugLineNum = 149;BA.debugLine="fb.Hide(True)";
mostCurrent._fb.Hide(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 150;BA.debugLine="fb1.Hide(True)";
mostCurrent._fb1.Hide(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 151;BA.debugLine="Activity.AddView(wv,0%x,0%y,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._wv.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 152;BA.debugLine="Activity.AddView(bstart,60%x,87%y,35%x,8%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._bstart.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (87),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (35),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 154;BA.debugLine="bTelenor.Visible = False";
mostCurrent._btelenor.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 155;BA.debugLine="bMPT.Visible = False";
mostCurrent._bmpt.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 156;BA.debugLine="bOoredoo.Visible = False";
mostCurrent._booredoo.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 157;BA.debugLine="bMEC.Visible = False";
mostCurrent._bmec.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 159;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _answ = 0;
 //BA.debugLineNum = 434;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 435;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 436;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 437;BA.debugLine="Answ = Msgbox2(\"Do you want to Exit App?\", \"At";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2("Do you want to Exit App?","Attention!","Yes","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 438;BA.debugLine="If Answ = DialogResponse.NEGATIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 439;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 440;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 443;BA.debugLine="If Answ = DialogResponse.POSITIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 444;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 445;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 447;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 400;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 401;BA.debugLine="AdView1.Pause";
mostCurrent._adview1.Pause();
 //BA.debugLineNum = 402;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 404;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 405;BA.debugLine="AdView1.Resume";
mostCurrent._adview1.Resume();
 //BA.debugLineNum = 406;BA.debugLine="End Sub";
return "";
}
public static String  _ad_adscreendismissed() throws Exception{
 //BA.debugLineNum = 416;BA.debugLine="Sub Ad_AdScreenDismissed";
 //BA.debugLineNum = 417;BA.debugLine="Log(\"screen dismissed\")";
anywheresoftware.b4a.keywords.Common.Log("screen dismissed");
 //BA.debugLineNum = 418;BA.debugLine="End Sub";
return "";
}
public static String  _ad_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 408;BA.debugLine="Sub Ad_FailedToReceiveAd (ErrorCode As String)";
 //BA.debugLineNum = 409;BA.debugLine="Log(\"failed: \" & ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("failed: "+_errorcode);
 //BA.debugLineNum = 410;BA.debugLine="End Sub";
return "";
}
public static String  _ad_receivead() throws Exception{
 //BA.debugLineNum = 412;BA.debugLine="Sub Ad_ReceiveAd";
 //BA.debugLineNum = 413;BA.debugLine="Log(\"received\")";
anywheresoftware.b4a.keywords.Common.Log("received");
 //BA.debugLineNum = 414;BA.debugLine="End Sub";
return "";
}
public static String  _bmec_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
 //BA.debugLineNum = 324;BA.debugLine="Sub bMEC_Click";
 //BA.debugLineNum = 325;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 326;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 327;BA.debugLine="StartActivity(p.OpenBrowser(\"http://reg.mectel.co";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_p.OpenBrowser("http://reg.mectel.com.mm/")));
 //BA.debugLineNum = 328;BA.debugLine="End Sub";
return "";
}
public static String  _bmec_longclick() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 330;BA.debugLine="Sub bMEC_LongClick";
 //BA.debugLineNum = 331;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 332;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:01391814\")";
_i.Initialize(_i.ACTION_VIEW,"tel:01391814");
 //BA.debugLineNum = 333;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 334;BA.debugLine="End Sub";
return "";
}
public static String  _bmpt_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
 //BA.debugLineNum = 300;BA.debugLine="Sub bMPT_Click";
 //BA.debugLineNum = 301;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 302;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 303;BA.debugLine="StartActivity(p.OpenBrowser(\"https://care.mpt.com";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_p.OpenBrowser("https://care.mpt.com.mm/")));
 //BA.debugLineNum = 304;BA.debugLine="End Sub";
return "";
}
public static String  _bmpt_longclick() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 306;BA.debugLine="Sub bMPT_LongClick";
 //BA.debugLineNum = 307;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 308;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:106\")";
_i.Initialize(_i.ACTION_VIEW,"tel:106");
 //BA.debugLineNum = 309;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 310;BA.debugLine="End Sub";
return "";
}
public static String  _booredoo_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
 //BA.debugLineNum = 312;BA.debugLine="Sub bOoredoo_CLick";
 //BA.debugLineNum = 313;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 314;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 315;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.ooredoo.c";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_p.OpenBrowser("http://www.ooredoo.com.mm/en/Personal/SelfCare/SIM-Registration.aspx")));
 //BA.debugLineNum = 316;BA.debugLine="End Sub";
return "";
}
public static String  _booredoo_longclick() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 318;BA.debugLine="Sub bOoredoo_LongClick";
 //BA.debugLineNum = 319;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 320;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:234\")";
_i.Initialize(_i.ACTION_VIEW,"tel:234");
 //BA.debugLineNum = 321;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 322;BA.debugLine="End Sub";
return "";
}
public static String  _bstart_click() throws Exception{
 //BA.debugLineNum = 161;BA.debugLine="Sub bstart_Click";
 //BA.debugLineNum = 162;BA.debugLine="wv.Visible = False";
mostCurrent._wv.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 163;BA.debugLine="bstart.Visible = False";
mostCurrent._bstart.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 164;BA.debugLine="fb.Hide(False)";
mostCurrent._fb.Hide(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 165;BA.debugLine="fb1.Hide(False)";
mostCurrent._fb1.Hide(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 166;BA.debugLine="fb.Show(True)";
mostCurrent._fb.Show(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 167;BA.debugLine="fb1.Show(True)";
mostCurrent._fb1.Show(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 169;BA.debugLine="bTelenor.Visible = True";
mostCurrent._btelenor.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 170;BA.debugLine="bMPT.Visible = True";
mostCurrent._bmpt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 171;BA.debugLine="bOoredoo.Visible = True";
mostCurrent._booredoo.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 172;BA.debugLine="bMEC.Visible = True";
mostCurrent._bmec.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _btelenor_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
 //BA.debugLineNum = 288;BA.debugLine="Sub bTelenor_Click";
 //BA.debugLineNum = 289;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 290;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 291;BA.debugLine="StartActivity(p.OpenBrowser(\"https://ecaf.telenor";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_p.OpenBrowser("https://ecaf.telenor.com.mm/")));
 //BA.debugLineNum = 292;BA.debugLine="End Sub";
return "";
}
public static String  _btelenor_longclick() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 294;BA.debugLine="Sub bTelenor_LongClick";
 //BA.debugLineNum = 295;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 296;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:979\")";
_i.Initialize(_i.ACTION_VIEW,"tel:979");
 //BA.debugLineNum = 297;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 298;BA.debugLine="End Sub";
return "";
}
public static String  _cso_click() throws Exception{
int _a = 0;
com.maximus.id.id _b = null;
anywheresoftware.b4a.objects.collections.List _li = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 184;BA.debugLine="Sub cso_Click";
 //BA.debugLineNum = 185;BA.debugLine="Dim a As Int";
_a = 0;
 //BA.debugLineNum = 186;BA.debugLine="Dim b As id";
_b = new com.maximus.id.id();
 //BA.debugLineNum = 187;BA.debugLine="Dim li As List";
_li = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 188;BA.debugLine="li.Initialize";
_li.Initialize();
 //BA.debugLineNum = 189;BA.debugLine="li.AddAll(Array As String(\"Call TELENOR Operator\"";
_li.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Call TELENOR Operator","Call MPT Operator","Call OOREDOO Operator","Call MECTEL Operator"}));
 //BA.debugLineNum = 190;BA.debugLine="a = b.InputList1(li,\"Choose Your Operator!\")";
_a = _b.InputList1(_li,"Choose Your Operator!",mostCurrent.activityBA);
 //BA.debugLineNum = 191;BA.debugLine="If a = 0 Then";
if (_a==0) { 
 //BA.debugLineNum = 192;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 193;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:979\")";
_i.Initialize(_i.ACTION_VIEW,"tel:979");
 //BA.debugLineNum = 194;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 196;BA.debugLine="If a = 1 Then";
if (_a==1) { 
 //BA.debugLineNum = 197;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 198;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:106\")";
_i.Initialize(_i.ACTION_VIEW,"tel:106");
 //BA.debugLineNum = 199;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 201;BA.debugLine="If a = 2 Then";
if (_a==2) { 
 //BA.debugLineNum = 202;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 203;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:234\")";
_i.Initialize(_i.ACTION_VIEW,"tel:234");
 //BA.debugLineNum = 204;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 206;BA.debugLine="If a = 3 Then";
if (_a==3) { 
 //BA.debugLineNum = 207;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 208;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:01391814\")";
_i.Initialize(_i.ACTION_VIEW,"tel:01391814");
 //BA.debugLineNum = 209;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 211;BA.debugLine="End Sub";
return "";
}
public static String  _ct_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _lis = null;
int _idd_int = 0;
com.maximus.id.id _idd = null;
 //BA.debugLineNum = 336;BA.debugLine="Sub ct_Click";
 //BA.debugLineNum = 337;BA.debugLine="Dim lis As List";
_lis = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 338;BA.debugLine="Dim idd_int As Int";
_idd_int = 0;
 //BA.debugLineNum = 339;BA.debugLine="Dim idd As id";
_idd = new com.maximus.id.id();
 //BA.debugLineNum = 340;BA.debugLine="lis.Initialize";
_lis.Initialize();
 //BA.debugLineNum = 341;BA.debugLine="lis.AddAll(Array As String(\"Holo\",\"Holo Light\",\"H";
_lis.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Holo","Holo Light","Holo Light Dark","Old Android","Material","Material Light","Material Light Dark","Transparent","Transparent No Title Bar"}));
 //BA.debugLineNum = 342;BA.debugLine="idd_int = idd.InputList1(lis,\"Choose Themes!\")";
_idd_int = _idd.InputList1(_lis,"Choose Themes!",mostCurrent.activityBA);
 //BA.debugLineNum = 343;BA.debugLine="If idd_int = 0 Then";
if (_idd_int==0) { 
 //BA.debugLineNum = 344;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo"));
 };
 //BA.debugLineNum = 347;BA.debugLine="If idd_int = 1 Then";
if (_idd_int==1) { 
 //BA.debugLineNum = 348;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo.Light"));
 };
 //BA.debugLineNum = 351;BA.debugLine="If idd_int = 2 Then";
if (_idd_int==2) { 
 //BA.debugLineNum = 352;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo.Light.DarkActionBar"));
 };
 //BA.debugLineNum = 355;BA.debugLine="If idd_int = 3 Then";
if (_idd_int==3) { 
 //BA.debugLineNum = 356;BA.debugLine="SetTheme(16973829)";
_settheme((int) (16973829));
 };
 //BA.debugLineNum = 359;BA.debugLine="If idd_int = 4 Then";
if (_idd_int==4) { 
 //BA.debugLineNum = 360;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material"));
 };
 //BA.debugLineNum = 363;BA.debugLine="If idd_int = 5 Then";
if (_idd_int==5) { 
 //BA.debugLineNum = 364;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material.Light"));
 };
 //BA.debugLineNum = 367;BA.debugLine="If idd_int = 6 Then";
if (_idd_int==6) { 
 //BA.debugLineNum = 368;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material.Light.DarkActionBar"));
 };
 //BA.debugLineNum = 371;BA.debugLine="If idd_int = 7 Then";
if (_idd_int==7) { 
 //BA.debugLineNum = 372;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:styl";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Translucent"));
 };
 //BA.debugLineNum = 375;BA.debugLine="If idd_int = 8 Then";
if (_idd_int==8) { 
 //BA.debugLineNum = 376;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:styl";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Translucent.NoTitleBar"));
 };
 //BA.debugLineNum = 379;BA.debugLine="End Sub";
return "";
}
public static String  _fb_click() throws Exception{
int _a = 0;
com.maximus.id.id _b = null;
anywheresoftware.b4a.objects.collections.List _li = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 213;BA.debugLine="Sub fb_Click";
 //BA.debugLineNum = 214;BA.debugLine="Dim a As Int";
_a = 0;
 //BA.debugLineNum = 215;BA.debugLine="Dim b As id";
_b = new com.maximus.id.id();
 //BA.debugLineNum = 216;BA.debugLine="Dim li As List";
_li = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 217;BA.debugLine="li.Initialize";
_li.Initialize();
 //BA.debugLineNum = 218;BA.debugLine="li.AddAll(Array As String(\"Call TELENOR Operator\"";
_li.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Call TELENOR Operator","Call MPT Operator","Call OOREDOO Operator","Call MECTEL Operator"}));
 //BA.debugLineNum = 219;BA.debugLine="a = b.InputList1(li,\"Choose Your Operator!\")";
_a = _b.InputList1(_li,"Choose Your Operator!",mostCurrent.activityBA);
 //BA.debugLineNum = 220;BA.debugLine="If a = 0 Then";
if (_a==0) { 
 //BA.debugLineNum = 221;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 222;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:979\")";
_i.Initialize(_i.ACTION_VIEW,"tel:979");
 //BA.debugLineNum = 223;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 225;BA.debugLine="If a = 1 Then";
if (_a==1) { 
 //BA.debugLineNum = 226;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 227;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:106\")";
_i.Initialize(_i.ACTION_VIEW,"tel:106");
 //BA.debugLineNum = 228;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 230;BA.debugLine="If a = 2 Then";
if (_a==2) { 
 //BA.debugLineNum = 231;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 232;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:234\")";
_i.Initialize(_i.ACTION_VIEW,"tel:234");
 //BA.debugLineNum = 233;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 235;BA.debugLine="If a = 3 Then";
if (_a==3) { 
 //BA.debugLineNum = 236;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 237;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"tel:01391814\")";
_i.Initialize(_i.ACTION_VIEW,"tel:01391814");
 //BA.debugLineNum = 238;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 240;BA.debugLine="End Sub";
return "";
}
public static String  _fb1_click() throws Exception{
 //BA.debugLineNum = 242;BA.debugLine="Sub fb1_Click";
 //BA.debugLineNum = 243;BA.debugLine="StartActivity(lollipop)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._lollipop.getObject()));
 //BA.debugLineNum = 244;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 26;BA.debugLine="Dim fb,fb1 As FloatingActionButton";
mostCurrent._fb = new de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper();
mostCurrent._fb1 = new de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim copy As BClipboard";
mostCurrent._copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 28;BA.debugLine="Dim bTelenor, bMPT, bOoredoo,bMEC As Button";
mostCurrent._btelenor = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._bmpt = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._booredoo = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._bmec = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim res As XmlLayoutBuilder";
mostCurrent._res = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 30;BA.debugLine="Dim tbg As ColorDrawable";
mostCurrent._tbg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 31;BA.debugLine="Dim mbg As ColorDrawable";
mostCurrent._mbg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 32;BA.debugLine="Dim obg As ColorDrawable";
mostCurrent._obg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 33;BA.debugLine="Dim mecbg As ColorDrawable";
mostCurrent._mecbg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 34;BA.debugLine="Dim lblTitle,lblFooter As Label";
mostCurrent._lbltitle = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblfooter = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Dim su As StringUtils";
mostCurrent._su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 36;BA.debugLine="Dim scr As Phone";
mostCurrent._scr = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 37;BA.debugLine="Dim AdView1 As AdView";
mostCurrent._adview1 = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim AdView2 As mwAdmobInterstitial";
mostCurrent._adview2 = new mobi.mindware.admob.interstitial.AdmobInterstitialsAds();
 //BA.debugLineNum = 39;BA.debugLine="Dim ph As Phone";
mostCurrent._ph = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 40;BA.debugLine="Dim bstart As Button";
mostCurrent._bstart = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim bsbg As ColorDrawable";
mostCurrent._bsbg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 42;BA.debugLine="Dim wv As WebView";
mostCurrent._wv = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static String  _help_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
 //BA.debugLineNum = 258;BA.debugLine="Sub help_Click";
 //BA.debugLineNum = 259;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 260;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 261;BA.debugLine="StartActivity(p.OpenBrowser(\"http://ht3tzn4ing.bl";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_p.OpenBrowser("http://ht3tzn4ing.blogspot.com/2016/11/MyanmarAllSimRegister.html")));
 //BA.debugLineNum = 262;BA.debugLine="End Sub";
return "";
}
public static String  _lblfooter_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
 //BA.debugLineNum = 428;BA.debugLine="Sub lblFooter_Click";
 //BA.debugLineNum = 429;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 430;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 431;BA.debugLine="StartActivity(p.OpenBrowser(\"https://www.facebook";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_p.OpenBrowser("https://www.facebook.com/Khun.Htetz.Naing")));
 //BA.debugLineNum = 432;BA.debugLine="End Sub";
return "";
}
public static String  _mwadi_adfailedtoload(String _errorcode) throws Exception{
 //BA.debugLineNum = 420;BA.debugLine="Sub mwadi_AdFailedToLoad (ErrorCode As String)";
 //BA.debugLineNum = 421;BA.debugLine="Log(\"failed to load ad\")";
anywheresoftware.b4a.keywords.Common.Log("failed to load ad");
 //BA.debugLineNum = 422;BA.debugLine="End Sub";
return "";
}
public static String  _mwadi_adloaded() throws Exception{
 //BA.debugLineNum = 424;BA.debugLine="Sub mwadi_AdLoaded";
 //BA.debugLineNum = 425;BA.debugLine="Log(\"ad loaded\")";
anywheresoftware.b4a.keywords.Common.Log("ad loaded");
 //BA.debugLineNum = 426;BA.debugLine="End Sub";
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
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 21;BA.debugLine="Dim Theme_Value As Int";
_theme_value = 0;
 //BA.debugLineNum = 22;BA.debugLine="Dim t,t1 As Timer";
_t = new anywheresoftware.b4a.objects.Timer();
_t1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _rad_click() throws Exception{
 //BA.debugLineNum = 175;BA.debugLine="Sub rad_Click";
 //BA.debugLineNum = 176;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 177;BA.debugLine="t1.Enabled = False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return "";
}
public static String  _settheme(int _theme) throws Exception{
 //BA.debugLineNum = 381;BA.debugLine="Private Sub SetTheme (Theme As Int)";
 //BA.debugLineNum = 382;BA.debugLine="If Theme = 0 Then";
if (_theme==0) { 
 //BA.debugLineNum = 383;BA.debugLine="ToastMessageShow(\"Theme not available.\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Theme not available.",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 384;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 386;BA.debugLine="If Theme = Theme_Value Then Return";
if (_theme==_theme_value) { 
if (true) return "";};
 //BA.debugLineNum = 387;BA.debugLine="Theme_Value = Theme";
_theme_value = _theme;
 //BA.debugLineNum = 388;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 389;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,main.getObject());
 //BA.debugLineNum = 390;BA.debugLine="End Sub";
return "";
}
public static String  _share_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
 //BA.debugLineNum = 246;BA.debugLine="Sub share_Click";
 //BA.debugLineNum = 247;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 248;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 249;BA.debugLine="copy.setText(\"#Myanmar_All_Sim_Register App! Regis";
mostCurrent._copy.setText(mostCurrent.activityBA,"#Myanmar_All_Sim_Register App! Registration you sim Free with this app. You can register all Myanmar sim Card, MPT, Telenor, Ooredoo, MecTel. Very simple useful App. Download Free at Google Play Store: https://play.google.com/store/apps/details?id=com.htetznaing.mmallsimregistration");
 //BA.debugLineNum = 250;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 251;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 252;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(mostCurrent._copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 253;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJEC";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("Get Free!!"));
 //BA.debugLineNum = 254;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\"";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 255;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 256;BA.debugLine="End Sub";
return "";
}
public static String  _t_tick() throws Exception{
 //BA.debugLineNum = 276;BA.debugLine="Sub t_tick";
 //BA.debugLineNum = 277;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 278;BA.debugLine="If AdView2.Status=AdView2.Status_AdReadyToShow Th";
if (mostCurrent._adview2.Status==mostCurrent._adview2.Status_AdReadyToShow) { 
 //BA.debugLineNum = 279;BA.debugLine="AdView2.Show";
mostCurrent._adview2.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 282;BA.debugLine="If AdView2.Status=AdView2.Status_Dismissed Then";
if (mostCurrent._adview2.Status==mostCurrent._adview2.Status_Dismissed) { 
 //BA.debugLineNum = 283;BA.debugLine="AdView2.LoadAd";
mostCurrent._adview2.LoadAd(mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 286;BA.debugLine="End Sub";
return "";
}
public static String  _t1_tick() throws Exception{
 //BA.debugLineNum = 263;BA.debugLine="Sub t1_tick";
 //BA.debugLineNum = 264;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 265;BA.debugLine="If AdView2.Status=AdView2.Status_AdReadyToShow Th";
if (mostCurrent._adview2.Status==mostCurrent._adview2.Status_AdReadyToShow) { 
 //BA.debugLineNum = 266;BA.debugLine="AdView2.Show";
mostCurrent._adview2.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 269;BA.debugLine="If AdView2.Status=AdView2.Status_Dismissed Then";
if (mostCurrent._adview2.Status==mostCurrent._adview2.Status_Dismissed) { 
 //BA.debugLineNum = 270;BA.debugLine="AdView2.LoadAd";
mostCurrent._adview2.LoadAd(mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 273;BA.debugLine="t1.Enabled = False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 274;BA.debugLine="End Sub";
return "";
}
public void _onCreate() {
	if (_theme_value != 0)
		setTheme(_theme_value);
}
}
