#!/usr/bin/env python3
import csv
import os

ORIGINAL_DIRECTORY = './1stprocesseed'
PROCESSED_DIRECTORY = './processed'


def 午前中ルール():
    pass

def csv2tsv(filename:str, dstfilename:str):
    actions = []
    with open(filename, 'r') as fh:
        lines = fh.readlines()
        for i, l in enumerate(lines):
            l : str = l.strip()

            # 空行のスキップ
            if len(l) <= 0:
                continue

            # print(l.split(maxsplit=2))  # DEBUG

            try:
                # 終日ルール(あとで考える)
                if l.find('終日') > 0:
                    try:
                        action_date, action = l.split(maxsplit=2)
                        action_date, action = l.split(maxsplit=2)
                        action_time = '9:00'
                        actions.append([action_date, action_time, action])
                    except ValueError as ve:
                        print('DEBUG => {} :'.format(i)  + l)
                        print(l.split(maxsplit=2))  # DEBUG
                # 午前中ルール
                elif l.find('午前中') > 0:
                # elif l.find('午前中') > 0 or (l.index('（日本時間午前）') > 0 and len(l.split(maxsplit=2)) == 3):
                    # print('DEBUG => ' + l)
                    print(l.split(maxsplit=2))  # DEBUG
                    try:
                        action_date, action = l.split(maxsplit=2)
                        action_date, action = l.split(maxsplit=2)
                        action_time = '9:00'
                        actions.append([action_date, action_time, action])
                        # message = ' |  '.join([action_date, action_time, action])
                        # print("DEBUG: " + message)
                        # break
                    except ValueError as ve:
                        print('DEBUG => ' + l)
                        print(l.split(maxsplit=2))  # DEBUG
                else:
                    action_date, action_time , action = l.split(maxsplit=2)
                    actions.append([action_date, action_time, action])
                    # message = ' |  '.join(l.split(maxsplit=2))
            except ValueError as ge:
                print('DEBUG => {} :'.format(i)  + l)
                print(l.split(maxsplit=2))  # DEBUG
                exit()

    # writing
    with open(dstfilename, 'w') as wfh:
        csv_writer = csv.writer(wfh, 'excel-tab')
        csv_writer.writerows(actions)
    return

if __name__ == "__main__":
    src = './original/201804.csv'
    dst = './processed/201804.csv'
    csv2tsv(src, dst)

    for _, _, files in os.walk(ORIGINAL_DIRECTORY):
        for f in sorted(files):
            srcfile = '{dir}/{file}'.format(**dict(dir=ORIGINAL_DIRECTORY, file=f))
            dstfile = '{dir}/{file}'.format(**dict(dir=PROCESSED_DIRECTORY, file=f))
            print(srcfile, '  |  ', dstfile)
            csv2tsv(srcfile, dstfile)
