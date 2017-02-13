Type=Activity
Version=6.5
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim t,t1 As Timer
End Sub

Sub Globals
	Dim wv As WebView
	Dim tlb As Label
	Dim menu,share As Button
	Dim mmbg,sbg As BitmapDrawable
	Dim copy As BClipboard
	Dim AdView1 As AdView
	Dim AdView2 As InterstitialAd
End Sub

Sub Activity_Create(FirstTime As Boolean)
	AdView1.Initialize("AdView1","ca-app-pub-4173348573252986/4583640954")
	AdView1.LoadAd
	Activity.AddView(AdView1, 0%x,100%y - 50dip, 100%x, 50dip)
	
	AdView2.Initialize("AdView2","ca-app-pub-4173348573252986/6060374151")
	AdView2.LoadAd
	Log(AdView2)
	
	t.Initialize("t",15000)
	t.Enabled = True
	
	t1.Initialize("t1",100)
	t.Enabled = False
	
	tlb.Initialize("tlb")
	tlb.Text = "Tutorial"
	tlb.TextColor = Colors.White
	tlb.TextSize = 25
	tlb.Typeface = Typeface.DEFAULT_BOLD
	tlb.Gravity = Gravity.CENTER
	tlb.Color = Colors.RGB(33,150,243)
	Activity.AddView(tlb,0%x,0%y,100%x,55dip)
	
	mmbg.Initialize(LoadBitmap(File.DirAssets,"menu.png"))
	menu.Initialize("menu")
	menu.Background = mmbg
	menu.Gravity = Gravity.CENTER
	Activity.AddView(menu,10dip,12.5dip,30dip,30dip)
	
	sbg.Initialize(LoadBitmap(File.DirAssets,"share.png"))
	share.Initialize("share")
	share.Background = sbg
	share.Gravity = Gravity.CENTER
	Activity.AddView(share,100%x - 40dip,12.5dip,30dip,30dip)
	Activity.Title = "Tutorial"
wv.Initialize("")
	wv.LoadUrl("file:///" & File.DirRootExternal & "/.tt.Ht3tz")
Activity.AddView(wv,0%x,55dip,100%x,100%y)
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub t1_tick
	If AdView2.Ready Then AdView2.Show Else AdView2.LoadAd
	t1.Enabled = False
End Sub

Sub t_tick
	If AdView2.Ready Then AdView2.Show Else AdView2.LoadAd
End Sub

Sub AdView2_AdClosed
	AdView2.LoadAd
End Sub

Sub menu_Click
	Dim lis As List
	Dim idd_int As Int
	Dim idd As id
	lis.Initialize
	lis.AddAll(Array As String("Stop Ad Showing","How to use ?","Call to Operator!","About"))
	idd_int = idd.InputList1(lis,"Choose!")
	
	If idd_int = 0 Then
		t.Enabled = False
		t1.Enabled = False
	End If
	
	If idd_int = 1 Then
		t1.Enabled = True
		Dim p As PhoneIntents
		StartActivity(p.OpenBrowser("http://www.htetznaing.com/myanmar-all-sim-register"))
	End If
	
	If idd_int = 2 Then
		Dim a As Int
		Dim b As id
		Dim li As List
		li.Initialize
		li.AddAll(Array As String("Call TELENOR Operator","Call MPT Operator","Call OOREDOO Operator","Call MECTEL Operator"))
		a = b.InputList1(li,"Choose Your Operator!")
		If a = 0 Then
			Dim i As Intent
			i.Initialize(i.ACTION_CALL, "tel:979")
			StartActivity(i)
		End If
		If a = 1 Then
			Dim i As Intent
			i.Initialize(i.ACTION_CALL, "tel:106")
			StartActivity(i)
		End If
		If a = 2 Then
			Dim i As Intent
			i.Initialize(i.ACTION_CALL, "tel:234")
			StartActivity(i)
		End If
		If a = 3 Then
			Dim i As Intent
			i.Initialize(i.ACTION_CALL, "tel:01391814")
			StartActivity(i)
		End If
	End If
	
	If idd_int = 3 Then
		StartActivity(lollipop)
	End If
End Sub

Sub share_Click
	Dim ShareIt As Intent
	copy.clrText
	copy.setText("#Myanmar_All_Sim_Register App! Registration you sim Free with this app. You can register all Myanmar sim Card, MPT, Telenor, Ooredoo, MecTel. Very simple useful App. Download Free at : http://www.htetznaing.com/myanmar-all-sim-register/")
	ShareIt.Initialize (ShareIt.ACTION_SEND,"")
	ShareIt.SetType ("text/plain")
	ShareIt.PutExtra ("android.intent.extra.TEXT",copy.getText)
	ShareIt.PutExtra ("android.intent.extra.SUBJECT","Get Free!!")
	ShareIt.WrapAsIntentChooser("Share App Via...")
	StartActivity (ShareIt)
End Sub
