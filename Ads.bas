Type=Activity
Version=6.3
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: True
	#IncludeTitle: false
#End Region

Sub Process_Globals
	Dim t As Timer
End Sub

Sub Globals
	Dim AdView2 As mwAdmobInterstitial
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	t.Initialize("t",500)
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