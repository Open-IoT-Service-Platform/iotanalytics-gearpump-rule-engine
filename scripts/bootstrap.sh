#!/bin/bash

/app/wait-for-it.sh localhost:8090 -t 300 -- python app.py --local &

/gearpump/gearpump-2.11-0.8.0/bin/local & /gearpump/gearpump-2.11-0.8.0/bin/services
