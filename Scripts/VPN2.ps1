#loops indefinetely
while($true){

#The following line is needed to install power BI commands
    #Install-Module -Name MicrosoftPowerBIMgmt

    $endpoint = "https://api.powerbi.com/beta/356e7329-c561-4aa1-98ce-b0395fef808d/datasets/c49c20ed-8042-437c-bcda-1e410c5d1998/rows?key=95hTczPyC2E8YN%2BKuQGvhkNjymOlNRojytnyfVMBUUzvnmpolBvx9UOzO9Vm50uAq378Wi4%2Fa3Lrj2wmR%2F4uRg%3D%3D"

    $date = Get-Date
    $todayTest = Get-Date

    $sleepDuration = 1
    $WorkspaceID = "c35d9b60-2dd3-4019-8e8c-72460334857e"
    $datasetID = "c49c20ed-8042-437c-bcda-1e410c5d1998"
    $TableName = "RealTimeData"
    $groupWorkspaceUrl = "https://api.powerbi.com/v1.0/myorg/groups/" + $WorkspaceID + "/datasets/" + $datasetID + "/tables/" + $TableName + "/rows"
    $eventsToSend = 500
    $index = 1
    ####################################################################

    $password = "VRx19355!!" | ConvertTo-SecureString -asPlainText -Force
    $username = "svc_PowerBI@Venatorx.com" 
    $credential = New-Object System.Management.Automation.PSCredential($username, $password)

    ###################################################################
    #connects to power bi services
    Connect-PowerBIServiceAccount -Credential $credential
    Write-Host $date
    Invoke-PowerBIRestMethod -Url "$groupWorkspaceUrl" -Method Delete
    #Write-Host "Deleting host"

    #loops while it is still current day
    while($date.Day -eq $todayTest.Day){


        $DayOfWeek = (Get-Date).DayOfWeek
        $startDate = (Get-Date)

        #switch case checks current day of week, and sets starting date to the current weeks sunday. This allows us to grab the VPN files from this week
        Switch($DayOfWeek){
            "Monday" { $startDate = $startDate.AddDays(-1)}
        
            "Tuesday"{$startDate = $startDate.AddDays(-2)}
        
            "Wednesday"{$startDate = $startDate.AddDays(-3)}
        
            "Thursday"{$startDate = $startDate.AddDays(-4)}
        
            "Friday"{$startDate = $startDate.AddDays(-5)}
        
            "Saturday"{$startDate = $startDate.AddDays(-6)}
       
            "Sunday"{$startDate = $startDate}

        }
        $today = Get-Date #uses the date in the file name
                                  
        #payload to send to power bi. resets after each loop so no repeat data is sent
        $fullPayload = @()

            #while the date variable is still the current day
            while($startDate.Date -ne $today.Date){
                   #---------------------
                   #following code manipulates date string to be the same as the vpn file naming convention
                    $month = $startDate.Month
                    $day = $startDate.Day
                    if($startDate.month -lt 10){
                        $month = "0$($startDate.Month)"
                    }
                    if($startDate.day -lt 10){
                        $day = "0$($startDate.Day)"
                    }

                    $fileName = "VPN.$($startDate.year).$($month).$($day).log"
                    $TextFile = "R:\IT\NetLogs\Daily\VPN\$($fileName)"
                    #---------------------
                   # Write-Host $TextFile
                   # $startDate = $startDate.AddDays(1)

                    #checks to see if file exists. If it does, we loop through every file from this week and add to payload
                    if(Test-Path -Path $TextFile){
                    ####################### Parse thru the file (string manipulation) ##################

                
                        #------------------------
                        foreach($line in [System.IO.File]::ReadLines($TextFile)){
                        $payload = @{
                                    "UsernameVPN" ="AAAAA555555"
                                    "TimeVPN" ="2021-05-14T16:45:21.806Z"
                                    }
                            if(($line -eq ("------------------------")) -or ($line -eq "")){
                            #Skip line
                                #Write-Host "Skip"
                            }
                            else{
                                $temp = $line.split(",").Trim()
  
                                 #Write-Host "h$($temp[1])h"
                                 $dateVar = $temp[2].split("T")[0]
                                 $time = $temp[2].split("T")[1]
                                 $time = $time.split("-")[0]
                                 #Write-Host "h$($time)h"
                                 $payload.UsernameVPN = $temp[1]
                                 $payload.TimeVPN = "$($dateVar) $($time)"
                 
                                 $fullPayload += $payload     
                         
                                  }
                            }
                        $startDate = $startDate.AddDays(1)
                         
                        #if that file doesnt exist, add a day to check if there is a vpn file for the next day
                    }else{
                        $startDate = $startDate.AddDays(1)
            
                        }
               
                
                 }#end of while loop. Jumps back up to read remaining files.

                    $payload = @{
                                "UsernameVPN" ="AAAAA555555"
                                "TimeVPN" ="2021-05-14T16:45:21.806Z"
                                }
##------------------------------------------------------- Below code loops on todays vpn file -------------------------------------------
          

                $date = Get-Date              
                $month = $date.Month
                $day = $date.Day
                if($date.month -lt 10){
                    $month = "0$($date.Month)"
                }
                if($date.day -lt 10){
                    $day = "0$($date.Day)"
                }

                $fileName = "VPN.$($date.year).$($month).$($day).log"
                $TextFile = "R:\IT\NetLogs\Daily\VPN\$($fileName)"

                #checks to see if file exists
                if(Test-Path -Path $TextFile){
                ####################### Parse thru the file (string manipulation) ##################

                 
                    foreach($line in [System.IO.File]::ReadLines($TextFile)){
                    $payload = @{
                                "UsernameVPN" ="AAAAA555555"
                                "TimeVPN" ="2021-05-14T16:45:21.806Z"
                                }
                        if(($line -eq ("------------------------")) -or ($line -eq " ")){
                            #do nothing. vpn file has --- seperators
                        }
                        else{
                            $temp = $line.split(",").Trim()
  
                             #Write-Host "h$($temp[1])h"
                             $dateVar = $temp[2].split("T")[0]
                             $time = $temp[2].split("T")[1]
                             $time = $time.split("-")[0]
                             #Write-Host "h$($time)h"
                             $payload.UsernameVPN = $temp[1]
                             $payload.TimeVPN = "$($dateVar) $($time)"
                 
                             $fullPayload += $payload     
                        
                              }
                        }
                        #send to power bi
                       try
                        {
                            Invoke-RestMethod -Method Post -Uri "$endpoint" -Body (ConvertTo-Json $fullPayload) -EA Stop
                        }
                        catch
                        {
                            Connect-PowerBIServiceAccount -Credential $credential
                            Write-Host "Post Error Handled"
                            Invoke-RestMethod -Method Post -Uri "$endpoint" -Body (ConvertTo-Json $fullPayload)
                        }
                        
        
                    $FileSize = ((Get-Item $TextFile).length/1KB)
                    #Write-Host $FileSize
                    $MonitorSize = $FileSize
                    ####################################################################################
                    #stays in this loop while file size has not changed (checks every 30 seconds)
                    while(($FileSize -eq $MonitorSize) -and ($date.Day -eq $todayTest.Day)){
                        $date = Get-Date
                        Start-Sleep 30
                        $MonitorSize = ((Get-Item $TextFile).length/1KB)
                       # Write-Host "$MonitorSize"
                    }
                    #error handling
                    try
                    {
                        Invoke-PowerBIRestMethod -Url "$groupWorkspaceUrl" -Method Delete -EA Stop
                    }
                    catch
                    {
                        Connect-PowerBIServiceAccount -Credential $credential
                        Write-Host "Delete Error Handled"
                       Invoke-PowerBIRestMethod -Url "$groupWorkspaceUrl" -Method Delete
                    }
                        #Write-Host "Deleting host"

                    #if file does not exist, wait until its created
                }else{

                    Write-Host "File Not Found or Created @"
                    Write-Host (Get-Date).TimeOfDay
        
                   try
                    {
                        Invoke-RestMethod -Method Post -Uri "$endpoint" -Body (ConvertTo-Json $fullPayload) -EA Stop
                    }
                    catch
                    {
                        Connect-PowerBIServiceAccount -Credential $credential
                        Write-Host "Post Error Handled"
                        Invoke-RestMethod -Method Post -Uri "$endpoint" -Body (ConvertTo-Json $fullPayload)
                    }
            

                    while((!(Test-Path -Path $TextFile)) -and ($date.Day -eq $todayTest.Day)){
                        $date = Get-Date
                        $month = $date.Month
                        $day = $date.Day
                        if($date.month -lt 10){
                            $month = "0$($date.Month)"
                        }
                        if($date.day -lt 10){
                            $day = "0$($date.Day)"
                        }

                        $fileName = "VPN.$($date.year).$($month).$($day).log"
                        $TextFile = "R:\IT\NetLogs\Daily\VPN\$($fileName)"
                       # checks for file creation every 300 seconds
                        Start-Sleep 300
                    }
               
                    try
                    {
                        Invoke-PowerBIRestMethod -Url "$groupWorkspaceUrl" -Method Delete -EA Stop
                    }
                    catch
                    {
                        Connect-PowerBIServiceAccount -Credential $credential
                        Write-Host "Delete Error Handled"
                       Invoke-PowerBIRestMethod -Url "$groupWorkspaceUrl" -Method Delete
                    }
                }
            }
   }