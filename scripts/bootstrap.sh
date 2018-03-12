#!/bin/bash

/app/wait-for-it.sh $GEARPUMP_HOST -t 300 -- python app.py --local &

gearpump-2.11-0.8.0/bin/local & gearpump-2.11-0.8.0/bin/services
