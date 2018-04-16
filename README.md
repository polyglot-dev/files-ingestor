
## External resources ingest

Endpoint for upload files (using streaming).



## Call endpoint with curl

```bash
$ curl -i --no-keepalive -F filedata=@/home/j/Downloads/UltraStudio-linux-17.07.1.0.zip  --header "filename: UltraStudio-linux-17.07.1.0.zip" http://127.0.1.1:9000/api/v1/files/upload


HTTP/1.1 100 Continue
Date: Thu, 02 Nov 2017 22:41:24 GMT

HTTP/1.1 200 OK
Vary: Origin
Date: Thu, 02 Nov 2017 22:41:25 GMT
Content-Type: text/plain; charset=UTF-8
Content-Length: 47

File 'UltraStudio-linux-17.07.1.0.zip' uploaded
```
