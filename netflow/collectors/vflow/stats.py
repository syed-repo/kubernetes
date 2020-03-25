#!/usr/bin/env python
import psutil
from datetime import date
from datetime import datetime
import time

while True:
    cpu=psutil.cpu_percent()
    psutil.virtual_memory()
    mem=dict(psutil.virtual_memory()._asdict())
    now = datetime.now()

    dt_string = now.strftime("%d/%m/%Y %H:%M:%S")
    print(dt_string + ' ' + str(cpu) + ' ' + str(mem['percent']))
    time.sleep(1)
