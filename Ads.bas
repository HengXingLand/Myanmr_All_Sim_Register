Type=Activity
Version=6.5
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
	Dim AdView2 As InterstitialAd
End Sub

Sub Activity_Create(FirstTime As Boolean)
	t.Initialize("t",500)
	t.Enabled = True
	
	AdView2.Initialize("AdView2","ca-app-pub-4173348573252986/6060374151")
	AdView2.LoadAd
	AdView2.Show
End Sub


Sub t_tick
	If AdView2.Ready Then AdView2.Show
		
	If AdView2.Ready = False Then AdView2.LoadAd
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)
	
End Sub