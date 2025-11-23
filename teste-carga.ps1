$jobs = @()
1..50 | ForEach-Object {
    $jobs += Start-Job -ScriptBlock {
        $sw = [System.Diagnostics.Stopwatch]::StartNew()
        Invoke-WebRequest -Uri "http://localhost:8003/bank-api/transfers" `
                          -Method Post `
                          -Body '{"fromAccountId":"914f6284-ee9d-47ad-ab0e-6b4a3a39a554","toAccountNumber":"0001","amount":1}' `
                          -ContentType "application/json"
        $sw.Stop()
        "$($_) took $($sw.ElapsedMilliseconds) ms"
    }
}

# Espera todos terminarem e exibe os resultados
$jobs | ForEach-Object { Receive-Job $_; Remove-Job $_ }
