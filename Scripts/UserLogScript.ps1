#Sets initial date one day behind upon startup
$today = (Get-Date).AddDays(-1)
#loops indefinetly (whole code)
while($true){

#The following line is needed to install power BI commands (if device does not have cmd's already installed)
    #Install-Module -Name MicrosoftPowerBIMgmt

############################################################
#These variables are for connecting to our power bi workspaces and data
$endpoint = "https://api.powerbi.com/beta/356e7329-c561-4aa1-98ce-b0395fef808d/datasets/4cb53e14-7ba9-45b9-a060-ab5ff6a14b9a/rows?key=xsqAMAF4weami6bbvATl%2B%2BZZP2M9RuHNOpspWE3di%2FJVDj%2F%2F6t9jPatmEyQH2FZj%2BotTke8ZCzT99M54oh8MgA%3D%3D"

    $WorkspaceID = "c35d9b60-2dd3-4019-8e8c-72460334857e"
    $datasetID = "4cb53e14-7ba9-45b9-a060-ab5ff6a14b9a"
    $TableName = "RealTimeData"
    $groupWorkspaceUrl = "https://api.powerbi.com/v1.0/myorg/groups/" + $WorkspaceID + "/datasets/" + $datasetID + "/tables/" + $TableName + "/rows"
############################################################
#These variables allow auto-connection to powerbi services
$password = "VRx19355!!" | ConvertTo-SecureString -asPlainText -Force
$username = "svc_PowerBI@Venatorx.com" 
$credential = New-Object System.Management.Automation.PSCredential($username, $password)

############################################################

#checks date, reconnects to power bi services when day changes
if($today.Day -ne (Get-Date).Day){
    Connect-PowerBIServiceAccount -Credential $credential
    Invoke-PowerBIRestMethod -Url "$groupWorkspaceUrl" -Method Delete
    Write-Host (Get-Date)
}
#sets date to current day so above code will execute tomorrow
$today = Get-Date
$year = $today.Year
$importFile = "R:\IT\NetLogs\Daily\$($year)\Users.csv"

#check import file
#Write-Host $importFile

#array that holds entire payload
$fullPayload = @() #resets after each loop

    foreach($line in [System.IO.File]::ReadLines($importFile)){

        $payload = @{
            "Username" ="AAAAA555555"
            "Time" ="2021-05-24T19:27.482Z"
            "IP" ="AAAAA555555"
            "Domain" ="AAAAA555555"
            "Server" ="AAAAA555555"
            "Windows" ="AAAAA555555"
            "ElapsedTime" ="AAAAA555555"
            "gB" ="AAAAA555555"
            "Heartbeat" = "AAAAA555555"
            "IPValue" = 98.6
		            }

        #csv reads line by line and separates cells by ",". Splitting by "," creates array of needed variables
        $ArrayGroup = $line.Split(",")
       
       #checks to make sure we are reading lines with data (not empty lines)
        if ($ArrayGroup.count -ge 10){
        #sets variables
            $payload.Username = $ArrayGroup[0].trim()
            $payload.Time = $ArrayGroup[1].trim() + " " + $ArrayGroup[2].SubString(0,$ArrayGroup[2].length - 8)
            $payload.IP = $ArrayGroup[3].trim()
            $payload.Domain = $ArrayGroup[5].trim()
            $payload.Server = $ArrayGroup[6].trim()
            $payload.Windows = $ArrayGroup[7].trim()
            $payload.ElapsedTime = $ArrayGroup[8].trim()
            $payload.gB = $ArrayGroup[9].trim()

            $ipVal = $ArrayGroup[3].Split(".")

            ###########  KEY  ##################
            #1 = 10.40.60. = AR LAN
            #2 = 10.40.20. = AR Wireless
            #3 = 10.40.40. = AR server
            #4 = 10.50.60. = SM LAN
            #5 = 10.50.20. = SM wireless
            ###################################

            if($ipVal[1] -eq 40){
                
                if($ipVal[2] -eq 60){
                    $payload.IPValue = 1 #AR LAN
                }elseif($ipVal[2] -eq 20){
                    $payload.IPValue = 2 #AR Wireless
                }elseif($ipVal[2] -eq 40){
                    $payload.IPValue = 3 #AR server
                }else{
                    $payload.IPValue = 100
                }

            }elseif($ipVal[1] -eq 50){

                if($ipVal[2] -eq 60){
                    $payload.IPValue = 4 #SM LAN
                }elseif($ipVal[2] -eq 20){
                    $payload.IPValue = 5 #SM wireless
                }else{
                    $payload.IPValue = 100
                }

            }else{
                $payload.IPValue = 100
            }

            #this allows for null heartbeat values to be passed. For all lines in file that were created before george made heartbeat
            if($ArrayGroup[10] -eq $null){
                $payload.Heartbeat = ""
            }else{
                $payload.Heartbeat = $ArrayGroup[10].trim()
            }
			                #Write-Host $payload.Username
			                #Write-Host $payload.Time
			                #Write-Host $payload.IP
			                #Write-Host $payload.Domain
			                #Write-Host $payload.Server
			                #Write-Host $payload.Windows
			                #Write-Host $payload.ElapsedTime
			                #Write-Host $payload.gB
        }
        else{
            Write-Host "Invalid data format"
        }
        #adds information to payload, line by line
        $fullPayload += $payload
    }
    #sends information to power bi
    Invoke-RestMethod -Method Post -Uri "$endpoint" -Body (ConvertTo-Json $fullPayload)
   #grabs file size to check when new user has logged on
    $FileSize = ((Get-Item $importFile).length/1KB)
        
    $MonitorSize = $FileSize
        ####################################################################################
        #stays inside loop, while file has not changed. Checks every 30 seconds
    while($FileSize -eq $MonitorSize){
        
        Start-Sleep 30
        $MonitorSize = ((Get-Item $importFile).length/1KB)     
        #Write-Host $MonitorSize
    }
    #after file size changes. This deletes all info from power bi to repopulate everything without repeated information
    Invoke-PowerBIRestMethod -Url "$groupWorkspaceUrl" -Method Delete
}