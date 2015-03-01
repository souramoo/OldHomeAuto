import string,cgi,time
from os import curdir, sep
from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer

import serial

nightlight=0
daylight=0

class MyHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        global nightlight
        global daylight
        global ser
        try:
            if self.path.endswith("nightlighton"):
                nightlight=1
                self.send_response(200)
                self.send_header('Content-type',	'text/html')
                self.end_headers()
                ser.write('a')
                self.wfile.write("OK")
                return

            if self.path.endswith("nightlightoff"):
                nightlight=0
                self.send_response(200)
                self.send_header('Content-type',	'text/html')
                self.end_headers()
                ser.write('b')
                self.wfile.write("Done")
                return

            if self.path.endswith("nightlightget"):
                self.send_response(200)
                self.send_header('Content-type',	'text/html')
                self.end_headers()
                self.wfile.write(nightlight)
                return

            if self.path.endswith("daylighton"):
                daylight=1
                self.send_response(200)
                self.send_header('Content-type',	'text/html')
                self.end_headers()
                ser.write('c')
                self.wfile.write("OK")
                return

            if self.path.endswith("daylightoff"):
                daylight=0
                self.send_response(200)
                self.send_header('Content-type',	'text/html')
                self.end_headers()
                ser.write('d')
                self.wfile.write("Done")
                return

            if self.path.endswith("daylightget"):
                self.send_response(200)
                self.send_header('Content-type',	'text/html')
                self.end_headers()
                self.wfile.write(daylight)
                return

            self.send_response(200)
            self.send_header('Content-type',	'text/html')
            self.end_headers()
            self.wfile.write("Error")

            return
        except IOError:
            self.send_error(404,'File Not Found: %s' % self.path)
            ser = serial.Serial('/dev/spark', 9600)


    def do_POST(self):
        return

def main():
    try:
        server = HTTPServer(('', 8090), MyHandler)
        print 'started httpserver...'
        server.serve_forever()
    except KeyboardInterrupt:
        print '^C received, shutting down server'
        server.socket.close()

ser = serial.Serial('/dev/spark', 9600)
main()

