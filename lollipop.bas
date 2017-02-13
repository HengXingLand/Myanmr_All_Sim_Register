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
Dim t As Timer
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim su As StringUtils 
	Dim p As PhoneIntents 
	Dim lstOne As ListView 
	Dim abg As BitmapDrawable
	Dim Banner As AdView
	Dim Interstitial As InterstitialAd
	Dim ph As Phone
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	ph.SetScreenOrientation(1)
	Banner.Initialize("AdView1","ca-app-pub-4173348573252986/4583640954")
	Banner.LoadAd
	Activity.AddView(Banner, 0%x,100%y - 50dip, 100%x, 50dip)
	
	Interstitial.Initialize("AdView2","ca-app-pub-4173348573252986/6060374151")
	Interstitial.LoadAd
	
t.Initialize("t",10000)
t.Enabled=True

	Activity.Title = "About"
	abg.Initialize(LoadBitmap(File.DirAssets,"bg.jpg"))
	Activity.Background = abg
	
	Dim imvLogo As ImageView 
	imvLogo.Initialize ("imv")
	imvLogo.Bitmap = LoadBitmap(File.DirAssets , "about.png")
	imvLogo.Gravity = Gravity.FILL 
	Activity.AddView ( imvLogo , 50%x - 50dip  , 20dip ,  100dip  ,  100dip )
	
	Dim lblName As  Label 
	Dim bg As ColorDrawable 
	bg.Initialize (Colors.DarkGray , 10dip)
	lblName.Initialize ("lbname")
	lblName.Background = bg
	lblName.Gravity = Gravity.CENTER 
	lblName.Text = "Myanmar All Sim Register"
	lblName.TextSize = 13
	lblName.TextColor = Colors.White 
	Activity.AddView (lblName , 100%x / 2 - 90dip , 130dip , 180dip , 50dip)
	lblName.Height = su.MeasureMultilineTextHeight (lblName, lblName.Text ) + 5dip
	
	
	Dim c As ColorDrawable 
	c.Initialize (Colors.White , 10dip )
	lstOne.Initialize ("lstOnes")
	lstOne.Background = c
	lstOne.SingleLineLayout .Label.TextSize = 12
	lstOne.SingleLineLayout .Label .TextColor = Colors.DarkGray 
	lstOne.SingleLineLayout .Label .Gravity = Gravity.CENTER 
	lstOne.SingleLineLayout .ItemHeight = 40dip
	lstOne.AddSingleLine2 ("Developed By : Khun Htetz Naing    ", 1)
	lstOne.AddSingleLine2 ("Email : khunht3tzn4ing@gmail.com    ",2)
	lstOne.AddSingleLine2 ("Powered By : Myanmar Android Apps    ",3)
	lstOne.AddSingleLine2 ("Website : www.htetznaing.com  ", 4)
	lstOne.AddSingleLine2 ("Facebook : www.fb.com/MmFreeAndroidApps   ", 5)
	Activity.AddView ( lstOne, 30dip , 170dip , 100%x -  60dip, 200dip)
	
	Dim lblCredit As Label 
	lblCredit.Initialize ("lblCredit")
	lblCredit.TextColor = Colors.RGB (74,20,140)
	lblCredit.TextSize = 13
	lblCredit.Gravity = Gravity.CENTER 
	lblCredit.Text = "If You have any Problem You can contact Me."
	Activity.AddView (lblCredit, 10dip,(lstOne.Top+lstOne.Height)+3%y, 100%x - 20dip, 50dip)
	lblCredit.Height = su.MeasureMultilineTextHeight (lblCredit, lblCredit.Text )
		
End Sub
 
Sub Interstitial_AdClosed
	Interstitial.LoadAd
End Sub

Sub t_Tick
	If Interstitial.Ready Then Interstitial.Show
		
	If Interstitial.Ready = False Then Interstitial.LoadAd
End Sub

Sub imv_Click
	Try
 
		Dim Facebook As Intent
 
		Facebook.Initialize(Facebook.ACTION_VIEW, "fb://profile/100006126339714")
		StartActivity(Facebook)
 
	Catch
 
		Dim i As Intent
		i.Initialize(i.ACTION_VIEW, "https://m.facebook.com/MgHtetzNaing")
 
		StartActivity(i)
 
	End Try
End Sub

Sub lbname_Click
 	StartActivity(p.OpenBrowser("https://play.google.com/store/apps/details?id=com.htetznaing.mmallsimregistration2"))
End Sub

Sub lblCredit_Click
	Try
 
		Dim Facebook As Intent
 
		Facebook.Initialize(Facebook.ACTION_VIEW, "fb://profile/100006126339714")
		StartActivity(Facebook)
 
	Catch
 
		Dim i As Intent
		i.Initialize(i.ACTION_VIEW, "https://m.facebook.com/MgHtetzNaing")
 
		StartActivity(i)
 
	End Try
End Sub
Sub Activity_Resume
     
End Sub

Sub Activity_Pause (UserClosed As Boolean)
     
End Sub

Sub lstOnes_ItemClick (Position As Int, Value As Object)
	Select Value
		Case 1
			Try
 
				Dim Facebook As Intent
 
				Facebook.Initialize(Facebook.ACTION_VIEW, "fb://profile/100006126339714")
				StartActivity(Facebook)
 
			Catch
 
				Dim i As Intent
				i.Initialize(i.ACTION_VIEW, "https://m.facebook.com/MgHtetzNaing")
 
				StartActivity(i)
 
			End Try
			Case 2
			Dim Message As Email
			Message.To.Add("khunhtetznaing@gmail.com")
			Message.CC.Add("")
			StartActivity(Message.GetIntent)
			
		Case 3
			Try
 
				Dim Facebook As Intent
 
				Facebook.Initialize(Facebook.ACTION_VIEW, "fb://page/627699334104477")
				StartActivity(Facebook)
 
			Catch
 
				Dim i As Intent
				i.Initialize(i.ACTION_VIEW, "https://m.facebook.com/MmFreeAndroidApps")
 
				StartActivity(i)
 
			End Try
			
	 			Case 4
				   StartActivity(p.OpenBrowser ("http://www.htetznaing.com/"))
				   
		Case 5
			Try
 
				Dim Facebook As Intent
 
				Facebook.Initialize(Facebook.ACTION_VIEW, "fb://page/627699334104477")
				StartActivity(Facebook)
 
			Catch
 
				Dim i As Intent
				i.Initialize(i.ACTION_VIEW, "https://m.facebook.com/MmFreeAndroidApps")
 
				StartActivity(i)
 
			End Try
	End Select
End Sub