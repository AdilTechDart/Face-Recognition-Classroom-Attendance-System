# coding:utf-8
from flask import Flask
from flask import request
import json
import pymysql
import os
import base64
import requests
import urllib3
import json
from json import JSONDecoder



app = Flask(__name__)

basedir = os.path.abspath(os.path.dirname(__file__))

key ="De3tTS2pT_R-02leUEdvvqSpLnY3rFgn"
secret ="ylwb_Ixyfqu4e9YglO-HPdDuHZ10K_g2"
facesetname="学生数据"
filepath_s = "img_filename"
Remove='RemoveAllFaceTokens'

@app.route('/upload', methods = ['POST'])
def upload_s():
    json_dict = json.loads(request.get_data().decode('utf-8'))
    img_str = json_dict['image']
    img_filename = json_dict['imgfilename']
    image_data = base64.b64decode(img_str)
    file = open(img_filename, 'wb')
    file.write(image_data)
    file.close()
    return "{\"code\" : 1}"



@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        dict = json.loads(request.get_data().decode('utf-8'))
        username = dict['username']
        password = dict['password']
        result = dbexec('select * from UP where username=\'{username}\' and password=\'{password}\''.format(username=username, password=password))
        if result == -1:
            return json.dumps({"code":0})#登录失败 code:0
        elif result == 1:
            return json.dumps({"code":1})#登录成功 code:1
    elif request.method == 'GET':
        username = request.args.get('username')
        password = request.args.get('password')
        result = dbexec('select * from UP where username=\'{username}\' and password=\'{password}\''.format(username=username, password=password))
        if result == -1:
            return json.dumps({"code": 0})  # 登录失败 code:0
        elif result == 1:
            return json.dumps({"code": 1})  # 登录成功 code:1

@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        dict = json.loads(request.get_data().decode('utf-8'))
        username = dict['username']
        password = dict['password']
        is_user_existed = dbexec('select * from UP where username=\'{username}\''.format(username=username))
        if is_user_existed == 1:
            return json.dumps({"code":0}) #已存在此用户，重复注册 code:0
        result = dbexec('insert into UP (username, password) values (\'{username}\', \'{password}\')'.format(username=username, password=password), False)
        if result == -1:
            return json.dumps({"code":0}) #注册失败 code:0
        elif result == 1:
            return json.dumps({"code":1}) #登录成功 code:1
    elif request.method == 'GET':
        username = request.args.get('username')
        password = request.args.get('password')
        is_user_existed = dbexec('select * from UP where username=\'{username}\''.format(username=username))
        if is_user_existed == 1:
            return json.dumps({"code": 0})  # 已存在此用户，重复注册 code:0
        result = dbexec('insert into UP (username, password) values (\'{username}\', \'{password}\')'.format(username=username, password=password), False)
        if result == -1:
            return json.dumps({"code":0}) #注册失败 code:0
        elif result == 1:
            return json.dumps({"code":1}) #登录成功 code:1

def dbexec(sql = '', is_query = True):
    conn = pymysql.connect(host = '39.96.221.44', port = 3306, user = 'root', passwd = 'Gyx3034688', db = 'gyxdatabase')
    cursor = conn.cursor()
    effect_row = cursor.execute(sql)
    conn.commit()
    conn.close()
    if is_query == True: #判断调用者为登录还是注册
        if cursor.rowcount == 0:
            return -1  # 查询结果为空，登录失败
        else:
            return 1  # 登录成功
    elif is_query == False:
        if effect_row == 1:
            return 1
        else:
            return -1

@app.route('/create',methods=['POST'])
def set_face():  # 创建face_set
    url = 'https://api-cn.faceplusplus.com/facepp/v3/faceset/create'
    params = {
        'api_key': key,
        'api_secret': secret,
        'outer_id': facesetname,

    }
    response = requests.post(url, data=params)
    req_dict = response.json()
    print(req_dict)
    return json.dumps({'code':1})

    #return json.dumps(dict1)//字典格式传递数据

@app.route('/clean',methods=['POST'])
def clean_face():
    #清除faceset中所有人脸
    url = 'https://api-cn.faceplusplus.com/facepp/v3/faceset/removeface'

    params = {
        'api_key': key,
        'api_secret': secret,
        'outer_id': facesetname,
        'face_tokens':Remove,
    }
    r = requests.post(url, data=params)
    req_dict = r.json()
    #print(req_dict)
    gyx_clean = {}
    gyx_clean['number'] = str(req_dict["face_removed"])
    # print(req_dict)
    print("删除人脸数目：" + str(req_dict["face_removed"]))
    dbexec('DELETE FROM Student;')
    #dbexec('insert into Student (Studentname, Studentstate) values (\'测试木人\', \'0\')')
    return json.dumps(gyx_clean)


@app.route('/update',methods=['POST'])
def detect_face():
    json_dict = json.loads(request.get_data().decode('utf-8'))
    name = json_dict['username']

    filename="学生上传"+name+".jpeg"
    url = "https://api-cn.faceplusplus.com/facepp/v3/detect"

    with open(filename, "rb") as f:  # 转为二进制格式
        base64_data = base64.b64encode(f.read())  # 使用base64进行加密
    params = {
        'api_key': key,
        'api_secret': secret,
        'image_base64': base64_data,
        'return_attributes': "gender,age,ethnicity",
    }
    response = requests.post(url, data=params)
    # 以二进制读入图像，这个字典中open(filepath_S, "rb")返回的是二进制的图像文件，所以"image_file"是二进制文件，符合官网要求
    # POTS上传

    # response的内容是JSON格式

    req_dict = response.json()
    # 对其解码成字典格式
    # print(req_dict)
    # 输出
    #print(req_dict)
    face = req_dict["faces"]
    local= face
    num = (len(face))
    gyx = {}
    if num !=0:
        for i in range(num):
            print("第" + str(i + 1) + "个人脸:")
            a1 = req_dict["faces"][i]
            print("年龄:" + str(a1["attributes"]["age"]["value"]))
            print("性别:" + str(a1["attributes"]["gender"]["value"]))
            # print("性别:"+str(a["attributes"]["ethnicity"]["value"]))
            b = req_dict["faces"][i]
            print("人脸唯一识别码:" + str(b["face_token"]))

            gyx['age'] = str(a1["attributes"]["age"]["value"])
            gyx['gender'] = str(a1["attributes"]["gender"]["value"])
            gyx['code'] = str(b["face_token"])
            gyx['num'] = str(num)
            gyx['error'] = str("T")
            c=local[i]["face_rectangle"]
            print(local[i]["face_rectangle"])
            gyx['width'] =c["width"]
            gyx['top']=c["top"]
            gyx['left']=c["left"]
            gyx['height']=c["height"]
            print(gyx)


        print("共检测到" + str(num) + "个人脸！")
    else :
        gyx['error']=str("F")

    return json.dumps(gyx)


@app.route('/add',methods=['POST'])
def detect_facecode():
    json_dict = json.loads(request.get_data().decode('utf-8'))
    name = json_dict['username']
    filename="学生上传"+name+".jpeg"
    url = "https://api-cn.faceplusplus.com/facepp/v3/detect"

    with open(filename, "rb") as f:  # 转为二进制格式
        base64_data = base64.b64encode(f.read())  # 使用base64进行加密
    params = {
        'api_key': key,
        'api_secret': secret,
        'image_base64': base64_data,
        'return_attributes': "gender,age,ethnicity",
    }
    response = requests.post(url, data=params)
    req_dict = response.json()
    print(req_dict)
    face = req_dict["faces"]

    num = (len(face))
    scod = {}
    if num !=0:
        for i in range(num):

            a1 = req_dict["faces"][i]
            b = req_dict["faces"][i]
            scod['age'] = str(a1["attributes"]["age"]["value"])
            scod['gender'] = str(a1["attributes"]["gender"]["value"])
            scod['code'] = str(b["face_token"])
            scod['num'] = str(num)
            scod['error'] = str("T")
    name_a = str(b["face_token"])
    url = 'https://api-cn.faceplusplus.com/facepp/v3/faceset/addface'
    params = {
            'api_key':key,
            'api_secret':secret,
            'outer_id':facesetname,
            'face_tokens': name_a,
            }
    r = requests.post(url,data = params)
    req_dict = r.json()
    print(req_dict)
    print("人脸库总数：" + str(req_dict["face_count"]))
    print("此次增加人脸数:"+str(req_dict["face_added"]))
    gyxadd ={ }
    gyxadd['num']=str(req_dict["face_added"])
    gyxadd['sum']=str(req_dict["face_count"])
    print(name_a)
    gyxadd['studentcode']=str(name_a)
    return json.dumps(gyxadd)


@app.route('/set',methods=['POST'])
def face_SetUserID():#为检测出的某一个人脸添加标识信息，该信息会在Search接口结果中返回，用来确定用户身份。
    json_dict = json.loads(request.get_data().decode('utf-8'))
    print(json_dict)
    name_s = json_dict['username']
    name_t = json_dict['password']
    url = 'https://api-cn.faceplusplus.com/facepp/v3/face/setuserid'
    params = {
            'api_key':key,
            'api_secret':secret,
            'face_token':name_t,
            'user_id':name_s
            }
    r = requests.post(url,data = params)
    req_dict = r.json()
    gyx_set={}
    gyx_set['ID']=str(req_dict["user_id"])
    code1=str(0)
    dbexec1('insert into Student (Studentname, Studentstate) values (\'{name}\', \'{state}\')'.format(name=name_s, state=code1))
    print("赋予该人ID:"+str(req_dict["user_id"]))
    #在此处调用这个函数
    return json.dumps(gyx_set)
    #print(req_dict)


def dbexec1(sql = ''):
    conn = pymysql.connect(host = '39.96.221.44', port = 3306, user = 'root', passwd = 'Gyx3034688', db = 'gyxdatabase')
    cursor = conn.cursor()
    effect_row = cursor.execute(sql)
    conn.commit()
    conn.close()

def dbexec2(sql = ''):
    conn = pymysql.connect(host = '39.96.221.44', port = 3306, user = 'root', passwd = 'Gyx3034688', db = 'gyxdatabase')
    cursor = conn.cursor()
    cursor.execute(sql)
    conn.commit()
    conn.close()
    data = cursor.fetchall()
    return(data)
    #print(data)
    #print(data[1][0])

def dbexec3(sql = ''):
    conn = pymysql.connect(host = '39.96.221.44', port = 3306, user = 'root', passwd = 'Gyx3034688', db = 'gyxdatabase')
    cursor = conn.cursor()
    cursor.execute(sql)
    conn.commit()
    conn.close()
    data1 = cursor.fetchall()
    return(data1)



@app.route('/search',methods=['POST'])
def detect1_face():
    json_dict = json.loads(request.get_data().decode('utf-8'))
    name = json_dict['username']
    filename="教师上传"+name+".jpeg"
    url = "https://api-cn.faceplusplus.com/facepp/v3/detect"

    with open(filename, "rb") as f:  # 转为二进制格式
        base64_data = base64.b64encode(f.read())  # 使用base64进行加密
    params = {
        'api_key': key,
        'api_secret': secret,
        'image_base64': base64_data,
        'return_attributes': "gender,age,ethnicity",
    }
    response = requests.post(url, data=params)
    # 以二进制读入图像，这个字典中open(filepath_S, "rb")返回的是二进制的图像文件，所以"image_file"是二进制文件，符合官网要求
    # POTS上传
    # response的内容是JSON格式
    req_dict = response.json()
    # 对其解码成字典格式
    # print(req_dict)
    # 输出
    facen=len(req_dict["faces"])
    print(facen)
    localface=req_dict["faces"]

    searchface_id = {}
    if facen!=0:
        face = req_dict["faces"]
        num = (len(face))
        b2=req_dict["faces"][0]
        c1=str(b2["face_token"])
        d = str(face_search(c1, facesetname))
        print(d)
        localrectangle=localface[0]["face_rectangle"]
        swidth=str(localrectangle["width"])
        stop=str(localrectangle["top"])
        sleft=str(localrectangle["left"])
        sheight=str(localrectangle["height"])
        sname=str(d)
        #print(d)
        dbexec1('UPDATE  Student SET Studentstate=1 where Studentname=\"'+d+'\";')

        for i in range(num-1):
            #print("第" + str(i + 1) + "个人脸:")
            #a = req_dict["faces"][i]
            #print("年龄:" + str(a["attributes"]["age"]["value"]))
            #print("性别:" + str(a["attributes"]["gender"]["value"]))
            # print("性别:"+str(a["attributes"]["ethnicity"]["value"]))
            b1 = req_dict["faces"][i+1]
            # c = b["face_token"];
            # print(c)
            # addface(facesetname, c)
            # face_SetUserID(c, userid)
            # ace_search(c, facesetname)
            c=str(b1["face_token"])
            d=str(face_search(c,facesetname))
            dbexec1('UPDATE  Student SET Studentstate=1 where Studentname=\"' + d + '\";')
            sname=sname+"+"+d
            swidth=swidth+"+"+str(localface[i+1]["face_rectangle"]["width"])
            stop=stop+"+"+str(localface[i+1]["face_rectangle"]["top"])
            sleft=sleft+"+"+str(localface[i+1]["face_rectangle"]["left"])
            sheight=sheight+"+"+str(localface[i+1]["face_rectangle"]["height"])
        searchface_id['name'] = str(sname)
        searchface_id['num'] = str(num)
        searchface_id['flag']=str("T")
        searchface_id['width']=str(swidth)
        searchface_id['top'] = str(stop)
        searchface_id['left'] = str(sleft)
        searchface_id['height'] = str(sheight)
        print(searchface_id)
    else:
        searchface_id['flag']=str("F")
    return json.dumps(searchface_id)

def face_search(facetokens,facesetname):
    url = 'https://api-cn.faceplusplus.com/facepp/v3/search'
    params = {
            'api_key':key,
            'api_secret':secret,
            'face_token':facetokens,
            'outer_id':facesetname,
            }
    r = requests.post(url,data = params)
    req_dict = r.json()
    print(req_dict)
    #print(req_dict)
    #print(req_dict["results"])
    id=str(req_dict["results"][0]["user_id"])
    #print("该人是："+str(req_dict["results"][0]["user_id"]))
    #print("可信度："+str(req_dict["results"][0]["confidence"]))

    return id;

@app.route('/lookup',methods=['POST'])
def lookup():
    data=dbexec2('SELECT Studentname FROM Student WHERE Studentstate=1')
    data1 = dbexec2('SELECT Studentname FROM Student WHERE Studentstate=0')
    q1=len(data)
    q2=len(data1)
    print(q1)
    print(q2)
    gyx_r={}


    if q1 != 0 and q2!=0:
        gyx_r['Pname']=data[0][0]
        gyx_r['UPname']=data1[0][0]
        gyx_r['flag'] = str("T")
        for i in range(q1-1):
          gyx_r['Pname']=gyx_r['Pname']+","+str(data[i+1][0])
        for i in range(q2-1):
          gyx_r['UPname'] = gyx_r['UPname'] + ","+str(data1[i+1][0])

    if q1==0 and (q2 !=0) :
        data2 = dbexec2('SELECT Studentname FROM Student WHERE Studentstate=0')
        q3 = len(data2)
        gyx_r['UPname1'] = data2[0][0]
        gyx_r['flag'] = str("F")
        for i in range(q3-1):
          gyx_r['UPname1']=gyx_r['UPname1']+","+str(data2[i+1][0])


    if q1!=0 and q2==0 :
        data2 = dbexec2('SELECT Studentname FROM Student WHERE Studentstate=1')
        q3 = len(data2)
        gyx_r['Pname'] = data2[0][0]
        print(data2)
        print(q3)
        gyx_r['flag'] = str("A")
        for i in range(q3 - 1):
            gyx_r['Pname'] = gyx_r['Pname'] + "," + str(data2[i + 1][0])
        print(gyx_r['Pname'])
    if q1==0 and (q2==0) :
         gyx_r['flag']= str("N")
    print(gyx_r['flag'])
    return json.dumps(gyx_r)

#查询历史数据
@app.route('/lookup1',methods=['POST'])
def lookup1():
    dict = json.loads(request.get_data().decode('utf-8'))
    lcourse = dict['username']
    tcourse1 = dict['password']
    tcourse=str('c')+str(tcourse1)
    data=dbexec2('SELECT name FROM {coursename} WHERE {time}=1'.format(coursename=lcourse,time=tcourse))
    data1 = dbexec2('SELECT name FROM {coursename} WHERE {time}=0'.format(coursename=lcourse,time=tcourse))
    q1=len(data)
    q2=len(data1)
    print(q1)
    print(q2)
    gyx_r={}


    if q1 != 0 and q2!=0:
        gyx_r['Pname']=data[0][0]
        gyx_r['UPname']=data1[0][0]
        for i in range(q1-1):
          gyx_r['Pname']=gyx_r['Pname']+","+str(data[i+1][0])
        for i in range(q2-1):
          gyx_r['UPname'] = gyx_r['UPname'] + ","+str(data1[i+1][0])
        gyx_r['flag']=str("T")
    if q1==0 and (q2 !=0) :
        data2 = data1
        q3 = len(data2)
        gyx_r['flag'] = str("F")
        gyx_r['UPname1'] = data2[0][0]
        for i in range(q3-1):
          gyx_r['UPname1']=gyx_r['UPname1']+","+str(data2[i+1][0])

        print(gyx_r['UPname1'])
    if q1!=0 and q2==0 :
        data2 = dbexec2('SELECT name FROM {coursename} WHERE {time}=1'.format(coursename=lcourse,time=tcourse))
        q3 = len(data2)
        gyx_r['flag'] = str("A")
        gyx_r['Pname'] = data2[0][0]
        for i in range(q3 - 1):
            gyx_r['Pname'] = gyx_r['Pname'] + "," + str(data2[i + 1][0])

        print(gyx_r['Pname'])
    if q1==0 and (q2==0) :
         gyx_r['flag']= str("N")
    print(gyx_r)
    return json.dumps(gyx_r)


@app.route('/fresh',methods=['POST'])
def fresh():
    dbexec('UPDATE  Student SET Studentstate=0 ')
    return json.dumps({"code": 1})

@app.route('/view',methods=['POST'])
def view():
    json_dict = json.loads(request.get_data().decode('utf-8'))
    d = json_dict['username']
    d1=str(d)
    f=dbexec2('SELECT Studentstate From Student WHERE Studentname=\"' + d1 + '\"')
    gyx_v={}
    gyx_v['Viewname']=str(d)
    gyx_v['Viewcode']=str(f[0][0])
    print(gyx_v["Viewcode"])
    return json.dumps(gyx_v)
@app.route('/addcourse',methods=['POST'])
def addcourse():
    dict = json.loads(request.get_data().decode('utf-8'))
    coursename = dict['username']
    dbexec1( 'drop table if exists {name} ;'.format(name=coursename))
    dbexec1('CREATE table   {name}  (name varchar(20) ,c1 varchar(20) , c2 varchar(20) ,c3 varchar(20) ,c4 varchar(20) ,c5 varchar(20) ,c6 varchar(20) ,c7 varchar(20) ,c8 varchar(20) ,c9 varchar(20) ,c10 varchar(20) ,c11 varchar(20) ,c12 varchar(20) ,c13  varchar(20) ,c14 varchar(20) ,c15 varchar(20) ,c16 varchar(20) ,c17 varchar(20)) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;'.format(name=coursename))
    dbexec1('insert into {name} (name) select  Studentname from Student'.format(name=coursename))
    return json.dumps({"code": 1})

@app.route('/addcourse1',methods=['POST'])
def addcourse1():
    dict = json.loads(request.get_data().decode('utf-8'))
    data = dbexec2('SELECT Studentname FROM Student WHERE Studentstate=1')
    data1 = dbexec2('SELECT Studentname FROM Student WHERE Studentstate=0')
    qs1=len(data)
    qs2=len(data1)
    print(qs1)
    print(qs2)
    print(dict)
    if qs1==0 and qs2 == 0 :
     print("没有学生！")

#无学生数据
    if qs1==0 and qs2!=0:
        print("缺席")
        coursename1 = dict['course']
        coursenumber = dict['coursenumber']
        studentUPnameadd = dict['studentUPlist']
        UPadd = studentUPnameadd.split(',')
        addnumber = str('c') + str(coursenumber)
        UPaddlen = len(UPadd)
        for i in range(UPaddlen):
            dbexec1('UPDATE  {coursename} SET {num}=0 where name=\'{name}\';'.format(coursename=coursename1, num=addnumber,name=UPadd[i]))
            print(UPadd[i])
#无签到的，都是缺席的
    if qs1!=0 and qs2==0:
        print("签到")
        coursename1 = dict['course']
        coursenumber = dict['coursenumber']
        studentPnameadd = dict['studentPlist']
        Padd = studentPnameadd.split(',')
        addnumber = str('c') + str(coursenumber)

        Paddlen = len(Padd)
        for i in range(Paddlen):
            dbexec1(
                'UPDATE  {coursename} SET {num}=1 where name=\'{name}\';'.format(coursename=coursename1, num=addnumber,name=Padd[i]))
            print(Padd[i])

#都是签到的的
    if qs1!=0 and  qs2!=0:
        print("正常")
        coursename1 = dict['course']
        coursenumber=dict['coursenumber']
        studentPnameadd=dict['studentPlist']
        studentUPnameadd=dict['studentUPlist']
        Padd=studentPnameadd.split(',')
        UPadd=studentUPnameadd.split(',')
        addnumber=str('c')+str(coursenumber)

        Paddlen=len(Padd)
        UPaddlen=len(UPadd)
        for i in range(Paddlen):
            dbexec1('UPDATE  {coursename} SET {num}=1 where name=\'{name}\';'.format(coursename=coursename1,num=addnumber,name=Padd[i]))
            print(Padd[i])
        for i in range(UPaddlen):
            dbexec1('UPDATE  {coursename} SET {num}=0 where name=\'{name}\';'.format(coursename=coursename1, num=addnumber,name=UPadd[i]))
            print(UPadd[i])
    return json.dumps({"code": 1})




@app.route('/deletestudent',methods=['POST'])
def deletestudent():
    dict = json.loads(request.get_data().decode('utf-8'))
    ds = dict['username']
    dcourse = dict['password']
    dbexec1('DELETE from {coursename} WHERE name=\'{name}\''.format(coursename=dcourse,name=ds))
    return json.dumps({"code": 1})


if __name__ == '__main__':
    app.run(host='0.0.0.0')
