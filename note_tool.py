import os
import re
from configparser import ConfigParser

conf = ConfigParser()
conf.read('note_tool.conf')


def get_file_extension(file_path):
    '''取得副檔名'''
    return os.path.splitext(file_path)[1][1:]


target_dir = conf.get('TOOL', 'TARGET_DIR', fallback=None)

if target_dir != None:
    files = os.listdir(target_dir)
    count = 0
    for file in files:
        count += 1
        file_path = f"{target_dir}/{file}"
        if os.path.splitext(file_path)[1] == ".txt":
            # print(file)
            with open(file_path, 'r') as f:
                content = f.read()
                # content = f"# {re.sub('.txt', '', file)}\n\n{content}"
                keyword = r"##################################\n[\s]*(.*?)\n##################################"
                items = re.findall(keyword, content)
                print(items)
                for item in items:
                    tar = r"##################################\n[\s]*{}\n##################################".format(item)
                    new_tar = f'## {item}\n'
                    content = f"{re.sub(tar, new_tar, content)}"
                    # print(content)
                with open(file_path, 'w') as w:
                    w.write(content)
        # if count == 3:
        #     break
else:
    print(f'指定資料夾:{target_dir}')
