while($true){

    #The following line is needed to install power BI commands
    #Install-Module -Name MicrosoftPowerBIMgmt
    #########################################################################################################################################
    $WorkspaceID = "c35d9b60-2dd3-4019-8e8c-72460334857e"
    $datasetID = "20e2fc4b-f69f-4748-9bfc-131dea8ea5fb"
    $TableName = "RealTimeData"
    $groupWorkspaceUrl = "https://api.powerbi.com/v1.0/myorg/groups/" + $WorkspaceID + "/datasets/" + $datasetID + "/tables/" + $TableName + "/rows"
    ################################################################################################################################################

    $password = "VRx19355!!" | ConvertTo-SecureString -asPlainText -Force
    $username = "svc_PowerBI@Venatorx.com" 
    $credential = New-Object System.Management.Automation.PSCredential($username, $password)

    ##########################################################################################################################################
    Connect-PowerBIServiceAccount -Credential $credential

    $endpoint = "https://api.powerbi.com/beta/356e7329-c561-4aa1-98ce-b0395fef808d/datasets/20e2fc4b-f69f-4748-9bfc-131dea8ea5fb/rows?key=UU0dtYa5gwOP4eP2KDA0Boe0zFayiBuwiEdgkpnA0xrfbi670esoyJ6MPXA1wrEREl%2Fx2hbcctbnYaJVGQDh0A%3D%3D"
    $path = "R:\IT\NetLogs\Daily\DHCP\"

    $today = Get-Date

    #whitlist location
    $whiteList = "C:\BB\Script\Scripts Grant\whiteList.txt"
    #use the whitelist to create an array to check unknown devices
    $white = @()
    #fills array
    foreach($line in [System.IO.File]::ReadLines($whiteList)){

        $white += $line    

    }

    $startDate = Get-Date
    $DayOfWeek = (Get-Date).DayOfWeek
    
        #switch case checks current day of week, and sets starting date to the current weeks sunday. This allows us to grab the DHCP files that exist from this week
    Switch($DayOfWeek){
            "Monday" { $startDate = $startDate.AddDays(-1)}
        
            "Tuesday"{$startDate = $startDate.AddDays(-2)}
        
            "Wednesday"{$startDate = $startDate.AddDays(-3)}
        
            "Thursday"{$startDate = $startDate.AddDays(-4)}
        
            "Friday"{$startDate = $startDate.AddDays(-5)}
        
            "Saturday"{$startDate = $startDate.AddDays(-6)}
       
            "Sunday"{$startDate = $startDate}

        }

    Invoke-PowerBIRestMethod -Url "$groupWorkspaceUrl" -Method Delete

        while($startDate.Date -ne $today.Date){
       
            $tempArr = @()

            $day = "$($startDate.Year).$($startDate.Month).$($startDate.Day).DHCP.Assign.log"
            $file = "$($path)$($day)"
            if(Test-Path -Path $file){

                $fullPayload = @()

                    foreach($line in [System.IO.File]::ReadLines($file)){
                        $payload = @{
                            "IP" ="AAAAA555555"
                            "DeviceName" ="AAAAA555555"
                            "Mac" ="AAAAA555555"
                            "Time" ="2021-06-02T19:44:47.478Z"
                            "Alert" = 98.6
        
                        }


                        $list = $line.Split(",").Trim()
                        #Write-Host $list.Length

                        $payload.IP = $list[8]
                        $payload.Mac = $list[10]
                        $payload.Time = "$($list[5]) $($list[6])"
                        $temp = $list[9].Split(".")[0]
                        $payload.DeviceName = $temp 
                            #Write-Host $payload.IP
                            #Write-Host $payload.Mac
                            #Write-Host $payload.Time
                            #Write-Host $payload.DeviceName
                            #Write-Host (ConvertTo-Json $fullPayload)
                            #Write-Host $PreviousTime
                            #Write-Host $PreviousName

                        if(!$white.Contains($payload.DeviceName)){
                            $payload.Alert = 1
                        }else{
                            $payload.Alert = 0
                        }

                    
                        if($tempArr.Contains("$($payload.DeviceName), $($payload.Time)")){
                
                        }else{
                            $fullPayload += $payload
                        }
                    
                        $tempArr += "$($payload.DeviceName), $($payload.Time)"

                    }
                
                    Invoke-RestMethod -Method Post -Uri "$endpoint" -Body (ConvertTo-Json $fullPayload)
                }
                if($startDate.Date -lt $today.Date){
                    $startDate = $startDate.AddDays(1)
                }
            }
        
            $tempArr = @()
            $today = Get-Date
            $todayTest = Get-Date

        while($today.Date -eq $todayTest.Date){
            $today = Get-Date
            $day = "$($today.Year).$($today.Month).$($today.Day).DHCP.Assign.log"
            $file = "$($path)$($day)"

            $fullPayload = @()

            if(Test-Path -Path $file){

                    foreach($line in [System.IO.File]::ReadLines($file)){
                        $payload = @{
                            "IP" ="AAAAA555555"
                            "DeviceName" ="AAAAA555555"
                            "Mac" ="AAAAA555555"
                            "Time" ="2021-06-02T19:44:47.478Z"
                            "Alert" = 98.6
        
                        }


                        $list = $line.Split(",").Trim()
                        #Write-Host $list.Length

                        $payload.IP = $list[8]
                        $payload.Mac = $list[10]
                        $payload.Time = "$($list[5]) $($list[6])"
                        $temp = $list[9].Split(".")[0]
                        $payload.DeviceName = $temp 
                            #Write-Host $payload.IP
                            #Write-Host $payload.Mac
                            #Write-Host $payload.Time
                            #Write-Host $payload.DeviceName
                            #Write-Host (ConvertTo-Json $fullPayload)
                            #Write-Host $PreviousTime
                            #Write-Host $PreviousName
                        if(!$white.Contains($payload.DeviceName)){
                            $payload.Alert = 1
                        }else{
                            $payload.Alert = 0
                        }


                        if($tempArr.Contains("$($payload.DeviceName), $($payload.Time)")){
                
                        }else{
                            $fullPayload += $payload
                        }

                     $tempArr += "$($payload.DeviceName), $($payload.Time)"

                    }
                   # Write-Host $tempArr[0]
                
                    Invoke-RestMethod -Method Post -Uri "$endpoint" -Body (ConvertTo-Json $fullPayload)

            $FileSize = ((Get-Item $file).length/1KB)
        
            $MonitorSize = $FileSize
        
            while(($FileSize -eq $MonitorSize) -and ($today.Day -eq $todayTest.Day)){
                $today = Get-Date
                Start-Sleep 30
                $MonitorSize = ((Get-Item $file).length/1KB)     
                #Write-Host $MonitorSize
            }
        
            }else{
                while((!(Test-Path -Path $file)) -and ($today.Day -eq $todayTest.Day)){
                    $today = Get-Date
                    #Write-Host "sleeping"
                    Start-Sleep 300
                }
            }

        }
    }