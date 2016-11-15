Type=Activity
Version=6.3
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: True
	#IncludeTitle: True
#End Region

Sub Process_Globals
	Dim t As Timer
	Dim Theme_Value As Int
End Sub

Sub Globals
	Dim AdView2 As mwAdmobInterstitial
	Dim res As XmlLayoutBuilder
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	SetTheme(res.GetResourceId("style", "android:style/Theme.Translucent.NoTitleBar"))
	
	t.Initialize("t",10)
	t.Enabled = True
	
	AdView2.Initialize("AdView2","ca-app-pub-4173348573252986/8871242155")
	AdView2.LoadAd
	AdView2.Show
	
End Sub


Sub t_tick
	
	If AdView2.Status=AdView2.Status_AdReadyToShow Then
	AdView2.Show
	End If
	
	If AdView2.Status=AdView2.Status_Dismissed Then
	AdView2.LoadAd
	End If
End Sub

Sub Activity_Resume
	
If AdView2.Status=AdView2.Status_AdReadyToShow Then
	AdView2.Show
	End If
	
	If AdView2.Status=AdView2.Status_Dismissed Then
	AdView2.LoadAd
	End If
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
If AdView2.Status=AdView2.Status_AdReadyToShow Then
	AdView2.Show
	End If
	If AdView2.Status=AdView2.Status_Dismissed Then
	AdView2.LoadAd
	End If
	
End Sub

Private Sub SetTheme (Theme As Int)
	If Theme = 0 Then
		ToastMessageShow("Theme not available.", False)
		Return
	End If
	If Theme = Theme_Value Then Return
	Theme_Value = Theme
	Activity.Finish
	StartActivity(Me)				
End Sub


#if java
public void _onCreate() {
	if (_theme_value != 0)
		setTheme(_theme_value);
}
#end if