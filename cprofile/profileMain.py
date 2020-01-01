import time
def printFunc():
    time.sleep(2)
    print("This is a program to test the cProfile System")


def callPrintFunc():
    cnt = 0
    while cnt < 5:
        cnt = cnt + 1
        printFunc()


callPrintFunc()
